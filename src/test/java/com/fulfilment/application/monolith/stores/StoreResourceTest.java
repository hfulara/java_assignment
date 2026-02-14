package com.fulfilment.application.monolith.stores;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class StoreResourceTest {

    @InjectMock
    LegacyStoreManagerGateway legacyStoreManagerGateway;

    @Test
    void shouldCreateStoreAndCallLegacyOnCommit() {
        given()
                .contentType("application/json")
                .body("""
                {
                  "name": "Store A",
                  "quantityProductsInStock": 10
                }
            """)
                .when()
                .post("/store")
                .then()
                .statusCode(201);

        verify(legacyStoreManagerGateway)
                .createStoreOnLegacySystem(ArgumentMatchers.any(Store.class));
    }

    @Test
    void shouldReturn400IfIdSetOnCreate() {

        given()
                .contentType("application/json")
                .body("""
                    {
                      "id": 1,
                      "name": "Invalid Store",
                      "quantityProductsInStock": 5
                    }
                """)
                .when()
                .post("/store")
                .then()
                .statusCode(400);
    }

    @Test
    @Transactional
    void shouldReturnAllStores() {
        Store store = new Store();
        store.name = "Store X";
        store.quantityProductsInStock = 5;
        store.persist();

        given()
                .when()
                .get("/store")
                .then()
                .statusCode(200)
                .body("$.size()", greaterThanOrEqualTo(1));
    }

    @Test
    void shouldReturn404IfStoreNotFound() {

        given()
                .when()
                .get("/store/999")
                .then()
                .statusCode(404);
    }

    @Test
    @Transactional
    void shouldReturn400IfNameMissingOnUpdate() {

        Store store = new Store();
        store.name = "Store";
        store.persist();

        given()
                .contentType("application/json")
                .body("""
                    {
                      "quantityProductsInStock": 20
                    }
                """)
                .when()
                .put("/store/" + store.id)
                .then()
                .statusCode(400);
    }

    @Test
    @Transactional
    void shouldDeleteStore() {
        int id = 3;
        given()
                .when()
                .delete("/store/" + id)
                .then()
                .statusCode(204);
    }
}
