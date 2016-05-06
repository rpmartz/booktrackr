package com.ryanpmartz.booktrackr.repository;

import com.ryanpmartz.booktrackr.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {
}
