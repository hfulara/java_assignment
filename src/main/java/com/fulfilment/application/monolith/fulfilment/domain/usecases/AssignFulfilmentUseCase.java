package com.fulfilment.application.monolith.fulfilment.domain.usecases;

import com.fulfilment.application.monolith.fulfilment.domain.models.Fulfilment;
import com.fulfilment.application.monolith.fulfilment.domain.ports.FulfilmentStore;
import com.fulfilment.application.monolith.fulfilment.domain.usecases.commands.AssignFulfilmentCommand;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AssignFulfilmentUseCase {

    private final FulfilmentStore fulfilmentStore;

    public AssignFulfilmentUseCase(FulfilmentStore fulfilmentStore) {
        this.fulfilmentStore = fulfilmentStore;
    }

    @Transactional
    public Fulfilment execute(AssignFulfilmentCommand cmd) {
        if (fulfilmentStore.countWarehousesByStoreAndProduct(
                cmd.storeId(), cmd.productId()) >= 2) {
            throw new IllegalArgumentException(
                    "Max 2 warehouses per product per store"
            );
        }

        if (fulfilmentStore.countWarehousesByStore(
                cmd.storeId()) >= 3) {
            throw new IllegalArgumentException(
                    "Max 3 warehouses per store"
            );
        }

        if (fulfilmentStore.countProductsByWarehouse(
                cmd.warehouseId()) >= 5) {
            throw new IllegalArgumentException(
                    "Warehouse can store max 5 products"
            );
        }

        Fulfilment fulfilment = new Fulfilment(
                null,
                cmd.storeId(),
                cmd.productId(),
                cmd.warehouseId()
        );

        return fulfilmentStore.save(fulfilment);
    }
}
