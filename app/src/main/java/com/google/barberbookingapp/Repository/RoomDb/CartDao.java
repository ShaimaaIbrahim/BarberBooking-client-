package com.google.barberbookingapp.Repository.RoomDb;

import java.util.List;
import androidx.room.Update;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface CartDao {

    @Query("SELECT SUM (ProductPrice * ProductQuantity) from Cart WHERE userPhone =:userPhone")
    long getSumPrice(String userPhone);

    @Query("SELECT * FROM Cart WHERE userPhone =:userPhone")
    List<CartItem> getAllItemsFromCart(String userPhone);

    @Query("SELECT COUNT(*) from Cart Where userPhone =:userPhone")
    int getCountItemsInCart(String userPhone);

    @Query("select * from Cart where ProductId=:productId AND userPhone =:userPhone")
    CartItem getProductInCart(String productId , String userPhone);

    @Insert(onConflict = OnConflictStrategy.FAIL)
     void insert(CartItem...cartItems);

    @Update
    void update(CartItem cartItem);


    @Delete
    void delete(CartItem cartItem);

    @Query("DELETE FROM Cart Where userPhone=:userPhone")
    void ClearCart(String userPhone);

}
