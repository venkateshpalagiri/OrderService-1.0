package com.venkatesh.OrderService.controller;

import com.venkatesh.OrderService.model.OrderRequest;
import com.venkatesh.OrderService.service.OrderService;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@Log4j2
public class OrderController {
    @Autowired
    private OrderService orderService;

    Logger logger= LogManager.getLogger(OrderController.class);

    @PostMapping("/placeOrder")
    public ResponseEntity<Long> placeOrder(@RequestBody OrderRequest orderRequest){
        logger.info("placeOrder method calling..");
        long orderId=orderService.placeOrder(orderRequest);
        return new ResponseEntity<>(orderId, HttpStatus.OK);

    }
}
