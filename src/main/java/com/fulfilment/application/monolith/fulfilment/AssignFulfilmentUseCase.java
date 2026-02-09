package com.fulfilment.application.monolith.fulfilment;

import com.fulfilment.application.monolith.products.Product;
import com.fulfilment.application.monolith.stores.Store;
import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;
import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AssignFulfilmentUseCase {

    @Inject
    FulfilmentRepository repository;
    @Inject
    WarehouseRepository warehouseRepository;

    @Transactional
    public void assign(Store store, Product product, Warehouse warehouse) {
        if (repository.exists(store, product, warehouse)) {
            return;
        }
        if (repository.countWarehousesForProductInStore(product, store) >= 2) {
            throw new IllegalArgumentException(
                    "Product can be fulfilled by max 2 warehouses per store");
        }

        if (repository.countWarehousesForStore(store) >= 3) {
            throw new IllegalArgumentException(
                    "Store can be fulfilled by max 3 warehouses");
        }

        if (repository.countProductsInWarehouse(warehouse) >= 5) {
            throw new IllegalArgumentException(
                    "Warehouse can store max 5 product types");
        }
        DbWarehouse dbWarehouse = warehouseRepository.findDbByBusinessUnitCode(
                        warehouse.businessUnitCode);


        FulfilmentAssignment assignment = new FulfilmentAssignment();
        assignment.store = store;
        assignment.product = product;
        assignment.warehouse = dbWarehouse;

        repository.persist(assignment);
    }
}
