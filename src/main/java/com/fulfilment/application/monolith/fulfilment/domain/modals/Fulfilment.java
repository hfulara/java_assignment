package com.fulfilment.application.monolith.fulfilment.domain.models;

public class Fulfilment {

    private Long id;
    private Long storeId;
    private Long productId;
    private Long warehouseId;

    public Fulfilment(Long id,
                      Long storeId,
                      Long productId,
                      Long warehouseId) {

        this.id = id;
        this.storeId = storeId;
        this.productId = productId;
        this.warehouseId = warehouseId;
    }

    public Long getId() { return id; }
    public Long getStoreId() { return storeId; }
    public Long getProductId() { return productId; }
    public Long getWarehouseId() { return warehouseId; }
}
