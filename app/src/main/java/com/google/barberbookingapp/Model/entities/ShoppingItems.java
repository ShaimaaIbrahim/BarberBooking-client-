package com.google.barberbookingapp.Model.entities;

public class ShoppingItems {
    private String name , image , id ;
    private long price ;


    public ShoppingItems() {

    }

    public String getId() {
        return id;
    }

    public ShoppingItems setId(String id) {
        this.id = id;
        return this;
    }

    public ShoppingItems(String name, String image, long price) {
        this.name = name;
        this.image = image;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;

    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;

    }
}
