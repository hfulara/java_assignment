package com.fulfilment.application.monolith.warehouses.adapters.database;

import static org.junit.jupiter.api.Assertions.*;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

@QuarkusTest
@Transactional
class WarehouseRepositoryTest {

    @Inject
    WarehouseRepository repository;

    @Test
    void shouldCreateAndGetAll() {
        Warehouse warehouse = new Warehouse("MWH-001", "ZWOLLE-001", 100, 50);
        repository.create(warehouse);

        List<Warehouse> all = repository.getAll();

        assertFalse(all.isEmpty());
        assertEquals("MWH.001", all.get(0).businessUnitCode);
    }

    @Test
    void shouldSaveAndGetById() {
        Warehouse warehouse = new Warehouse("MWH-002", "ZWOLLE-002", 200, 75);

        Warehouse saved = repository.save(warehouse);

        Optional<Warehouse> found = repository.findByBusinessUnitCode(warehouse.businessUnitCode);

        assertTrue(found.isPresent());
        assertEquals("MWH-002", found.get().businessUnitCode);
    }

    @Test
    void shouldFindByBusinessUnitCode() {
        Warehouse warehouse = new Warehouse("MWH-003", "AMSTERDAM-001", 300, 150);
        repository.save(warehouse);

        Optional<Warehouse> found = repository.findByBusinessUnitCode("MWH-003");

        assertTrue(found.isPresent());
        assertEquals("AMSTERDAM-001", found.get().location);
    }

    @Test
    void shouldReturnEmptyWhenBusinessUnitNotFound() {
        Optional<Warehouse> found = repository.getById(4L);

        assertTrue(found.isEmpty());
    }



    @Test
    void shouldThrowWhenUpdatingNonExisting() {
        Warehouse warehouse = new Warehouse("MWH-XXX", "ZWOLLE-001", 100, 50);
        warehouse.id = 999L;

        assertThrows(NotFoundException.class,
                () -> repository.update(warehouse));
    }



    @Test
    void shouldThrowWhenRemovingNonExisting() {
        Warehouse warehouse = new Warehouse("MWH-YYY", "ZWOLLE-001", 100, 50);
        warehouse.id = 888L;

        assertThrows(IllegalArgumentException.class,
                () -> repository.remove(warehouse));
    }
}
