package com.google.barberbookingapp.Model.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Barber implements Parcelable{


    private String name , username , password, barberId ;
    private Double rating;
    private Long ratingTimes;


    public Barber() {
    }

    public static Creator<Barber> getCREATOR() {
        return CREATOR;
    }

    public String getName() {
        return name;
    }

    public Barber setName(String name) {
        this.name = name;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public Barber setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Barber setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getBarberId() {
        return barberId;
    }

    public Barber setBarberId(String barberId) {
        this.barberId = barberId;
        return this;
    }

    public Double getRating() {
        return rating;
    }

    public Barber setRating(Double rating) {
        this.rating = rating;
        return this;
    }

    public Long getRatingTimes() {
        return ratingTimes;
    }

    public Barber setRatingTimes(Long ratingTimes) {
        this.ratingTimes = ratingTimes;
        return this;
    }

    protected Barber(Parcel in) {
        name = in.readString();
        username = in.readString();
        password = in.readString();
        barberId = in.readString();
        if (in.readByte() == 0) {
            rating = null;
        } else {
            rating = in.readDouble();
        }
        if (in.readByte() == 0) {
            ratingTimes = null;
        } else {
            ratingTimes = in.readLong();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(barberId);
        if (rating == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(rating);
        }
        if (ratingTimes == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(ratingTimes);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Barber> CREATOR = new Creator<Barber>() {
        @Override
        public Barber createFromParcel(Parcel in) {
            return new Barber(in);
        }

        @Override
        public Barber[] newArray(int size) {
            return new Barber[size];
        }
    };
}
