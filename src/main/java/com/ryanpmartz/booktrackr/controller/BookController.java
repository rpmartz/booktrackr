package com.ryanpmartz.booktrackr.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.ryanpmartz.booktrackr.authentication.JwtAuthenticationToken;
import com.ryanpmartz.booktrackr.authentication.JwtUtil;
import com.ryanpmartz.booktrackr.domain.Book;
import com.ryanpmartz.booktrackr.service.BookService;


@RestController
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(final BookService bookService) {
        this.bookService = bookService;
    }

    @Timed
    @RequestMapping(value = "/books", method = RequestMethod.GET)
    public ResponseEntity<List<Book>> getAllBooks() {
        JwtAuthenticationToken jwt = JwtUtil.tokenFromSecurityContext();
        return ResponseEntity.ok(bookService.getAllBooksForUser(jwt.getUserId()));
    }

    @RequestMapping(value = "/books/{bookId}", method = RequestMethod.GET)
    public ResponseEntity<Book> getBook(@PathVariable Long bookId) {
        Optional<Book> bookOptional = bookService.getBook(bookId);

        return bookOptional
                .map(result -> {
                    JwtAuthenticationToken jwt = JwtUtil.tokenFromSecurityContext();
                    if (!result.getUser().getId().equals(jwt.getUserId())) {
                        throw new AccessDeniedException("User [" + jwt.getUserId() + "] does not have access to book [" + bookId + "]");
                    }

                    return new ResponseEntity<>(result, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Timed
    @RequestMapping(value = "/books", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) {
        Optional<Book> persistedBook = bookService.createBook(book);

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON_UTF8).body(persistedBook.orElseThrow(IllegalArgumentException::new));
    }

    @Timed
    @RequestMapping(value = "/books/{bookId}", method = RequestMethod.PUT)
    public ResponseEntity<Book> updateBook(@PathVariable Long bookId, @Valid @RequestBody Book book) {
        return bookService.getBook(bookId).map(b -> {

            JwtAuthenticationToken jwt = JwtUtil.tokenFromSecurityContext();
            if (!b.getUser().getId().equals(jwt.getUserId())) {
                throw new AccessDeniedException("User [" + jwt.getUserId() + "] does not have access to book [" + bookId + "]");
            }

            b.setTitle(book.getTitle());
            b.setAuthor(book.getAuthor());
            b.setNotes(book.getNotes());

            bookService.updateBook(b);

            return new ResponseEntity<>(b, HttpStatus.OK);

        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Timed
    @RequestMapping(value = "/books/{bookId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteBook(@PathVariable Long bookId) {
        Optional<Book> book = bookService.getBook(bookId);

        return book.map(b -> {

            JwtAuthenticationToken jwt = JwtUtil.tokenFromSecurityContext();
            if (!b.getUser().getId().equals(jwt.getUserId())) {
                throw new AccessDeniedException("User [" + jwt.getUserId() + "] does not have access to book [" + bookId + "]");
            }

            bookService.deleteBook(bookId);

            return new ResponseEntity<>(HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }
}
