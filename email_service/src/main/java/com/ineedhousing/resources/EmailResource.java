package com.ineedhousing.resources;

import com.ineedhousing.models.requests.KeyRotationEvent;
import com.ineedhousing.models.requests.NewListingsMadeEvent;
import com.ineedhousing.models.requests.NewServiceRegisteredEvent;
import com.ineedhousing.models.requests.VerificationCodeDto;
import com.ineedhousing.services.ClientEmailService;
import com.ineedhousing.services.ServiceInteractionEmailService;
import io.smallrye.faulttolerance.api.RateLimit;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.temporal.ChronoUnit;

@Path("/v1/emails")
public class EmailResource {

    @Inject
    ServiceInteractionEmailService serviceInteractionEmailService;

    @Inject
    ClientEmailService clientEmailService;

    @POST
    @Path("/services/key-rotation")
    @Consumes(MediaType.APPLICATION_JSON)
    @RateLimit(value = 20, window = 10, windowUnit = ChronoUnit.MINUTES)
    public Response keyRotation(KeyRotationEvent keyRotationEvent) {
        serviceInteractionEmailService.sendKeyRotationEmail(keyRotationEvent);
        return Response.status(Response.Status.ACCEPTED).build();
    }

    @POST
    @Path("/services/new-listings-made")
    @Consumes(MediaType.APPLICATION_JSON)
    @RateLimit(value = 20, window = 10, windowUnit = ChronoUnit.MINUTES)
    public Response newListingMade(NewListingsMadeEvent newListingsMadeEvent) {
        serviceInteractionEmailService.sendNewListingsEmail(newListingsMadeEvent);
        return Response.status(Response.Status.ACCEPTED).build();
    }

    @POST
    @Path("/services/service-registered")
    @Consumes(MediaType.APPLICATION_JSON)
    @RateLimit(value = 20, window = 10, windowUnit = ChronoUnit.MINUTES)
    public Response serviceRegistered(NewServiceRegisteredEvent newServiceRegisteredEvent) {
        serviceInteractionEmailService.sendServiceRegisteredEmail(newServiceRegisteredEvent);
        return Response.status(Response.Status.ACCEPTED).build();
    }

    @POST
    @Path("/clients/verify")
    @Consumes(MediaType.APPLICATION_JSON)
    @RateLimit(value = 20, window = 10, windowUnit = ChronoUnit.MINUTES)
    public Response verifyEmail(VerificationCodeDto  verificationCodeDto) {
        clientEmailService.sendEmailVerificationEmail(verificationCodeDto, 0);
        return Response.status(Response.Status.ACCEPTED).build();
    }

    @POST
    @Path("/clients/reset-password")
    @Consumes(MediaType.APPLICATION_JSON)
    @RateLimit(value = 20, window = 10, windowUnit = ChronoUnit.MINUTES)
    public Response resetPassword(VerificationCodeDto verificationCodeDto) {
        clientEmailService.sendResetPasswordEmail(verificationCodeDto, 0);
        return Response.status(Response.Status.ACCEPTED).build();
    }
}
