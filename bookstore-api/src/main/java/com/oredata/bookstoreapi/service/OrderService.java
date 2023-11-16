package com.oredata.bookstoreapi.service;

import com.oredata.bookstoreapi.entity.Order;

import java.util.List;

public interface OrderService {

    List<Order> getAllOrders();

    Order getOrderById(Long orderId);

    List<Order> getOrdersByUser(Long userId);

    Order placeOrder(Long userId, List<Long> bookIds);
}
