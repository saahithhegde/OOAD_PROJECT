package com.cash4books.cash4books.repository;

import com.cash4books.cash4books.entity.Cart;
import com.cash4books.cash4books.entity.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(rollbackFor = Exception.class)
public interface CartRepository extends CrudRepository<Cart, Integer> {
    List<BooksInCart> getBookIDByUsers(Users users);
    List<Cart> findAllByUsers(Users user);
    Cart findCartByBookIDAndUsers(Integer bookId,Users users);
    void deleteCartByUsers(Users user);
}
