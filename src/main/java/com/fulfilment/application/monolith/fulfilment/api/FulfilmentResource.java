package com.fulfilment.application.monolith.fulfilment.api;

import com.fulfilment.application.monolith.fulfilment.AssignFulfilmentUseCase;
import com.fulfilment.application.monolith.products.Product;
import com.fulfilment.application.monolith.products.ProductRepository;
import com.fulfilment.application.monolith.stores.Store;
import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

public class FulfilmentResource {
    @Inject
    AssignFulfilmentUseCase assignFulfilmentUseCase;

    @Inject
    WarehouseRepository warehouseRepository;

    @Inject
    ProductRepository productRepository;


    @POST
    @Transactional
    public Response assign(FulfilmentRequest request) {
        Store store = Store.findById(request.storeId);
        Product product = productRepository.findById(request.productId);
        Warehouse warehouse = warehouseRepository.findByBusinessUnitCode(request.warehouseBusinessUnitCode);
        if (warehouse == null) {
            throw new WebApplicationException("Warehouse not found", 404);
        }

        assignFulfilmentUseCase.assign(store, product, warehouse);
        return Response.status(201).build();
    }
}
