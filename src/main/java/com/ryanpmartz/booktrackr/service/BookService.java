package com.ryanpmartz.booktrackr.service;

import com.codahale.metrics.annotation.Timed;
import com.ryanpmartz.booktrackr.authentication.JwtUtil;
import com.ryanpmartz.booktrackr.domain.Book;
import com.ryanpmartz.booktrackr.domain.User;
import com.ryanpmartz.booktrackr.repository.BookRepository;
import com.ryanpmartz.booktrackr.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Autowired
    public BookService(BookRepository bookRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Timed
    @Transactional(readOnly = true)
    public List<Book> getAllBooksForUser(UUID userId) {
        return bookRepository.findByUserId(userId);
    }

    @Timed
    @Transactional(readOnly = true)
    public Optional<Book> getBook(UUID bookId) {
        return Optional.ofNullable(bookRepository.findOne(bookId));
    }

    @Timed
    @Transactional
    public Book createBook(Book book) {
        User user = userRepository.findOne(JwtUtil.tokenFromSecurityContext().getUserId());
        book.setUser(user);
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
