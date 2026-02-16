package com.fulfilment.application.monolith.stores;

import com.fulfilment.application.monolith.common.BusinessException;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class StoreResourceTest {

    @InjectMocks
    StoreResource resource;


    Store store;

    @BeforeEach
    void setup() {
        store = new Store();
        store.id = 1L;
        store.name = "Store A";
        store.quantityProductsInStock = 10;
    }

    @Test
    void shouldThrow422IfIdSetOnCreate() {
        store.id = 99L;

        assertThrows(BusinessException.class,
                () -> resource.create(store));
    }

    @Test
    void shouldThrow422IfNameMissingOnUpdate() {

        Store updated = new Store();
        updated.name = null;

        assertThrows(BusinessException.class,
                () -> resource.update(1L, updated));
    }

}
