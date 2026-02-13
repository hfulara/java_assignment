package com.fulfilment.application.monolith.fulfilment.domain.usecases;

import com.fulfilment.application.monolith.fulfilment.domain.models.Fulfilment;
import com.fulfilment.application.monolith.fulfilment.domain.ports.FulfilmentStore;
import com.fulfilment.application.monolith.fulfilment.domain.usecases.commands.AssignFulfilmentCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssignFulfilmentUseCaseTest {

    private FulfilmentStore fulfilmentStore;
    private AssignFulfilmentUseCase useCase;

    @BeforeEach
    void setup() {
        fulfilmentStore = Mockito.mock(FulfilmentStore.class);
        useCase = new AssignFulfilmentUseCase(fulfilmentStore);
    }

    @Test
    void shouldAssignWhenAllConstraintsSatisfied() {
        when(fulfilmentStore.countWarehousesByStoreAndProduct(1L, 1L))
                .thenReturn(1);
        when(fulfilmentStore.countWarehousesByStore(1L))
                .thenReturn(2);
        when(fulfilmentStore.countProductsByWarehouse(1L))
                .thenReturn(4);
        when(fulfilmentStore.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Fulfilment result = useCase.execute(
                new AssignFulfilmentCommand(1L, 1L, 1L)
        );

        assertNotNull(result);
        verify(fulfilmentStore, times(1)).save(any());
    }

    @Test
    void shouldThrowWhenMoreThanTwoWarehousesPerProductPerStore() {

        when(fulfilmentStore.countWarehousesByStoreAndProduct(1L, 1L))
                .thenReturn(2);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute(
                        new AssignFulfilmentCommand(1L, 1L, 1L)
                )
        );

        assertEquals("Max 2 warehouses per product per store", ex.getMessage());

        verify(fulfilmentStore, never()).save(any());
    }

    @Test
    void shouldThrowWhenMoreThanThreeWarehousesPerStore() {

        when(fulfilmentStore.countWarehousesByStoreAndProduct(1L, 1L))
                .thenReturn(1); // valid

        when(fulfilmentStore.countWarehousesByStore(1L))
                .thenReturn(3); // violation

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute(
                        new AssignFulfilmentCommand(1L, 1L, 1L)
                )
        );

        assertEquals("Max 3 warehouses per store", ex.getMessage());

        verify(fulfilmentStore, never()).save(any());
    }

    @Test
    void shouldThrowWhenMoreThanFiveProductsPerWarehouse() {

        when(fulfilmentStore.countWarehousesByStoreAndProduct(1L, 1L))
                .thenReturn(1);

        when(fulfilmentStore.countWarehousesByStore(1L))
                .thenReturn(2);

        when(fulfilmentStore.countProductsByWarehouse(1L))
                .thenReturn(5); // violation

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute(
                        new AssignFulfilmentCommand(1L, 1L, 1L)
                )
        );

        assertEquals("Warehouse can store max 5 products", ex.getMessage());

        verify(fulfilmentStore, never()).save(any());
    }

    @Test
    void shouldAllowWhenExactlyAtBoundaryMinusOne() {

        when(fulfilmentStore.countWarehousesByStoreAndProduct(1L, 1L))
                .thenReturn(1);

        when(fulfilmentStore.countWarehousesByStore(1L))
                .thenReturn(2);

        when(fulfilmentStore.countProductsByWarehouse(1L))
                .thenReturn(4);

        when(fulfilmentStore.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Fulfilment result = useCase.execute(
                new AssignFulfilmentCommand(1L, 1L, 1L)
        );

        assertNotNull(result);
        verify(fulfilmentStore, times(1)).save(any());
    }
}
