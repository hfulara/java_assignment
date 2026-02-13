package com.fulfilment.application.monolith.fulfilment.domain.usecases.commands;

public record AssignFulfilmentCommand(
        Long storeId,
        Long productId,
        Long warehouseId
) {}
