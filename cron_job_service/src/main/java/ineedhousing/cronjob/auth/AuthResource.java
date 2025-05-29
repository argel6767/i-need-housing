package ineedhousing.cronjob.auth;

import ineedhousing.cronjob.auth.requests.RegistrationDto;
import jakarta.annotation.Resource;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/auth")
@Resource
public class AuthResource {

    private final AuthUserService authUserService;

    public AuthResource(AuthUserService authUserService) {
        this.authUserService = authUserService;
    }

    /**
     *
     * @param request
     * @return
     */
    @POST
    @Path("/register")
    public Response register(RegistrationDto request) {
        String registerMessage = authUserService.register(request);
        return switch (registerMessage) {
            case "Registration key is incorrect" -> Response.status(403).entity(registerMessage).build();
            case "Username is already taken, choose another" -> Response.status(409).entity(registerMessage).build();
            default -> Response.status(201).entity(registerMessage).build();
        };
    }

}
