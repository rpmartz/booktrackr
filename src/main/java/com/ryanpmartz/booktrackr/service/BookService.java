package com.ryanpmartz.booktrackr.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codahale.metrics.annotation.Timed;
import com.ryanpmartz.booktrackr.authentication.JwtUtil;
import com.ryanpmartz.booktrackr.domain.Book;
import com.ryanpmartz.booktrackr.domain.User;
import com.ryanpmartz.booktrackr.repository.BookRepository;
import com.ryanpmartz.booktrackr.repository.UserRepository;

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
    public List<Book> getAllBooksForUser(Long userId) {
        return bookRepository.findByUserId(userId);
    }

    @Timed
    @Transactional(readOnly = true)
    public Optional<Book> getBook(Long bookId) {
        return Optional.ofNullable(bookRepository.findOne(bookId));
    }

    /**
     * Creates a new book record with the user set to the user making the request.
     *
     * @param book the <code>Book</code> from the create book request
     * @return the persisted <code>Book</code>
     */
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
    public void deleteBook(Long bookId) {
        bookRepository.delete(bookId);
    }
}
