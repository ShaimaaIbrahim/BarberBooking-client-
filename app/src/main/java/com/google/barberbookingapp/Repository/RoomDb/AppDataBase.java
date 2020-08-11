package com.google.barberbookingapp.Repository.RoomDb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(version = 1 , entities = CartItem.class , exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    public static final String DB_NAME ="MyBarberDb" ;


   public abstract  CartDao cartDao();



}
