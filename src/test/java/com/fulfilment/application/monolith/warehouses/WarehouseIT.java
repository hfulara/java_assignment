package com.fulfilment.application.monolith.warehouses;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class WarehouseIT {
    @Test
    void shouldCreateAndArchiveWarehouse() {
        given()
                .contentType("application/json")
                .body("""
                        {
              "businessUnitCode": "MWH-999",
              "location": "ZWOLLE-001",
              "capacity": 100,
              "stock": 50
            }
                        """)
                .when()
                .post("warehouse")
                .then()
                .statusCode(200)
                .body(containsString("MWH-999"));

        given()
                .when()
                .get("warehouse")
                .then()
                .statusCode(200)
                .body(containsString("MWH-999"));

        given()
                .when()
                .delete("warehouse/MWH-999")
                .then()
                .statusCode(204);
    }
}
