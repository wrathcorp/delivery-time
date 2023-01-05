package rso.project.delivery.services.config;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@ConfigBundle("app-properties")
public class AppProperties {
    @ConfigValue(watch = true)
    private boolean healthy;

    @ConfigValue("delivery-time-client.geocoder-key")
    private String geocoderKey;

    @ConfigValue("delivery-time-client.distance-key")
    private String distanceKey;

    public String getGeocoderKey() {
        return geocoderKey;
    }

    public void setGeocoderKey(String geocoderKey) {
        this.geocoderKey = geocoderKey;
    }

    public String getDistanceKey() {
        return distanceKey;
    }

    public void setDistanceKey(String distanceKey) {
        this.distanceKey = distanceKey;
    }
}
