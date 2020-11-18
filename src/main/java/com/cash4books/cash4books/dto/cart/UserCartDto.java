package com.cash4books.cash4books.dto.cart;

import java.util.List;

public class UserCartDto {
    private int total;
    private List<CartDto> cartDetails;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<CartDto> getCartDetails() {
        return cartDetails;
    }

    public void setCartDetails(List<CartDto> cartDetails) {
        this.cartDetails = cartDetails;
    }
}
