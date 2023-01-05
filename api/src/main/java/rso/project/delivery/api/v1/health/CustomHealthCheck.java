package rso.project.delivery.api.v1.health;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;
import rso.project.delivery.services.config.RestProperties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Liveness
@ApplicationScoped
public class CustomHealthCheck implements HealthCheck {
    @Inject
    private RestProperties restProperties;

    @Override
    public HealthCheckResponse call() {
        if (restProperties.isBroken()) {
            return HealthCheckResponse.down(CustomHealthCheck.class.getName());
        } else {
            return HealthCheckResponse.up(CustomHealthCheck.class.getName());
        }
    }
}
