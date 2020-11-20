package com.cash4books.cash4books.repository;

import com.cash4books.cash4books.entity.UserPaymentTypes;
import com.cash4books.cash4books.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserPayementRepository extends JpaRepository<UserPaymentTypes,Integer> {
    List<UserPaymentTypes> findAllByUsers(Users user);
    UserPaymentTypes findUserPaymentTypesByCardNumber(Long cardNumber);
}
