package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import com.fulfilment.application.monolith.warehouses.domain.usecases.ArchiveWarehouseUseCase;
import com.fulfilment.application.monolith.warehouses.domain.usecases.CreateWarehouseUseCase;
import com.fulfilment.application.monolith.warehouses.domain.usecases.ReplaceWarehouseUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import jakarta.ws.rs.WebApplicationException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class WarehouseResourceImplTest {

    @Mock
    CreateWarehouseUseCase createUseCase;

    @Mock
    ReplaceWarehouseUseCase replaceUseCase;

    @Mock
    ArchiveWarehouseUseCase archiveUseCase;

    @Mock
    WarehouseStore warehouseStore;

    @InjectMocks
    WarehouseResourceImpl resource;

    com.fulfilment.application.monolith.warehouses.domain.models.Warehouse domainWarehouse;

    @BeforeEach
    void setup() {
        domainWarehouse =
                new com.fulfilment.application.monolith.warehouses.domain.models.Warehouse(
                        "MWH-001",
                        "ZWOLLE-001",
                        100,
                        10
                );
    }

    @Test
    void shouldListAllWarehouses() {

        when(warehouseStore.getAll())
                .thenReturn(List.of(domainWarehouse));

        List<com.warehouse.api.beans.Warehouse> result =
                resource.listAllWarehousesUnits();

        assertEquals(1, result.size());
        assertEquals("MWH-001", result.get(0).getBusinessUnitCode());
    }

    @Test
    void shouldCreateNewWarehouse() {
        com.warehouse.api.beans.Warehouse request =
                new com.warehouse.api.beans.Warehouse();

        request.setBusinessUnitCode("MWH-002");
        request.setLocation("AMSTERDAM-001");
        request.setCapacity(200);
        request.setStock(50);

        com.warehouse.api.beans.Warehouse response =
                resource.createANewWarehouseUnit(request);

        verify(createUseCase).create(any());

        assertEquals("MWH-002", response.getBusinessUnitCode());
        assertEquals(200, response.getCapacity());
        assertEquals(50, response.getStock());
    }

    @Test
    void shouldGetWarehouseByBusinessUnitCode() {
        when(warehouseStore.findByBusinessUnitCode("MWH-001"))
                .thenReturn(Optional.of(domainWarehouse));

        com.warehouse.api.beans.Warehouse response =
                resource.getAWarehouseUnitByID("MWH-001");

        assertEquals("MWH-001", response.getBusinessUnitCode());
    }

    @Test
    void shouldThrow404IfWarehouseNotFound() {
        when(warehouseStore.findByBusinessUnitCode("UNKNOWN"))
                .thenReturn(Optional.empty());

        assertThrows(WebApplicationException.class,
                () -> resource.getAWarehouseUnitByID("UNKNOWN"));
    }

    @Test
    void shouldArchiveWarehouse() {
        when(warehouseStore.findByBusinessUnitCode("MWH-001"))
                .thenReturn(Optional.of(domainWarehouse));

        resource.archiveAWarehouseUnitByID("MWH-001");

        verify(archiveUseCase).archive(domainWarehouse);
        assertNotNull(domainWarehouse.archivedAt);
    }

    @Test
    void shouldThrow404WhenArchivingUnknownWarehouse() {
        when(warehouseStore.findByBusinessUnitCode("UNKNOWN"))
                .thenReturn(Optional.empty());

        assertThrows(WebApplicationException.class,
                () -> resource.archiveAWarehouseUnitByID("UNKNOWN"));
    }

    @Test
    void shouldReplaceWarehouse() {
        when(warehouseStore.findByBusinessUnitCode("MWH-001"))
                .thenReturn(Optional.of(domainWarehouse));

        com.warehouse.api.beans.Warehouse request =
                new com.warehouse.api.beans.Warehouse();

        request.setBusinessUnitCode("MWH-NEW");
        request.setLocation("TILBURG-001");
        request.setCapacity(300);
        request.setStock(20);

        com.warehouse.api.beans.Warehouse response =
                resource.replaceTheCurrentActiveWarehouse("MWH-001", request);

        verify(replaceUseCase).replace(domainWarehouse);

        assertEquals("MWH-NEW", response.getBusinessUnitCode());
        assertEquals("TILBURG-001", response.getLocation());
    }

    @Test
    void shouldThrow404WhenReplacingUnknownWarehouse() {
        when(warehouseStore.findByBusinessUnitCode("UNKNOWN"))
                .thenReturn(Optional.empty());

        com.warehouse.api.beans.Warehouse request =
                new com.warehouse.api.beans.Warehouse();

        assertThrows(WebApplicationException.class,
                () -> resource.replaceTheCurrentActiveWarehouse("UNKNOWN", request));
    }
}
