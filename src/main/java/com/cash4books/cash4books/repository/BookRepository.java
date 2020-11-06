package com.cash4books.cash4books.repository;

import com.cash4books.cash4books.dto.book.BookDtoQuery;
import com.cash4books.cash4books.entity.Book;
import com.cash4books.cash4books.entity.Users;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends CrudRepository<Book, Integer> {
    Book findByBookID(Integer bookID);
    List<Book> findAllByUsers(Users user);
    List<Book> findByTitleContaining(String title);
    List<Book> findAllByAuthor(String author);
    List<Book> findAllByCategory(String category);
    List<Book> findAllByIsbn(String isbn);
    Book findBookByBookIDAndUsers(Integer bookID,Users users);

    @Query("SELECT new com.cash4books.cash4books.dto.book.BookDtoQuery(b.isbn ,b.title,COUNT(b)) FROM Book b GROUP BY b.isbn")
    List<BookDtoQuery> fetchAllDisticntIsbn();
}
