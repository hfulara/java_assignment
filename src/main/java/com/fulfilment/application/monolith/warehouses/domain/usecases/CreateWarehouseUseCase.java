package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.location.LocationGateway;
import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.CreateWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

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
      throw new IllegalArgumentException("Warehouse must be provided");
    }
    Optional<Warehouse> existingWarehouse = warehouseStore.findByBusinessUnitCode(warehouse.businessUnitCode);
    if (! existingWarehouse.isEmpty()) {
      throw new IllegalArgumentException("Business Unit already exists");
    }

    Optional<Location> locationOpt =
            locationGateway.resolveByIdentifier(warehouse.location);

    if (locationOpt.isEmpty()) {
      throw new IllegalArgumentException("Invalid warehouse location");
    }

    Location location = locationOpt.get();

    if (warehouse.capacity == 0 || warehouse.stock == 0) {
      throw new IllegalArgumentException("Capacity and stock must be informed");
    }

    if (warehouse.stock > warehouse.capacity) {
      throw new IllegalArgumentException("Stock exceeds capacity");
    }

    if (warehouse.capacity > location.maxCapacity) {
      throw new IllegalArgumentException("Warehouse capacity exceeds location limit");
    }

    warehouse.createdAt = LocalDateTime.now();
    warehouse.archivedAt = null;

    warehouseStore.create(warehouse);
  }
}
