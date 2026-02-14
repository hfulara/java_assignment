package com.fulfilment.application.monolith.warehouses.domain.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import java.util.Optional;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@QuarkusTest
class ReplaceWarehouseUseCaseTest {

    @Mock
    WarehouseStore warehouseStore;

    ReplaceWarehouseUseCase useCase;

    Warehouse existing;
    Warehouse replacement;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        useCase = new ReplaceWarehouseUseCase(warehouseStore);

        existing = new Warehouse("MWH-001", "ZWOLLE-001", 100, 50);
        replacement = new Warehouse("MWH-001", "ZWOLLE-001",50,150);
    }

    @Test
    void shouldReplaceWarehouseSuccessfully() {
        replacement.stock = 50;
        when(warehouseStore.findByBusinessUnitCode("MWH-001"))
                .thenReturn(Optional.of(existing));

        useCase.replace(replacement);
        verify(warehouseStore).update(existing);
    }

    @Test
    void shouldThrowIfWarehouseNotFound() {
        when(warehouseStore.findByBusinessUnitCode("MWH-001"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> useCase.replace(replacement));

        assertEquals("Warehouse not found", exception.getMessage());
    }

    @Test
    void shouldThrowIfStockMismatch() {
        replacement.stock = 60;

        when(warehouseStore.findByBusinessUnitCode("MWH-001"))
                .thenReturn(Optional.of(existing));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> useCase.replace(replacement));

        assertEquals("Stock must match existing warehouse", exception.getMessage());
    }

    @Test
    void shouldThrowIfCapacityCannotHoldStock() {
        replacement.stock = 50;
        replacement.capacity = 5;
        when(warehouseStore.findByBusinessUnitCode("MWH-001"))
                .thenReturn(Optional.of(existing));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> useCase.replace(replacement));

        assertEquals("Capacity cannot hold existing stock", exception.getMessage());
    }

}
