package com.fulfilment.application.monolith.warehouses.adapters.database;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class WarehouseRepository implements WarehouseStore, PanacheRepository<DbWarehouse> {

  @Inject
  EntityManager em;

  @Override
  public List<Warehouse> getAll() {
    return this.listAll().stream().map(DbWarehouse::toDomain).toList();
  }

  @Override
  public void create(Warehouse warehouse) {
    DbWarehouse db = DbWarehouse.fromDomain(warehouse);
    db.createdAt = LocalDateTime.now();
    db.archivedAt = null;

    persistAndFlush(db);
  }

  @Override
  public void update(Warehouse warehouse) {
    DbWarehouse db = findByIdOptional(warehouse.id)
            .orElseThrow(() -> new NotFoundException("Warehouse not found"));

    db.businessUnitCode = warehouse.businessUnitCode;
    db.location = warehouse.location;
    db.capacity = warehouse.capacity;
    db.stock = warehouse.stock;
    db.archivedAt = warehouse.archivedAt;
  }

  @Override
  public void remove(Warehouse warehouse) {
    DbWarehouse db = findByIdOptional(warehouse.id)
            .orElseThrow(() -> new IllegalArgumentException("Warehouse not found"));

    db.archivedAt = LocalDateTime.now();
  }

  @Override
  public Optional<Warehouse> findByBusinessUnitCode(String buCode) {
    return find("businessUnitCode", buCode)
            .firstResultOptional()
            .map(DbWarehouse::toDomain);
  }

  @Override
  public Optional<Warehouse> getById(Long id) {
    DbWarehouse entity = em.find(DbWarehouse.class, id);
    if (entity == null) {
      return Optional.empty();
    }
    return Optional.of(entity.toDomain());
  }

  @Override
  public Warehouse save(Warehouse warehouse) {
    DbWarehouse entity = DbWarehouse.fromDomain(warehouse);
    create(warehouse);

    return entity.toDomain();
  }

}
