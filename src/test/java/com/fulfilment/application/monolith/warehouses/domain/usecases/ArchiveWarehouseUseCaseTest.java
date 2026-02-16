package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        Warehouse existing = new Warehouse("MWH-001", "ZWOLLE-001", 100, 50);
        when(warehouseStore.findByBusinessUnitCode("MWH-001"))
                .thenReturn(Optional.of(existing));

        useCase.archive(existing);

        assertNotNull(existing.archivedAt);

        verify(warehouseStore).update(existing);
    }


}
