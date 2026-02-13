package com.fulfilment.application.monolith.fulfilment.adapters.database.restapi;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

@QuarkusTest
public class FulfilmentIT {
    @Test
    void shouldAssignFulfilmentSuccessfully() {
        String requestBody = """
            {
              "storeId": 1,
              "productId": 1,
              "warehouseId": 1
            }
        """;

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/fulfilment")
                .then()
                .statusCode(201);
    }

    @Test
    void shouldFailWhenProductAssignedToMoreThanTwoWarehouses() {
        given().contentType("application/json")
                .body("""
          { "storeId": 1, "productId": 1, "warehouseId": 1 }
          """)
                .post("/fulfilment")
                .then().statusCode(201);

        given().contentType("application/json")
                .body("""
          { "storeId": 1, "productId": 1, "warehouseId": 2 }
          """)
                .post("/fulfilment")
                .then().statusCode(400)
                .body(containsString("Max 2 warehouses per product per store"));


        given().contentType("application/json")
                .body("""
          { "storeId": 1, "productId": 1, "warehouseId": "3" }
          """)
                .post("/fulfilment")
                .then().statusCode(400);
    }
}
