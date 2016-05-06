package com.ryanpmartz.booktrackr.controller;

import com.ryanpmartz.booktrackr.domain.Book;
import com.ryanpmartz.booktrackr.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @RequestMapping(value = "/books", method = RequestMethod.GET)
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @RequestMapping(value = "/books/{bookId}", method = RequestMethod.GET)
    public ResponseEntity<Book> getBook(@PathVariable UUID bookId) {
        Optional<Book> bookOptional = bookService.getBook(bookId);

        return bookOptional
                .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // TODO: integration test
    @RequestMapping(value = "/books", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) {
        Book persistedBook = bookService.createBook(book);

        return ResponseEntity.status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON_UTF8).body(persistedBook);
    }
}
