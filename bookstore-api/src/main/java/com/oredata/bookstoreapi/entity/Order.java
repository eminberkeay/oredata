package com.oredata.bookstoreapi.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "\"order\"")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // A User can have many orders
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // An Order can have many books, assuming that we have sufficient stocks of books
    // i.e., a book with id = 1 can appear in multiple orders since we have enough of that book
    @ManyToMany
    @JoinTable(
            name = "\"order_books\"",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id"))
    private List<Book> books;

    private double totalPrice;

    private LocalDateTime orderDate;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
