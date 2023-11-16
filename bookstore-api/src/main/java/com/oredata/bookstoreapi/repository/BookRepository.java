package com.oredata.bookstoreapi.repository;

import com.oredata.bookstoreapi.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Book findByIsbn(String isbn);
    List<Book> findAllByOrderByCreatedAt();
}
