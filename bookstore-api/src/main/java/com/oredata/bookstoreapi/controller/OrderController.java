package com.oredata.bookstoreapi.controller;

import com.oredata.bookstoreapi.entity.Order;
import com.oredata.bookstoreapi.exception.MinimumOrderAmountException;
import com.oredata.bookstoreapi.exception.OrderNotFoundException;
import com.oredata.bookstoreapi.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/orders", produces = "application/json", consumes = "application/json")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestParam Long userId, @RequestParam List<Long> bookIds) {
        try {
            Order order = orderService.placeOrder(userId, bookIds);
            return new ResponseEntity<>(order, HttpStatus.CREATED);
        } catch (MinimumOrderAmountException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable Long userId) {
        List<Order> orders = orderService.getOrdersByUser(userId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/details/{orderId}")
    public ResponseEntity<Order> getOrderDetails(@PathVariable Long orderId) {
        try {
            Order order = orderService.getOrderById(orderId);
            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (OrderNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
