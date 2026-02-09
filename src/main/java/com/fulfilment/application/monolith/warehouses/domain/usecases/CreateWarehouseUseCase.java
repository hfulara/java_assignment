package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.CreateWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CreateWarehouseUseCase implements CreateWarehouseOperation {

  private final WarehouseStore warehouseStore;

  public CreateWarehouseUseCase(WarehouseStore warehouseStore) {
    this.warehouseStore = warehouseStore;
  }

  @Override
  public void create(Warehouse warehouse) {
    Warehouse newWarehouse = warehouseStore.findByBusinessUnitCode(warehouse.businessUnitCode);
    if(newWarehouse != null)
      throw new IllegalArgumentException("Business Unit already exist");

    if (newWarehouse.capacity == null || newWarehouse.stock == null) {
      throw new IllegalArgumentException("Capacity and stock must be informed");
    }

    if (newWarehouse.stock > newWarehouse.capacity) {
      throw new IllegalArgumentException("Stock exceeds capacity");
    }

    newWarehouse.createdAt = java.time.LocalDateTime.now();
    newWarehouse.archivedAt = null;
    // if all went well, create the warehouse
    warehouseStore.create(newWarehouse);
  }
}
