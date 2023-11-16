package com.oredata.bookstoreapi.service;

import com.oredata.bookstoreapi.entity.Book;
import com.oredata.bookstoreapi.exception.BookNotFoundException;
import com.oredata.bookstoreapi.exception.InsufficientStockException;
import com.oredata.bookstoreapi.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAllByOrderByCreatedAt();
    }

    @Override
    public Book getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbn(isbn);
        if (book == null) {
            throw new BookNotFoundException("Bu ISBN numarası ile kitap bulunamadı: " + isbn);
        }
        return book;
    }

    @Override
    public List<Book> getBooksByIds(List<Long> bookIds) {
        return bookRepository.findAllById(bookIds);
    }

    @Override
    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book updateBook(String isbn, Book book) {
        Book existingBook = getBookByIsbn(isbn);

        if (book.getStockQuantity() < 0) {
            throw new InsufficientStockException("Yeterli stok kalmadı!");
        }

        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setPrice(book.getPrice());
        existingBook.setStockQuantity(book.getStockQuantity());

        return bookRepository.save(existingBook);
    }

    @Override
    public void deleteBook(String isbn) {
        Book book = getBookByIsbn(isbn);
        bookRepository.delete(book);
    }
}
