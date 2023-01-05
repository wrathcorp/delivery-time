package rso.project.delivery.services.config;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

import javax.enterprise.context.ApplicationScoped;

@ConfigBundle
@ApplicationScoped
public class RestProperties {
    @ConfigValue(watch = true)
    private boolean maintenanceMode;

    private boolean broken;

    public boolean isMaintenanceMode() {
        return this.maintenanceMode;
    }

    public void setMaintenanceMode(boolean maintenanceMode) {
        this.maintenanceMode = maintenanceMode;
    }

    public boolean isBroken() {
        return broken;
    }

    public void setBroken(boolean broken) {
        this.broken = broken;
    }
}
