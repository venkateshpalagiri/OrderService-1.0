package com.venkatesh.OrderService.service;

import com.venkatesh.OrderService.entity.Order;
import com.venkatesh.OrderService.external.client.ProductService;
import com.venkatesh.OrderService.model.OrderRequest;
import com.venkatesh.OrderService.repository.OrderRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service

public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductService productService;

    Logger logger= LogManager.getLogger(OrderServiceImpl.class);
    @Override
    public long placeOrder(OrderRequest orderRequest){

        //Create Order Entity and Save the data with Status Order Created
        //Call the Product Service to Block Products (Reduce the Quantity)
        /*Call the Payment Service to complete the payment as well,
        if payment successfull mark order as COMPLETE, Else CANCELLED
         */
        
        logger.info("Placing Order Request: {}", orderRequest);

        productService.reduceQuantity(orderRequest.getProductId(),orderRequest.getQuantity());
        logger.info("Creating Order with status CREATED");


        Order order=Order.builder()
                .amount(orderRequest.getTotalAmount())
                .orderStatus("CREATED")
                .productId(orderRequest.getProductId())
                .orderDate(Instant.now())
                .quantity(orderRequest.getQuantity())
                .build();
        order=orderRepository.save(order);
        logger.info("Order Places Successfully with Order Id:{}",order.getId());
        return order.getId();

    }
}
