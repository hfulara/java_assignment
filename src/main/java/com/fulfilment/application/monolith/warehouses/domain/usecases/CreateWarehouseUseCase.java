package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.common.BusinessException;
import com.fulfilment.application.monolith.location.LocationGateway;
import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.CreateWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;
import java.util.Optional;

@ApplicationScoped
public class CreateWarehouseUseCase implements CreateWarehouseOperation {

  private final WarehouseStore warehouseStore;
  private LocationGateway locationGateway;

  public CreateWarehouseUseCase(WarehouseStore warehouseStore, LocationGateway locationGateway) {
    this.warehouseStore = warehouseStore;
    this.locationGateway = locationGateway;
  }

  @Override
  public void create(Warehouse warehouse) {
    if (warehouse == null) {
      throw new BusinessException("Warehouse must be provided");
    }
    Optional<Warehouse> existingWarehouse = warehouseStore.findByBusinessUnitCode(warehouse.businessUnitCode);

    if (existingWarehouse.isPresent()) {
      throw new BusinessException("Business Unit already exists");
    }

    Optional<Location> locationOpt =
            locationGateway.resolveByIdentifier(warehouse.location);

    if (locationOpt.isEmpty()) {
      throw new BusinessException("Invalid warehouse location");
    }

    Location location = locationOpt.get();

    if (warehouse.capacity == 0 || warehouse.stock == 0) {
      throw new BusinessException("Capacity and stock must be informed");
    }

    if (warehouse.stock > warehouse.capacity) {
      throw new BusinessException("Stock exceeds capacity");
    }

    if (warehouse.capacity > location.maxCapacity) {
      throw new BusinessException("Warehouse capacity exceeds location limit");
    }

    warehouse.createdAt = LocalDateTime.now();
    warehouse.archivedAt = null;

    warehouseStore.create(warehouse);
  }
}
