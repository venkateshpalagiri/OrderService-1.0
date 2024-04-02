package com.venkatesh.OrderService.service;

import com.venkatesh.OrderService.model.OrderRequest;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);
}
