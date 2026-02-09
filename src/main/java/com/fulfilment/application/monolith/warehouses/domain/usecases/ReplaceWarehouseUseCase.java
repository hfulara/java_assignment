package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.ReplaceWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ReplaceWarehouseUseCase implements ReplaceWarehouseOperation {

  private final WarehouseStore warehouseStore;

  public ReplaceWarehouseUseCase(WarehouseStore warehouseStore) {
    this.warehouseStore = warehouseStore;
  }

  @Override
  public void replace(Warehouse newWarehouse) {
    Warehouse existingWarehouse = warehouseStore.findByBusinessUnitCode(newWarehouse.businessUnitCode);

    if(existingWarehouse == null)
      throw new IllegalArgumentException("Warehouse not found");

    if (!existingWarehouse.stock.equals(newWarehouse.stock)) {
      throw new IllegalArgumentException("Stock must match existing warehouse");
    }

    if (newWarehouse.capacity < existingWarehouse.stock) {
      throw new IllegalArgumentException("Capacity cannot hold existing stock");
    }

    existingWarehouse.archivedAt = java.time.LocalDateTime.now();
    warehouseStore.update(existingWarehouse);

    newWarehouse.createdAt = java.time.LocalDateTime.now();
    newWarehouse.archivedAt = null;
    warehouseStore.create(newWarehouse);
  }
}
