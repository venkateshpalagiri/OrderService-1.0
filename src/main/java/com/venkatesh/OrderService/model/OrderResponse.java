package com.venkatesh.OrderService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private long orderId;
    private String orderStatus;
    private Instant orderDate;
    private long totalAmount;
    private ProductResponse productResponse;
    private PaymentResponse paymentResponse;



}
