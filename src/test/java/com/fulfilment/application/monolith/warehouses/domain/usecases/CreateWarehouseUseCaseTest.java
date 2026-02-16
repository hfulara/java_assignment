package com.fulfilment.application.monolith.warehouses.domain.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fulfilment.application.monolith.common.BusinessException;
import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.location.LocationGateway;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
        useCase = new CreateWarehouseUseCase(warehouseStore, locationGateway);
    }

    @Test
    void shouldThrowIfWarehouseIsNull() {
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> useCase.create(null));

        assertEquals("Warehouse must be provided", exception.getMessage());
    }

    @Test
    void shouldCreateWarehouseSuccessfully() {
        Warehouse warehouse =  new Warehouse("MWH-002", "ZWOLLE-001", 100, 50);

        when(warehouseStore.findByBusinessUnitCode(warehouse.businessUnitCode)).thenReturn(Optional.empty());
        when(locationGateway.resolveByIdentifier(warehouse.location))
                .thenReturn(Optional.of(new Location("ZWOLLE-001", 2, 200)));

        assertDoesNotThrow(() -> useCase.create(warehouse));
        verify(warehouseStore).create(warehouse);
    }

    @Test
    void shouldFailWhenBusinessUnitCodeAlreadyExists() {
        Warehouse warehouse = new Warehouse("MWH-001", "ZWOLLE-001", 100, 50);

        when(warehouseStore.findByBusinessUnitCode("MWH-001"))
                .thenReturn(Optional.of(warehouse));

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> useCase.create(warehouse)
        );

        assertEquals("Business Unit already exists", exception.getMessage());
    }

    @Test
    void shouldFailWhenLocationIsInvalid() {
        Warehouse warehouse = new Warehouse("MWH-001", "ZWOLLE-001", 100, 50);

        when(warehouseStore.findByBusinessUnitCode("MWH-003")).thenReturn(Optional.empty());
        when(locationGateway.resolveByIdentifier("INVALID")).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> useCase.create(warehouse));

        assertEquals("Invalid warehouse location", exception.getMessage());
    }

    @Test
    void shouldThrowIfCapacityOrStockZero() {
        Warehouse warehouse = new Warehouse("MWH-002", "ZWOLLE-002", 0, 0);
        Location location = new Location("ZWOLLE-002", 2,50);

        when(warehouseStore.findByBusinessUnitCode("MWH-003"))
                .thenReturn(Optional.of(warehouse));
        when(locationGateway.resolveByIdentifier("ZWOLLE-002"))
                .thenReturn(Optional.of(location));

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> useCase.create(warehouse));

        assertEquals("Capacity and stock must be informed", exception.getMessage());
    }

    @Test
    void shouldThrowIfCapacityExceedsLocationLimit() {
        Warehouse warehouse = new Warehouse("MWH-004", "ZWOLLE-002", 100, 10);
        Location location = new Location("ZWOLLE-002", 2,50);

        when(warehouseStore.findByBusinessUnitCode("MWH-001"))
                .thenReturn(Optional.of(warehouse));

        when(locationGateway.resolveByIdentifier("ZWOLLE-002"))
                .thenReturn(Optional.of(location));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> useCase.create(warehouse));

        assertEquals("Warehouse capacity exceeds location limit", exception.getMessage());
    }

    @Test
    void shouldThrowIfStockExceedsCapacity() {
        Warehouse warehouse = new Warehouse("MWH-005", "ZWOLLE-002", 100, 150);
        Location location = new Location("ZWOLLE-002", 2,50);

        when(locationGateway.resolveByIdentifier("ZWOLLE-002"))
                .thenReturn(Optional.of(location));

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> useCase.create(warehouse));

        assertEquals("Stock exceeds capacity", exception.getMessage());
    }

}
