package com.oredata.bookstoreapi.service;

import com.oredata.bookstoreapi.entity.Book;

import java.util.List;

public interface BookService {

    Book addBook(Book book);
    Book updateBook(String isbn, Book book);
    Book getBookByIsbn(String isbn);
    List<Book> getBooksByIds(List<Long> bookIds);
    List<Book> getAllBooks();
    void deleteBook(String isbn);
}
