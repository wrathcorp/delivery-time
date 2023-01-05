package rso.project.delivery.api.v1;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;


import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

@OpenAPIDefinition(info = @Info(title = "Delivery Time API", version = "v1",
        contact = @Contact(email = "jovanprodanov123@gmail.com"),
        license = @License(name = "dev"), description = "API for calculating time to deliver metadata."),
        servers = @Server(url = "http://localhost:8081/"))
@ApplicationPath("/v1")
public class DeliveryTimeApplication extends Application {

}
