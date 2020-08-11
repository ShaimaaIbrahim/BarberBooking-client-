package com.google.barberbookingapp.Model.mvp;

import android.content.Context;

import com.google.barberbookingapp.Repository.RoomDb.CartItem;

import java.util.List;

public interface CartPresenter {

    List<CartItem> getAllItemsFromCart(Context context , String userPhone);

     int getCountItemsInCart(String userPhone , Context context);

      CartItem getProductInCart(String productId , String userPhone , Context context);

    void insert( Context context , CartItem cartItem);

    void update(CartItem cartItem , Context context);

    void delete(CartItem cartItem , Context context);

    void ClearCart(String userPhone , Context context);

    long sumPrice(String userPhone , Context context);

    long deletePrice(String userPhone , Context context);
}
