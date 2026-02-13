package com.fulfilment.application.monolith.warehouses.adapters.database;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "warehouse")
@Cacheable
@SequenceGenerator(
        name = "warehouse_seq_gen",
        sequenceName = "warehouse_seq",
        allocationSize = 1
)
public class DbWarehouse {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "warehouse_seq_gen")
  public Long id;

  public String businessUnitCode;

  public String location;

  public Integer capacity;

  public Integer stock;

  public LocalDateTime createdAt;

  public LocalDateTime archivedAt;

  public DbWarehouse() {}

  public Warehouse toDomain() {
    Warehouse warehouse = new Warehouse(
            this.businessUnitCode,
            this.location,
            this.capacity,
            this.stock
    );

    warehouse.id = this.id;
    warehouse.createdAt = this.createdAt;
    warehouse.archivedAt = this.archivedAt;

    return warehouse;
  }

  public static DbWarehouse fromDomain(Warehouse warehouse) {

    DbWarehouse entity = new DbWarehouse();

    if (warehouse.id != null) {
      entity.id = warehouse.id;
    }
    entity.businessUnitCode = warehouse.businessUnitCode;
    entity.location = warehouse.location;
    entity.capacity = warehouse.capacity;
    entity.stock = warehouse.stock;
    entity.createdAt = warehouse.createdAt;
    entity.archivedAt = warehouse.archivedAt;

    return entity;
  }

}
