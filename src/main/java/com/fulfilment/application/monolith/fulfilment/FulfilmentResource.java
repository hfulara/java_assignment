package com.fulfilment.application.monolith.fulfilment;

import com.fulfilment.application.monolith.products.Product;
import com.fulfilment.application.monolith.products.ProductRepository;
import com.fulfilment.application.monolith.stores.Store;
import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("fulfilment")
@ApplicationScoped
@Consumes("application/json")
@Produces("application/json")
public class FulfilmentResource {
    @Inject
    AssignFulfilmentUseCase assignFulfilmentUseCase;

    @Inject
    WarehouseRepository warehouseRepository;

    @Inject
    ProductRepository productRepository;

    @POST
    @Transactional
    public Response create(FulfilmentRequest request) {
        Store store = Store.findById(request.storeId);
        if (store == null) {
            throw new WebApplicationException("Store not found", 404);
        }

        Product product = productRepository.findById(request.productId);
        if (product == null) {
            throw new WebApplicationException("Product not found", 404);
        }

        Warehouse warehouse =
                warehouseRepository.findByBusinessUnitCode(
                        request.warehouseBusinessUnitCode);

        if (warehouse == null) {
            throw new WebApplicationException("Warehouse not found", 404);
        }

        // assignFulfilmentUseCase.assign(store, product, warehouse);

        return Response.status(201).build();
    }
}
