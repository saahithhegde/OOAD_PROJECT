package com.cash4books.cash4books.repository;

import com.cash4books.cash4books.entity.OrderDetails;
import com.cash4books.cash4books.entity.Orders;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(rollbackFor = Exception.class)
public interface OrderDetailsRepository extends CrudRepository<OrderDetails,Integer> {

    @Query("SELECT distinct(orderID) from com.cash4books.cash4books.entity.OrderDetails where seller =:seller ")
    List<Integer> findAllOrdersOfSeller(@Param("seller") String seller);

    List<OrderDetails> findAllByOrderID(Integer orderID);
}
