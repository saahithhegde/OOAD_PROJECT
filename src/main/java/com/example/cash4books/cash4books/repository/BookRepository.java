package com.example.cash4books.cash4books.repository;

import com.example.cash4books.cash4books.entity.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends CrudRepository<Book, Integer> {
    Book findByBookID(Integer bookID);
    List<Book> findAllByUserId(String username);
    List<Book> findAllByTitle(String title);
}
