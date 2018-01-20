package com.ryanpmartz.booktrackr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ryanpmartz.booktrackr.domain.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

	List<Book> findByUserId(Long userId);
}
