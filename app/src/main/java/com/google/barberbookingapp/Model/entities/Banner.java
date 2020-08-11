package com.google.barberbookingapp.Model.entities;

public class Banner {
    //Banner and LookBook Is same

    private String image;

    public Banner() {
    }

    public Banner(String image) {
        this.image = image;
    }


    public String getImage() {
        return image;
    }

    public Banner setImage(String image) {
        this.image = image;
        return this;
    }
}
