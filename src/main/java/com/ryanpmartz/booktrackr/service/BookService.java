package com.ryanpmartz.booktrackr.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
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
    @SuppressWarnings("unchecked")
    public Optional<Book> getBook(Long bookId) {
	    Book bookForQuery = new Book();
	    bookForQuery.setId(bookId);

	    Example example = Example.of(bookForQuery);

	    return (Optional<Book>) bookRepository.findOne(example);
    }

    /**
     * Creates a new book record with the user set to the user making the request.
     *
     * @param book the <code>Book</code> from the create book request
     * @return the persisted <code>Book</code>
     */
    @Timed
    @Transactional
    public Optional<Book> createBook(Book book) {
	    User userForExample = new User();
	    userForExample.setId(JwtUtil.tokenFromSecurityContext().getUserId());

	    Example<User> example = Example.of(userForExample);
	    Optional<User> userOptional = userRepository.findOne(example);
	    userOptional.map(u -> {
		    book.setUser(u);
		    return Optional.of(bookRepository.save(book));
	    });

	    return Optional.empty();
    }

    @Timed
    @Transactional
    public void updateBook(Book book) {
        bookRepository.save(book);
    }

    @Timed
    @Transactional
    public void deleteBook(Long bookId) {
	    bookRepository.deleteById(bookId);
    }
}
