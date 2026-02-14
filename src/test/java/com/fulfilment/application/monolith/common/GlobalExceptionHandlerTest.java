package com.fulfilment.application.monolith.common;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class GlobalExceptionHandlerTest {

    GlobalExceptionHandler handler;
    UriInfo uriInfo;

    @BeforeEach
    void setup() {
        handler = new GlobalExceptionHandler();
        uriInfo = Mockito.mock(UriInfo.class);

        Mockito.when(uriInfo.getPath()).thenReturn("test/path");

        handler.uriInfo = uriInfo; // manually inject
    }

    // ---------------------------------------------------------
    // BusinessException
    // ---------------------------------------------------------

    @Test
    void shouldHandleBusinessException() {
        BusinessException exception =
                new BusinessException("Stock exceeds capacity");

        Response response = handler.toResponse(exception);

        assertEquals(400, response.getStatus());

        ApiError error = (ApiError) response.getEntity();

        assertEquals(400, error.status);
        assertEquals("Business Rule Violation", error.error);
        assertEquals("Stock exceeds capacity", error.message);
        assertEquals("test/path", error.path);
        assertNotNull(error.timestamp);
    }

    @Test
    void shouldHandleNotFoundException() {
        NotFoundException exception =
                new NotFoundException("Resource not found");

        Response response = handler.toResponse(exception);

        assertEquals(404, response.getStatus());

        ApiError error = (ApiError) response.getEntity();

        assertEquals(404, error.status);
        assertEquals("Not Found", error.error);
        assertEquals("Resource not found", error.message);
        assertEquals("test/path", error.path);
    }

    @Test
    void shouldHandleWebApplicationException() {
        WebApplicationException exception =
                new WebApplicationException("Forbidden", 403);

        Response response = handler.toResponse(exception);

        assertEquals(403, response.getStatus());

        ApiError error = (ApiError) response.getEntity();

        assertEquals(403, error.status);
        assertEquals("Forbidden", error.error);
        assertEquals("Forbidden", error.message);
    }

    @Test
    void shouldHandleGenericException() {
        RuntimeException exception =
                new RuntimeException("Unexpected error");

        Response response = handler.toResponse(exception);

        assertEquals(500, response.getStatus());

        ApiError error = (ApiError) response.getEntity();

        assertEquals(500, error.status);
        assertEquals("Internal Server Error", error.error);
        assertEquals("Unexpected error", error.message);
    }
}
