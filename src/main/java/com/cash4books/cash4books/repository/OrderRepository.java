package com.cash4books.cash4books.repository;

import com.cash4books.cash4books.entity.Orders;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(rollbackFor = Exception.class)
public interface OrderRepository extends CrudRepository<Orders,Integer> {

    Orders findByOrderID(Integer orderID);

    List<Orders> findAllByBuyerID(String buyerID);
}
