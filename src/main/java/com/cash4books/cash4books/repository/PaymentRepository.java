package com.cash4books.cash4books.repository;

import com.cash4books.cash4books.entity.Payment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends CrudRepository<Payment, Integer> {
}
