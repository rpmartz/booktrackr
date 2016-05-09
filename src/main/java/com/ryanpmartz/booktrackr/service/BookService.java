package com.ryanpmartz.booktrackr.service;

import com.codahale.metrics.annotation.Timed;
import com.ryanpmartz.booktrackr.domain.Book;
import com.ryanpmartz.booktrackr.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Timed
    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Timed
    @Transactional(readOnly = true)
    public Optional<Book> getBook(UUID bookId) {
        return Optional.ofNullable(bookRepository.findOne(bookId));
    }

    @Timed
    @Transactional
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    @Timed
    @Transactional
    public void updateBook(Book book) {
        bookRepository.save(book);
    }

    @Timed
    @Transactional
    public void deleteBook(UUID bookId) {
        bookRepository.delete(bookId);
    }
}
