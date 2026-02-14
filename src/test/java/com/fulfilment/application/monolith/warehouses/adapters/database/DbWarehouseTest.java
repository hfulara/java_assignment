package com.fulfilment.application.monolith.warehouses.adapters.database;

import static org.junit.jupiter.api.Assertions.*;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

@QuarkusTest
class DbWarehouseTest {

    @Test
    void shouldConvertToDomainCorrectly() {
        DbWarehouse entity = new DbWarehouse();
        entity.id = 1L;
        entity.businessUnitCode = "MWH-001";
        entity.location = "ZWOLLE-001";
        entity.capacity = 100;
        entity.stock = 50;
        entity.createdAt = LocalDateTime.now();
        entity.archivedAt = null;

        Warehouse domain = entity.toDomain();

        assertNotNull(domain);
        assertEquals("MWH-001", domain.businessUnitCode);
        assertEquals("ZWOLLE-001", domain.location);
        assertEquals(100, domain.capacity);
        assertEquals(50, domain.stock);
    }

    @Test
    void shouldConvertFromDomainCorrectly() {
        Warehouse warehouse = new Warehouse(
                "MWH-001",
                "ZWOLLE-001",
                100,
                50
        );

        warehouse.id = 1L;
        warehouse.createdAt = LocalDateTime.now();
        warehouse.archivedAt = null;

        DbWarehouse entity = DbWarehouse.fromDomain(warehouse);

        assertNotNull(entity);
        assertEquals(1L, entity.id);
        assertEquals("MWH-001", entity.businessUnitCode);
        assertEquals("ZWOLLE-001", entity.location);
        assertEquals(100, entity.capacity);
        assertEquals(50, entity.stock);
        assertEquals(warehouse.createdAt, entity.createdAt);
        assertNull(entity.archivedAt);
    }

    @Test
    void shouldCreateEmptyEntityWithNoArgsConstructor() {
        DbWarehouse entity = new DbWarehouse();

        assertNotNull(entity);
        assertNull(entity.id);
        assertNull(entity.businessUnitCode);
        assertNull(entity.location);
        assertNull(entity.capacity);
        assertNull(entity.stock);
        assertNull(entity.createdAt);
        assertNull(entity.archivedAt);
    }
}
