package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class WarehouseResourceIT {
    @Test
    void shouldCreateAndArchiveWarehouse() {
        given()
                .contentType("application/json")
                .body("""
                        {
              "businessUnitCode": "MWH-999",
              "location": "ZWOLLE-001",
              "capacity": 30,
              "stock": 20
            }
            """)
                .when()
                .post("/warehouse")
                .then()

                .body(containsString("MWH-999"));

        given()
                .when()
                .get("/warehouse")
                .then()
                .statusCode(200)
                .body(containsString("MWH-999"));

        given()
                .when()
                .delete("/warehouse/MWH-999")
                .then()
                .statusCode(204);
    }

    @Test
    public void testSimpleListWarehouses() {

        final String path = "/warehouse";

        // List all, should have all 3 products the database has initially:
        given()
                .when()
                .get(path)
                .then()
                .statusCode(200)
                .body(containsString("MWH.001"), containsString("MWH.012"), containsString("MWH.023"));
    }

    @Test
    public void testSimpleCheckingArchivingWarehouses() {
        final String path = "/warehouse";
        // List all, should have all 3 products the database has initially:
        given()
                .when()
                .get(path)
                .then()
                .statusCode(200)
                .body(
                        containsString("MWH.001"),
                        containsString("MWH.012"),
                        containsString("MWH.023"),
                        containsString("ZWOLLE-001"),
                        containsString("AMSTERDAM-001"),
                        containsString("TILBURG-001"));

        // Archive the ZWOLLE-001:
        given().when().delete(path + "/MWH.001").then().statusCode(204);

    }
}
