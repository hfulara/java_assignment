package com.fulfilment.application.monolith.warehouses.domain.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.location.LocationGateway;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;
import java.util.Optional;

class CreateWarehouseUseCaseTest {

    @Mock
    WarehouseStore warehouseStore;

    @Mock
    LocationGateway locationGateway;

    CreateWarehouseUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new CreateWarehouseUseCase(warehouseStore);
    }

    @Test
    void shouldCreateWarehouseSuccessfully() {
        Warehouse warehouse = warehouse("MWH-001", "ZWOLLE-001", 100, 50);

        when(warehouseStore.findByBusinessUnitCode("MWH-001")).thenReturn(null);
        when(locationGateway.resolveByIdentifier("ZWOLLE-001"))
                .thenReturn(Optional.of(new Location("ZWOLLE-001", 1, 200)));

        assertDoesNotThrow(() -> useCase.create(warehouse));
        verify(warehouseStore).create(warehouse);
    }

    @Test
    void shouldFailWhenBusinessUnitCodeAlreadyExists() {
        Warehouse warehouse = warehouse("MWH-001", "ZWOLLE-001", 100, 50);

        // Simulate existing warehouse
        when(warehouseStore.findByBusinessUnitCode("MWH-001"))
                .thenReturn(existingWarehouse("MWH-001"));


        assertThrows(
                IllegalArgumentException.class,
                () -> useCase.create(warehouse));
    }

    @Test
    void shouldFailWhenLocationIsInvalid() {
        Warehouse warehouse = warehouse("MWH-001", "INVALID", 100, 50);

        when(warehouseStore.findByBusinessUnitCode("MWH-001")).thenReturn(null);
        when(locationGateway.resolveByIdentifier("INVALID")).thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> useCase.create(warehouse));
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
    private Warehouse existingWarehouse(String buCode) {
        Warehouse w = new Warehouse();
        w.businessUnitCode = buCode;
        w.location = "ZWOLLE-001";
        w.capacity = 200;
        w.stock = 100;
        w.createdAt = LocalDateTime.now();
        return w;
    }

}
