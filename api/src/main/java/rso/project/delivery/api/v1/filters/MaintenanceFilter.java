package rso.project.delivery.api.v1.filters;

import rso.project.delivery.services.config.RestProperties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@ApplicationScoped
public class MaintenanceFilter implements ContainerRequestFilter {
    @Inject
    private RestProperties restProperties;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        if (restProperties.isMaintenanceMode()) {
            requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).entity("{\"message\" : \"Maintenance mode enabled\"}").build());
        }
    }
}
