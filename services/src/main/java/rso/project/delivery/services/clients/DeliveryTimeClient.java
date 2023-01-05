package rso.project.delivery.services.clients;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.json.JSONArray;
import org.json.JSONObject;
import rso.project.delivery.services.config.AppProperties;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.temporal.ChronoUnit;

@ApplicationScoped
public class DeliveryTimeClient {
    HttpGet httpGetDistance = new HttpGet("https://distance-calculator8.p.rapidapi.com/calc");
    HttpGet httpGetGeocoder = new HttpGet("http://api.positionstack.com/v1/forward");
    HttpGet httpGetDelivery = new HttpGet("http://localhost:8080/v1/deliveries"); // demo (not final)
    @Inject
    private AppProperties appProperties;
    private HttpClient httpClient;
    private HttpResponse response;
    private Object locationLatitude;
    private Object locationLongitude;
    private Object destinationLocationLatitude;
    private Object destinationLocationLongitude;

    @PostConstruct
    private void Init() {
        httpClient = HttpClientBuilder.create().build();
        httpGetDistance.addHeader("X-RapidAPI-Key", appProperties.getDistanceKey());
        httpGetDistance.addHeader("X-RapidAPI-Host", "distance-calculator8.p.rapidapi.com");
    }

    public boolean GeocodeAddresses(int deliveryId) {
        Object location;
        Object destinationLocation;
        try {
            HttpGet tmpHttpGetDelivery = new HttpGet(new URIBuilder(httpGetDelivery.getURI() + "/" + deliveryId).build());
            response = httpClient.execute(tmpHttpGetDelivery);
            JSONObject jsonObject = new JSONObject(EntityUtils.toString(response.getEntity()));

            location = jsonObject.get("location");
            Object restaurantLocation = jsonObject.get("restaurantLocation"); // todo
            destinationLocation = jsonObject.get("destinationLocation");

            System.out.println("Locations got: " + location + " " + restaurantLocation + " " + destinationLocation);
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }

        try {
            // current location
            Object[] locations = GetGeolocation(location);
            locationLatitude = locations[0];
            locationLongitude = locations[1];

            // destination location
            locations = GetGeolocation(destinationLocation);
            destinationLocationLatitude = locations[0];
            destinationLocationLongitude = locations[1];
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public Object fallbackMethod(int deliveryId) {
        return null;
    }

    private Object[] GetGeolocation(Object location) throws URISyntaxException, IOException {
        httpGetGeocoder.setURI(new URIBuilder(httpGetGeocoder.getURI())
                .addParameter("access_key", appProperties.getGeocoderKey())
                .addParameter("query", location.toString()).build());

        response = httpClient.execute(httpGetGeocoder);
        JSONObject jsonObject = new JSONObject(EntityUtils.toString(response.getEntity()));
        JSONArray data = jsonObject.getJSONArray("data");

        Object latitude = data.getJSONObject(0).get("latitude"); // get the first results only
        Object longitude = data.getJSONObject(0).get("longitude");
        return new Object[]{latitude, longitude};
    }

    @Timeout(value = 8, unit = ChronoUnit.SECONDS)
    @CircuitBreaker(requestVolumeThreshold = 10)
    @Fallback(fallbackMethod = "fallbackMethod")
    public Object CalculateDistance(int deliveryId) {
        if (GeocodeAddresses(deliveryId)) {
            try {
                httpGetDistance.setURI(new URIBuilder(httpGetDistance.getURI())
                        .addParameter("startLatitude", locationLatitude.toString())
                        .addParameter("startLongitude", locationLongitude.toString())
                        .addParameter("endLatitude", destinationLocationLatitude.toString())
                        .addParameter("endLongitude", destinationLocationLongitude.toString())
                        .build());

                response = httpClient.execute(httpGetDistance);
                JSONObject jsonObject = new JSONObject(EntityUtils.toString(response.getEntity()));
                return jsonObject.getJSONObject("body").getJSONObject("distance").get("kilometers");
            } catch (URISyntaxException | IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return null;
        }
    }
}
