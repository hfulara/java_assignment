package com.fulfilment.application.monolith.common;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Throwable> {
    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(Throwable exception) {
        int status = 500;
        String error = "Internal Server Error";
        String message = exception.getMessage();

        if (exception instanceof BusinessException businessEx) {
            status = 400;
            error = "Business Rule Violation";
            message = businessEx.getMessage();
        }
        else if (exception instanceof WebApplicationException webEx) {
            status = webEx.getResponse().getStatus();
            error = Response.Status.fromStatusCode(status).getReasonPhrase();
        }
        else if (exception instanceof NotFoundException notFoundEx) {
            status = notFoundEx.getResponse().getStatus();
            error = "Resource Not Found";
            message = notFoundEx.getMessage();
        }
        else if (exception instanceof WebApplicationException webEx) {
            status = webEx.getResponse().getStatus();
            error = Response.Status.fromStatusCode(status).getReasonPhrase();
        }

        ApiError apiError = new ApiError(
                status,
                error,
                message,
                uriInfo.getPath()
        );

        return Response.status(status).entity(apiError).build();
    }
}
