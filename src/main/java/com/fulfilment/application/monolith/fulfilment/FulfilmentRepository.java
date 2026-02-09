package com.fulfilment.application.monolith.fulfilment;

import com.fulfilment.application.monolith.products.Product;
import com.fulfilment.application.monolith.stores.Store;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FulfilmentRepository {
    public long countWarehousesForProductInStore(Product product, Store store) {
        return FulfilmentAssignment.count(
                "product = ?1 and store = ?2", product, store);
    }

    public long countWarehousesForStore(Store store) {
        return FulfilmentAssignment.count("store = ?1", store);
    }

    public long countProductsInWarehouse(Warehouse warehouse) {
        return FulfilmentAssignment.count("warehouse = ?1", warehouse);
    }

    public boolean exists(Store store, Product product, Warehouse warehouse) {
        return FulfilmentAssignment.count(
                "store = ?1 and product = ?2 and warehouse = ?3",
                store, product, warehouse) > 0;
    }

    public void persist(FulfilmentAssignment assignment) {
        assignment.persist();
    }
}
