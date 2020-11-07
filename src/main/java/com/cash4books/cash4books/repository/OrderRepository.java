package com.cash4books.cash4books.repository;

import com.cash4books.cash4books.entity.Orders;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Orders,Integer> {
}
