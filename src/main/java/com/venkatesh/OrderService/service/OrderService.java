package com.venkatesh.OrderService.service;

import com.venkatesh.OrderService.model.OrderRequest;
import com.venkatesh.OrderService.model.OrderResponse;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);
    OrderResponse getOrderDetails(long orderId);
}
