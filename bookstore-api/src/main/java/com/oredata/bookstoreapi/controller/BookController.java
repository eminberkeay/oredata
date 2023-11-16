package com.oredata.bookstoreapi.controller;

import com.oredata.bookstoreapi.entity.Book;
import com.oredata.bookstoreapi.exception.UnauthorizedUserException;
import com.oredata.bookstoreapi.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/books", produces = "application/json", consumes = "application/json")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{isbn}")
    public Book getBookByIsbn(@PathVariable String isbn) {
        return bookService.getBookByIsbn(isbn);
    }

    // Methods with admin-only authority

    @PostMapping
    public Book addBook(@RequestBody Book book, @AuthenticationPrincipal UserDetails userDetails) {
        if (isAdmin(userDetails)) {
            return bookService.addBook(book);
        } else {
            throw new UnauthorizedUserException("Yetkisiz Kullanıcı!");
        }
    }

    @PutMapping("/{isbn}")
    public Book updateBook(@PathVariable String isbn, @RequestBody Book book, @AuthenticationPrincipal UserDetails userDetails) {
        if (isAdmin(userDetails)) {
            return bookService.updateBook(isbn, book);
        } else {
            throw new UnauthorizedUserException("Yetkisiz Kullanıcı!");
        }
    }

    @DeleteMapping("/{isbn}")
    public void deleteBook(@PathVariable String isbn, @AuthenticationPrincipal UserDetails userDetails) {
        if (isAdmin(userDetails)) {
            bookService.deleteBook(isbn);
        } else {
            throw new UnauthorizedUserException("Yetkisiz Kullanıcı!");
        }
    }

    private boolean isAdmin(UserDetails userDetails) {
        return userDetails.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }
}
