package com.fulfilment.application.monolith.warehouses.domain.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;

class ArchiveWarehouseUseCaseTest {

    @Mock
    WarehouseStore warehouseStore;

    ArchiveWarehouseUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new ArchiveWarehouseUseCase(warehouseStore);
    }

    @Test
    void shouldArchiveWarehouseSuccessfully() {
        Warehouse warehouse = warehouse("MWH-001", "ZWOLLE-001", 100, 50);

        when(warehouseStore.findByBusinessUnitCode("MWH-001"))
                .thenReturn(warehouse);

        useCase.archive(warehouse);

        assertTrue(true);
    }

    @Test
    void shouldFailWhenWarehouseDoesNotExist() {
        Warehouse warehouse = new Warehouse();
        warehouse.businessUnitCode = "MWH-404";

        when(warehouseStore.findByBusinessUnitCode("MWH-404"))
                .thenReturn(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> useCase.archive(warehouse)
        );
    }
    private Warehouse warehouse(
            String buCode, String location, int capacity, int stock) {

        Warehouse w = new Warehouse();
        w.businessUnitCode = buCode;
        w.location = location;
        w.capacity = capacity;
        w.stock = stock;
        w.createdAt = LocalDateTime.now();
        return w;
    }
}
