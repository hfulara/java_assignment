package com.fulfilment.application.monolith.warehouses.domain.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fulfilment.application.monolith.location.LocationGateway;
import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ReplaceWarehouseUseCaseTest {

    @Mock WarehouseStore warehouseStore;
    ReplaceWarehouseUseCase useCase;

    @Mock
    LocationGateway locationGateway;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new ReplaceWarehouseUseCase(warehouseStore);
    }

    @Test
    void shouldReplaceWarehouseSuccessfully() {
        Warehouse oldWarehouse =
                warehouse("MWH-001", 100, 50);

        Warehouse newWarehouse =
                warehouse("MWH-001", 150, 50);

        when(warehouseStore.findByBusinessUnitCode("MWH-001"))
                .thenReturn(oldWarehouse);

        Location location = new Location("ZWOLLE-001", 2,300);

        when(locationGateway.resolveByIdentifier("ZWOLLE-001"))
                .thenReturn(Optional.of(location));

        assertDoesNotThrow(() -> useCase.replace(newWarehouse));

        assertNotNull(oldWarehouse.archivedAt);


        verify(warehouseStore).create(newWarehouse);
    }

    @Test
    void shouldFailWhenNewCapacityIsInsufficient() {
        Warehouse oldWarehouse = warehouse("MWH-001", 100, 80);
        Warehouse newWarehouse = warehouse("MWH-001", 60, 80);

        when(warehouseStore.findByBusinessUnitCode("MWH-001"))
                .thenReturn(oldWarehouse);

        assertThrows(IllegalArgumentException.class,
                () -> useCase.replace(newWarehouse));
    }

    @Test
    void shouldFailWhenStockDoesNotMatch() {
        Warehouse oldWarehouse = warehouse("MWH-001", 100, 50);
        Warehouse newWarehouse = warehouse("MWH-001", 150, 40);

        when(warehouseStore.findByBusinessUnitCode("MWH-001"))
                .thenReturn(oldWarehouse);

        assertThrows(IllegalArgumentException.class,
                () -> useCase.replace(newWarehouse));
    }

    private Warehouse warehouse(String bu, int cap, int stock) {
        Warehouse w = new Warehouse();
        w.businessUnitCode = bu;
        w.capacity = cap;
        w.stock = stock;
        w.createdAt = LocalDateTime.now();
        return w;
    }
}
