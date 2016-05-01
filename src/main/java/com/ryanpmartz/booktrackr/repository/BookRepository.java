package com.ryanpmartz.booktrackr.repository;

import com.ryanpmartz.booktrackr.domain.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {
}
