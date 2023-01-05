package rso.project.delivery.api.v1.resources;

import com.kumuluz.ee.logs.cdi.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.json.JSONArray;
import org.json.JSONObject;
import rso.project.delivery.lib.DeliveryMetadata;
import rso.project.delivery.services.clients.DeliveryTimeClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Logger;

@Log
@ApplicationScoped
@Path("/deliveryTime")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DeliveryTimeResource {

    private final Logger log = Logger.getLogger(DeliveryTimeResource.class.getName());
    @Context
    protected UriInfo uriInfo;

    @Inject
    private DeliveryTimeClient deliveryTimeClient;

    @Operation(description = "Calculate time for a delivery.", summary = "Get time for a delivery")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Delivery time",
                    content = @Content(
                            schema = @Schema(implementation = DeliveryMetadata.class))
            )})
    @GET
    @Path("/{deliveryId}")
    public Response getDeliveryTime(@Parameter(description = "Metadata ID.", required = true)
                                        @PathParam("deliveryId") Integer deliveryId) {

//        DeliveryMetadata deliveryMetadata = deliveryMetadataBean.getDeliveryMetadata(imageMetadataId);
//
//        if (deliveryMetadata == null) {
//            return Response.status(Response.Status.NOT_FOUND).build();
//        }

        HttpClient httpClient = HttpClientBuilder.create().build();

        HttpGet httpGet = new HttpGet("https://distance-calculator8.p.rapidapi.com/calc");
        httpGet.addHeader("X-RapidAPI-Key", "197be5d856mshf891aa29c50ec04p1d3d61jsna8cb6a927b9b");
        httpGet.addHeader("X-RapidAPI-Host", "distance-calculator8.p.rapidapi.com");
        URI uri = null;
        try {
            uri = new URIBuilder(httpGet.getURI())
                    .addParameter("startLatitude", "-26.311960")
                    .addParameter( "startLongitude","-48.880964")
                    .addParameter( "endLatitude","-26.313662")
                    .addParameter( "endLongitude","-48.881103")
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        httpGet.setURI(uri);
        try {
            HttpResponse response = httpClient.execute(httpGet);

            String json = EntityUtils.toString(response.getEntity());
            JSONObject obj = new JSONObject(json);
            System.out.println(obj.getJSONObject("body").getJSONObject("distance").get("kilometers"));
            System.out.println(response.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        httpGet = new HttpGet("http://api.positionstack.com/v1/forward");
        uri = null;
        try {
            uri = new URIBuilder(httpGet.getURI())
                    .addParameter("access_key", "be886d430eead1e57fd5c3db1f805ded")
                    .addParameter( "query","1600 Pennsylvania Ave NW, Washington DC")
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        httpGet.setURI(uri);
        try {
            HttpResponse response = httpClient.execute(httpGet);

            String json = EntityUtils.toString(response.getEntity());

            JSONObject obj = new JSONObject(json);
            JSONArray data = obj.getJSONArray("data");
            // get first results
            Object latitude = data.getJSONObject(0).get("latitude");
            Object longitude = data.getJSONObject(0).get("longitude");
            System.out.println(response.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


//        return Response.status(Response.Status.OK).entity(deliveryMetadata).build();
        return Response.status(Response.Status.OK).build();
    }


}
