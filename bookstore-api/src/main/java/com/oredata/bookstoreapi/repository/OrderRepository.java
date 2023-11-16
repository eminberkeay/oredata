package com.oredata.bookstoreapi.repository;

import com.oredata.bookstoreapi.entity.Order;
import com.oredata.bookstoreapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUser(User user);

    List<Order> findAllByUserOrderByUpdatedAtDesc(User user);
}
