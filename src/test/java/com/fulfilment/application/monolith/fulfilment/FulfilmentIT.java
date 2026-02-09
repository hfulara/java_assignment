package com.fulfilment.application.monolith.fulfilment;

import static io.restassured.RestAssured.given;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class FulfilmentIT {
    @Test
    void shouldAssignFulfilmentSuccessfully() {
        given()
                .contentType("application/json")
                .body("""
            {
              "storeId": 1,
              "productId": 1,
              "warehouseBusinessUnitCode": "MWH.001"
            }
            """)
                .when()
                .post("/fulfilment")
                .then()
                .statusCode(201);
    }

    @Test
    void shouldFailWhenProductAssignedToMoreThanTwoWarehouses() {
        given().contentType("application/json")
                .body("""
          { "storeId": 1, "productId": 1, "warehouseBusinessUnitCode": "MWH.001" }
          """)
                .post("/fulfilment")
                .then().statusCode(201);

        given().contentType("application/json")
                .body("""
          { "storeId": 1, "productId": 1, "warehouseBusinessUnitCode": "MWH.012" }
          """)
                .post("/fulfilment")
                .then().statusCode(201);


        given().contentType("application/json")
                .body("""
          { "storeId": 1, "productId": 1, "warehouseBusinessUnitCode": "MWH.023" }
          """)
                .post("/fulfilment")
                .then().statusCode(400);
    }

}
