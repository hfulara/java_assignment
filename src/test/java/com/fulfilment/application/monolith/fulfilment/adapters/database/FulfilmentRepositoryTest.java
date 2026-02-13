package com.fulfilment.application.monolith.fulfilment.adapters.database;

import com.fulfilment.application.monolith.fulfilment.domain.models.Fulfilment;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import jakarta.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class FulfilmentRepositoryTest {

    @Inject
    FulfilmentRepository repository;

    @Test
    @Transactional
    void shouldPersistFulfilment() {

        Fulfilment fulfilment =
                new Fulfilment(null, 1L, 1L, 1L);

        Fulfilment saved = repository.save(fulfilment);

        assertNotNull(saved.getId());
        assertEquals(1L, saved.getStoreId());
    }

    @Test
    @Transactional
    void shouldReturnAllFulfilments() {
        repository.save(new Fulfilment(null, 1L, 1L, 1L));
        repository.save(new Fulfilment(null, 2L, 2L, 2L));

        List<Fulfilment> result = repository.findAllFulfilments();

        assertNotNull(result);
        assertTrue(result.size() >= 2);
    }
}
