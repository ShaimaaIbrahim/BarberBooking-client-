package com.google.barberbookingapp.Repository.RoomDb;


import android.content.Context;

import androidx.room.Room;

import static com.google.barberbookingapp.Repository.RoomDb.AppDataBase.DB_NAME;

public class RoomClient {

    private static RoomClient mInstance;
    private Context mCtx;
    //our app database object
    private AppDataBase appDatabase;
    private AppDataBase instance;

    private RoomClient(Context mCtx) {
        this.mCtx = mCtx;

        //creating the app database with Room database builder
        //MyToDos is the name of the database
        //forget old data

        appDatabase = Room.databaseBuilder(mCtx, AppDataBase.class, DB_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }


    public static synchronized RoomClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new RoomClient(mCtx);
        }
        return mInstance;
    }

    public AppDataBase getAppDatabase() {
        return appDatabase;
    }

}
