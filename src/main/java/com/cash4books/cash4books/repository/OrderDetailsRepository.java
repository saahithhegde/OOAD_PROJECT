package com.cash4books.cash4books.repository;

import com.cash4books.cash4books.entity.OrderDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(rollbackFor = Exception.class)
public interface OrderDetailsRepository extends CrudRepository<OrderDetails,Integer> {
}
