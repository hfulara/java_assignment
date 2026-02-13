package com.fulfilment.application.monolith.fulfilment.domain.ports;

import com.fulfilment.application.monolith.fulfilment.domain.models.Fulfilment;
import java.util.List;

public interface FulfilmentStore {

  Fulfilment save(Fulfilment fulfilment);

  int countWarehousesByStore(Long storeId);

  int countWarehousesByStoreAndProduct(Long storeId, Long productId);

  int countProductsByWarehouse(Long warehouseId);

  List<Fulfilment> findAllFulfilments();
}
