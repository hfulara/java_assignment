package com.fulfilment.application.monolith.fulfilment.adapters.database;

import com.fulfilment.application.monolith.fulfilment.domain.models.Fulfilment;
import com.fulfilment.application.monolith.fulfilment.domain.ports.FulfilmentStore;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class FulfilmentRepository
        implements FulfilmentStore, PanacheRepository<DbFulfilment> {

    @Override
    @Transactional
    public Fulfilment save(Fulfilment fulfilment) {
        DbFulfilment db = DbFulfilment.fromDomain(fulfilment);
        persist(db);
        return db.toDomain();
    }

    @Override
    public int countWarehousesByStore(Long storeId) {
        return (int) count("storeId", storeId);
    }

    @Override
    public int countWarehousesByStoreAndProduct(Long storeId, Long productId) {
        return (int) count("storeId = ?1 and productId = ?2",
                storeId, productId);
    }

    @Override
    public int countProductsByWarehouse(Long warehouseId) {
        return (int) count("warehouseId", warehouseId);
    }

    @Override
    public List<Fulfilment> findAllFulfilments() {
        return listAll().stream().map(DbFulfilment::toDomain).toList();
    }
}
