package com.fulfilment.application.monolith.fulfilment;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.fulfilment.application.monolith.products.Product;
import com.fulfilment.application.monolith.stores.Store;
import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AssignFulfilmentUseCaseTest {
    private FulfilmentRepository fulfilmentRepository;
    private WarehouseRepository warehouseRepository;
    private AssignFulfilmentUseCase assignFulfilmentUseCase;

    private Store store;
    private Product product;
    private Warehouse warehouse;

    @BeforeEach
    void setup() {
        fulfilmentRepository = mock(FulfilmentRepository.class);
        warehouseRepository = mock(WarehouseRepository.class);

        assignFulfilmentUseCase = new AssignFulfilmentUseCase();
        assignFulfilmentUseCase.repository = fulfilmentRepository;
        assignFulfilmentUseCase.warehouseRepository = warehouseRepository;

        store = new Store("Store-A");
        product = new Product("Product-X");

        warehouse = new Warehouse();
        warehouse.businessUnitCode = "WH-001";
    }

    @Test
    void shouldAssignFulfilmentSuccessfully() {

        when(fulfilmentRepository.exists(store, product, warehouse)).thenReturn(false);
        when(fulfilmentRepository.countWarehousesForProductInStore(product, store)).thenReturn(1L);
        when(fulfilmentRepository.countWarehousesForStore(store)).thenReturn(2L);
        when(fulfilmentRepository.countProductsInWarehouse(warehouse)).thenReturn(3L);

        assertDoesNotThrow(() ->
                assignFulfilmentUseCase.assign(store, product, warehouse)
        );

        verify(fulfilmentRepository).persist(any(FulfilmentAssignment.class));
    }

    @Test
    void shouldFailWhenProductHasMoreThanTwoWarehousesPerStore() {

        when(fulfilmentRepository.exists(store, product, warehouse)).thenReturn(false);
        when(fulfilmentRepository.countWarehousesForProductInStore(product, store)).thenReturn(2L);

        IllegalArgumentException ex =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> assignFulfilmentUseCase.assign(store, product, warehouse)
                );

        assertTrue(ex.getMessage().contains("max 2"));
    }

    @Test
    void shouldFailWhenStoreHasMoreThanThreeWarehouses() {

        when(fulfilmentRepository.exists(store, product, warehouse)).thenReturn(false);
        when(fulfilmentRepository.countWarehousesForProductInStore(product, store)).thenReturn(1L);
        when(fulfilmentRepository.countWarehousesForStore(store)).thenReturn(3L);

        IllegalArgumentException ex =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> assignFulfilmentUseCase.assign(store, product, warehouse)
                );

        assertTrue(ex.getMessage().contains("max 3"));
    }

    @Test
    void shouldFailWhenWarehouseHasMoreThanFiveProducts() {

        when(fulfilmentRepository.exists(store, product, warehouse)).thenReturn(false);
        when(fulfilmentRepository.countWarehousesForProductInStore(product, store)).thenReturn(1L);
        when(fulfilmentRepository.countWarehousesForStore(store)).thenReturn(2L);
        when(fulfilmentRepository.countProductsInWarehouse(warehouse)).thenReturn(5L);

        IllegalArgumentException ex =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> assignFulfilmentUseCase.assign(store, product, warehouse)
                );

        assertTrue(ex.getMessage().contains("max 5"));
    }

    @Test
    void shouldNotCreateDuplicateAssignment() {
        when(fulfilmentRepository.exists(store, product, warehouse)).thenReturn(true);
        assertDoesNotThrow(() ->
                assignFulfilmentUseCase.assign(store, product, warehouse)
        );

        verify(fulfilmentRepository, never()).persist(any());
    }
}
