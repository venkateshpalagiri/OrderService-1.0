package com.venkatesh.OrderService.service;

import com.venkatesh.OrderService.entity.Order;
import com.venkatesh.OrderService.exception.CustomException;
import com.venkatesh.OrderService.external.client.PaymentService;
import com.venkatesh.OrderService.external.client.ProductService;
import com.venkatesh.OrderService.model.*;
import com.venkatesh.OrderService.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Random;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public long placeOrder(OrderRequest orderRequest){

        //Create Order Entity and Save the data with Status Order Created
        //Call the Product Service to Block Products (Reduce the Quantity)
        /*Call the Payment Service to complete the payment as well,
        if payment successfull mark order as COMPLETE, Else CANCELLED
         */
        log.info("Placing Order Request: {}", orderRequest);

        productService.reduceQuantity(orderRequest.getProductId(),orderRequest.getQuantity());
        log.info("Creating Order with status CREATED");


        Order order=Order.builder()
                .amount(orderRequest.getTotalAmount())
                .orderStatus("CREATED")
                .productId(orderRequest.getProductId())
                .orderDate(Instant.now())
                .quantity(orderRequest.getQuantity())
                .build();
        order=orderRepository.save(order);
        log.info("Order Places Successfully with Order Id:{}",order.getId());

//        for generating random long for refereneceNumber
        Random random=new Random();
        long randomLong=random.nextLong();
        if(randomLong<0){
            randomLong=randomLong*(-1);
        }

        PaymentRequest paymentRequest=PaymentRequest
                .builder()
                .orderId(order.getId())
                .paymentMode(orderRequest.getPaymentmode())
                .referenceNumber(randomLong)
                .amount(orderRequest.getTotalAmount())
                .build();

        String orderStatus=null;
        try {
            paymentService.doPayment(paymentRequest);
            log.info("Payment done Successfully. changing the Order status to ORDER_PLACED");
            orderStatus="ORDER_PLACED";

        }catch (Exception e){
            log.error("Error occurred in payment. Changing order status to PAYMENT_FAILED");
            orderStatus="PAYMENT_FAILED";
        }
        order.setOrderStatus(orderStatus);
        orderRepository.save(order);

        return order.getId();

    }
    @Override
    public OrderResponse getOrderDetails(long orderId) {
        Order order=orderRepository.findById(orderId)
                .orElseThrow(()->new CustomException("Order not found with the given Id:"+orderId,"ORDER_NOT_FOUND",500));

        ProductResponse productResponse
                = restTemplate.getForObject(
                "http://PRODUCT-SERVICE/product/" + order.getProductId(),
                ProductResponse.class);
        PaymentResponse paymentResponse=
                restTemplate.getForObject(
                        "http://PAYMENT-SERVICE/payment/"+order.getId(),PaymentResponse.class
                );

        ProductResponse productResponse1=
                ProductResponse
                        .builder()
                        .productId(order.getProductId())
                        .productName(productResponse.getProductName())
                        .price(order.getAmount())
                        .quantity(order.getQuantity())
                        .build();


        OrderResponse orderResponse=OrderResponse
                .builder()
                .orderId(order.getId())
                .orderStatus(order.getOrderStatus())
                .orderDate(order.getOrderDate())
                .totalAmount(order.getAmount())
                .productResponse(productResponse1)
                .paymentResponse(paymentResponse)
                .build();

        return orderResponse;
    }
}
