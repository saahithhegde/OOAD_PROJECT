package com.example.cash4books.cash4books.repository;

import com.example.cash4books.cash4books.entity.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends CrudRepository<Book, Integer> {

    List<Book> findByUsername(String username);
}
