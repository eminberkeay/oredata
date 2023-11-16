package com.oredata.bookstoreapi.service;

import com.oredata.bookstoreapi.entity.Book;
import com.oredata.bookstoreapi.entity.Order;
import com.oredata.bookstoreapi.entity.User;
import com.oredata.bookstoreapi.exception.MinimumOrderAmountException;
import com.oredata.bookstoreapi.exception.OrderNotFoundException;
import com.oredata.bookstoreapi.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final BookService bookService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, UserService userService, BookService bookService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.bookService = bookService;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Bu ID ile sipariş bulunamadı: " + orderId));
    }

    @Override
    public List<Order> getOrdersByUser(Long userId) {
        User user = userService.getUserById(userId);
        if (user != null) {
            return orderRepository.findAllByUserOrderByUpdatedAtDesc(user);
        }
        return null;
    }

    @Override
    public Order placeOrder(Long userId, List<Long> bookIds) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return null;
        }

        List<Book> books = bookService.getBooksByIds(bookIds);
        double totalPrice = calculateTotalPrice(books);

        if (totalPrice < 25.0) {
            throw new MinimumOrderAmountException("Minimum sepet tutarı $25'dır.");
        }

        Order order = new Order();
        order.setUser(user);
        order.setBooks(books);
        order.setTotalPrice(totalPrice);

        return orderRepository.save(order);
    }

    private double calculateTotalPrice(List<Book> books) {
        double totalPrice = 0.0;

        if (books != null) {
            for (Book book : books) {
                totalPrice += book.getPrice();
            }
        }

        return totalPrice;
    }
}
