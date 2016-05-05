package com.ryanpmartz.booktrackr.service;

import com.ryanpmartz.booktrackr.domain.Book;
import com.ryanpmartz.booktrackr.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Transactional
    public Optional<Book> getBook(Long bookId) {
        return Optional.ofNullable(bookRepository.findOne(bookId));
    }
}
