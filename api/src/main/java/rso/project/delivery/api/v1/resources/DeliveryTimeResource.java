package rso.project.delivery.api.v1.resources;

import com.kumuluz.ee.logs.cdi.Log;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import rso.project.delivery.lib.DeliveryTimeMetadata;
import rso.project.delivery.services.clients.DeliveryTimeClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
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
                            schema = @Schema(implementation = DeliveryTimeMetadata.class))
            )})
    @GET
    @Path("/{deliveryId}")
    public Response getDeliveryTime(@Parameter(description = "Metadata ID.", required = true)
                                    @PathParam("deliveryId") Integer deliveryId) {
        deliveryTimeClient.GeocodeAddresses(deliveryId);
        double distanceKm = deliveryTimeClient.CalculateDistance();
        System.out.println("Distance from location to destination location: " + distanceKm + " km");

        DeliveryTimeMetadata metadata = new DeliveryTimeMetadata();
        metadata.setDeliveryId(deliveryId);
        metadata.setDeliveryDistanceKm(distanceKm);

        return Response.status(Response.Status.OK).entity(metadata).build();
    }
}
