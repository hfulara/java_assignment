package com.fulfilment.application.monolith.fulfilment.adapters.database;

import com.fulfilment.application.monolith.fulfilment.domain.models.Fulfilment;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "fulfilment")
public class DbFulfilment extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public Long storeId;
    public Long productId;
    public Long warehouseId;

    public Fulfilment toDomain() {
        return new Fulfilment(id, storeId, productId, warehouseId);
    }

    public static DbFulfilment fromDomain(Fulfilment fulfilment) {
        DbFulfilment db = new DbFulfilment();
        db.id = fulfilment.getId();
        db.storeId = fulfilment.getStoreId();
        db.productId = fulfilment.getProductId();
        db.warehouseId = fulfilment.getWarehouseId();
        return db;
    }
}
