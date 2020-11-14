package com.cash4books.cash4books.dto.orders;

import com.cash4books.cash4books.entity.OrderDetails;
import com.cash4books.cash4books.entity.Orders;

import java.util.List;

public class BooksOrderDto {

   Orders orders;
   List<OrderDetails> orderDetails;

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public List<OrderDetails> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetails> orderDetails) {
        this.orderDetails = orderDetails;
    }
}
