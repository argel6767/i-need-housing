package ineedhousing.cronjob;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
@Path("")
public class PingResource {

    /**
     * Pings service to wake up/check on it
     * @return
     */
    @GET
    @Path("/ping")
    public Response pingService() {
        return Response.ok().build();
    }

}
