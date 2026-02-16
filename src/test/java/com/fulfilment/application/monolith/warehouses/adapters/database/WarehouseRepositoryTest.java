package com.fulfilment.application.monolith.warehouses.adapters.database;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class WarehouseRepositoryTest {

    @Spy
    WarehouseRepository repository;

    @Mock
    EntityManager em;

    @Mock
    DbWarehouse dbWarehouse;

    @BeforeEach
    void setup() {
        repository.em = em; // manual injection
    }

    @Test
    void shouldReturnAllWarehouses() {

        DbWarehouse db1 = new DbWarehouse();
        db1.businessUnitCode = "MWH-001";
        db1.location = "LOC-1";
        db1.capacity = 100;
        db1.stock = 10;

        doReturn(List.of(db1)).when(repository).listAll();

        List<Warehouse> result = repository.getAll();

        assertEquals(1, result.size());
        assertEquals("MWH-001", result.get(0).businessUnitCode);
    }

    @Test
    void shouldCreateWarehouse() {

        Warehouse warehouse =
                new Warehouse("MWH-002", "LOC-2", 200, 50);

        doNothing().when(repository).persistAndFlush(any());

        repository.create(warehouse);

        verify(repository).persistAndFlush(any(DbWarehouse.class));
    }

    @Test
    void shouldUpdateWarehouse() {
        Warehouse warehouse =
                new Warehouse("MWH-003", "LOC-3", 300, 100);
        warehouse.id = 1L;

        DbWarehouse db = new DbWarehouse();
        db.id = 1L;

        doReturn(Optional.of(db))
                .when(repository)
                .findByIdOptional(1L);

        repository.update(warehouse);

        assertEquals("MWH-003", db.businessUnitCode);
        assertEquals("LOC-3", db.location);
    }

    @Test
    void shouldThrowWhenUpdatingNonExisting() {
        Warehouse warehouse =
                new Warehouse("MWH-XXX", "LOC", 100, 10);
        warehouse.id = 999L;

        doReturn(Optional.empty())
                .when(repository)
                .findByIdOptional(999L);

        assertThrows(NotFoundException.class,
                () -> repository.update(warehouse));
    }

    @Test
    void shouldArchiveWarehouse() {
        Warehouse warehouse =
                new Warehouse("MWH-004", "LOC", 100, 10);
        warehouse.id = 1L;

        DbWarehouse db = new DbWarehouse();
        db.id = 1L;

        doReturn(Optional.of(db))
                .when(repository)
                .findByIdOptional(1L);

        repository.remove(warehouse);

        assertNotNull(db.archivedAt);
    }

    @Test
    void shouldFindByBusinessUnitCode() {
        DbWarehouse db = new DbWarehouse();
        db.businessUnitCode = "MWH-005";
        db.location = "LOC";
        db.capacity = 100;
        db.stock = 20;

        var queryMock =
                mock(io.quarkus.hibernate.orm.panache.PanacheQuery.class);

        doReturn(queryMock)
                .when(repository)
                .find("businessUnitCode", "MWH-005");

        when(queryMock.firstResultOptional())
                .thenReturn(Optional.of(db));

        Optional<Warehouse> result =
                repository.findByBusinessUnitCode("MWH-005");

        assertTrue(result.isPresent());
        assertEquals("MWH-005", result.get().businessUnitCode);
    }

    @Test
    void shouldReturnWarehouseById() {
        DbWarehouse db = new DbWarehouse();
        db.id = 1L;
        db.businessUnitCode = "MWH-001";
        db.location = "LOC-1";
        db.capacity = 200;
        db.stock = 50;

        when(em.find(DbWarehouse.class, 1L))
                .thenReturn(db);

        Optional<Warehouse> result = repository.getById(1L);

        assertTrue(result.isPresent());
        assertEquals("MWH-001", result.get().businessUnitCode);
    }

    @Test
    void shouldSaveWarehouse() {

        Warehouse warehouse =
                new Warehouse("MWH-007", "LOC", 100, 10);

        doNothing().when(repository).create(any());

        Warehouse result = repository.save(warehouse);

        assertEquals("MWH-007", result.businessUnitCode);
        verify(repository).create(any());
    }
}
