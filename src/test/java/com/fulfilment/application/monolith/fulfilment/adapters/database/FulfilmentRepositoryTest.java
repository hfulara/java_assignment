package com.fulfilment.application.monolith.fulfilment.adapters.database;

import com.fulfilment.application.monolith.fulfilment.domain.models.Fulfilment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class FulfilmentRepositoryTest {

    @Spy
    FulfilmentRepository repository;

    @Test
    void shouldPersistFulfilment() {

        // Arrange
        Fulfilment fulfilment =
                new Fulfilment(null, 1L, 1L, 1L);

        doNothing().when(repository).persist(any(DbFulfilment.class));

        Fulfilment saved = repository.save(fulfilment);

        assertNotNull(saved);
        assertEquals(1L, saved.getStoreId());

        verify(repository, times(1))
                .persist(any(DbFulfilment.class));
    }

    @Test
    void shouldReturnAllFulfilments() {
        DbFulfilment db1 = DbFulfilment.fromDomain(
                new Fulfilment(1L, 1L, 1L, 1L)
        );

        DbFulfilment db2 = DbFulfilment.fromDomain(
                new Fulfilment(2L, 2L, 2L, 2L)
        );

        doReturn(List.of(db1, db2))
                .when(repository)
                .listAll();

        List<Fulfilment> result = repository.findAllFulfilments();

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(1L, result.get(0).getStoreId());
        assertEquals(2L, result.get(1).getStoreId());

        verify(repository, times(1)).listAll();
    }

    @Test
    void shouldCountWarehousesByStore() {
        doReturn(5L)
                .when(repository)
                .count("storeId", 10L);

        int count = repository.countWarehousesByStore(10L);
        assertEquals(5, count);
    }

    @Test
    void shouldCountWarehousesByStoreAndProduct() {
        doReturn(3L)
                .when(repository)
                .count("storeId = ?1 and productId = ?2",
                        10L, 20L);
        int count = repository.countWarehousesByStoreAndProduct(10L, 20L);

        assertEquals(3, count);
    }

    @Test
    void shouldCountProductsByWarehouse() {
        doReturn(7L)
                .when(repository)
                .count("warehouseId", 99L);

        int count = repository.countProductsByWarehouse(99L);
        assertEquals(7, count);
    }
}
