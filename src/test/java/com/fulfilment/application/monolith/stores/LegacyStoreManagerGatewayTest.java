package com.fulfilment.application.monolith.stores;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LegacyStoreManagerGatewayTest {

    LegacyStoreManagerGateway gateway =
            new LegacyStoreManagerGateway();

    @Test
    void shouldCreateStoreAndWriteTempFileSuccessfully() {

        Store store = new Store();
        store.name = "TestStore";
        store.quantityProductsInStock = 10;

        assertDoesNotThrow(() ->
                gateway.createStoreOnLegacySystem(store)
        );
    }

    @Test
    void shouldUpdateStoreAndWriteTempFileSuccessfully() {

        Store store = new Store();
        store.name = "UpdateStore";
        store.quantityProductsInStock = 5;

        assertDoesNotThrow(() ->
                gateway.updateStoreOnLegacySystem(store)
        );
    }

    @Test
    void shouldHandleExceptionGracefully() {

        Store store = new Store();
        store.name = null; // this may cause createTempFile to throw exception
        store.quantityProductsInStock = 1;

        assertDoesNotThrow(() ->
                gateway.createStoreOnLegacySystem(store)
        );
    }
}
