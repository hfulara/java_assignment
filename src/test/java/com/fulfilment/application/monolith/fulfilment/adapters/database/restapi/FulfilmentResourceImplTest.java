package com.fulfilment.application.monolith.fulfilment.adapters.database.restapi;

import com.fulfilment.application.monolith.fulfilment.adapters.restapi.FulfilmentResourceImpl;
import com.fulfilment.application.monolith.fulfilment.domain.modals.FulfilmentRequest;
import com.fulfilment.application.monolith.fulfilment.domain.usecases.AssignFulfilmentUseCase;
import com.fulfilment.application.monolith.fulfilment.domain.usecases.commands.AssignFulfilmentCommand;
import jakarta.ws.rs.WebApplicationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class FulfilmentResourceImplTest {

    @Mock
    AssignFulfilmentUseCase assignFulfilmentUseCase;

    @InjectMocks
    FulfilmentResourceImpl resource;

    FulfilmentRequest request;

    @BeforeEach
    void setup() {
        request = new FulfilmentRequest();
        request.storeId = 1L;
        request.productId = 10L;
        request.warehouseId = 100L;
    }

    // ---------------------------------------------------------
    // SUCCESS CASE
    // ---------------------------------------------------------

    @Test
    void shouldCallUseCaseSuccessfully() {

        resource.assign(request);

        ArgumentCaptor<AssignFulfilmentCommand> captor =
                ArgumentCaptor.forClass(AssignFulfilmentCommand.class);

        verify(assignFulfilmentUseCase).execute(captor.capture());

        AssignFulfilmentCommand command = captor.getValue();

        assertEquals(1L, command.storeId());
        assertEquals(10L, command.productId());
        assertEquals(100L, command.warehouseId());
    }

    // ---------------------------------------------------------
    // ERROR CASE
    // ---------------------------------------------------------

    @Test
    void shouldReturn400WhenUseCaseThrowsIllegalArgumentException() {

        doThrow(new IllegalArgumentException("Business rule violated"))
                .when(assignFulfilmentUseCase)
                .execute(any());

        WebApplicationException exception =
                assertThrows(WebApplicationException.class,
                        () -> resource.assign(request));

        assertEquals(400, exception.getResponse().getStatus());
        assertEquals("Business rule violated", exception.getMessage());
    }
}
