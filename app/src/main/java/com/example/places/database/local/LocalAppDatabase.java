package com.example.places.database.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.places.database.dao.PlaceDao;
import com.example.places.models.data.Place;

@Database(entities = {Place.class}, version = 3)
public abstract class LocalAppDatabase extends RoomDatabase {
    public static String DATABASE_NAME = "local_database";
    public abstract PlaceDao placeDao();
}
