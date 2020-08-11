package com.google.barberbookingapp.Repository.RoomDb;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "Cart")
public class CartItem  {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "ProductId")
    private String ProductId;


    @ColumnInfo(name = "ProductName")
    private String ProductName;

    @ColumnInfo(name = "userPhone")
    private String userPhone;

    @ColumnInfo(name = "ProductImage")
    private String ProductImage;


    @ColumnInfo(name = "ProductPrice")
    private long ProductPrice;


    @ColumnInfo(name = "ProductQuantity")
    private int ProductQuantity;

    @NonNull
    public String getProductId() {
        return ProductId;
    }

    public void setProductId(@NonNull String productId) {
        ProductId = productId;

    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;

    }

    public String getProductImage() {
        return ProductImage;
    }

    public void setProductImage(String productImage) {
        ProductImage = productImage;

    }

    public long getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(long productPrice) {
        ProductPrice = productPrice;

    }

    public int getProductQuantity() {
        return ProductQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        ProductQuantity = productQuantity;

    }

    public String getUserPhone() {
        return userPhone;
    }

    public void  setUserPhone(String userPhone) {
        this.userPhone = userPhone;

    }


    public CartItem() {
    }


}
