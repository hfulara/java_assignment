package com.fulfilment.application.monolith.warehouses.domain.models;

import java.time.LocalDateTime;

public class Warehouse {

  public Long id;
  public String businessUnitCode;
  public String location;
  public int capacity;
  public int stock;
  public LocalDateTime createdAt;
  public LocalDateTime archivedAt;

  public Warehouse(String businessUnitCode,
                   String location,
                   int capacity,
                   int stock
                   ) {

    this.businessUnitCode = businessUnitCode;
    this.location = location;
    this.capacity = capacity;
    this.stock = stock;

  }
}