package com.fulfilment.application.monolith.fulfilment.adapters.restapi;

import com.fulfilment.application.monolith.fulfilment.domain.modals.FulfilmentRequest;
import com.fulfilment.application.monolith.fulfilment.domain.ports.FulfilmentStore;
import com.fulfilment.application.monolith.fulfilment.domain.usecases.AssignFulfilmentUseCase;
import com.fulfilment.application.monolith.fulfilment.domain.usecases.commands.AssignFulfilmentCommand;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/fulfilment")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class FulfilmentResourceImpl {

    @Inject
    AssignFulfilmentUseCase assignFulfilmentUseCase;

    @POST
    @Transactional
    public Response assign(FulfilmentRequest request) {

        try {
            assignFulfilmentUseCase.execute(
                    new AssignFulfilmentCommand(
                            request.storeId,
                            request.productId,
                            request.warehouseId
                    )
            );
            return Response.status(Response.Status.CREATED).build();
        } catch (IllegalArgumentException e) {
            throw new WebApplicationException(e.getMessage(), 400);
        }
    }
    @GET
    public void getAll() {

    }
}
