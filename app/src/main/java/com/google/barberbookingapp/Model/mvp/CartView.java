package com.google.barberbookingapp.Model.mvp;

import com.google.barberbookingapp.Repository.RoomDb.CartItem;

import java.util.List;

public interface CartView {

    void ShowAllCartItems(List<CartItem> cartItemList);
    void ShowAllCartItemCount(int count);
    void showProductById(CartItem cartItem);
    void showSumPrice(long sumPrice);
}
