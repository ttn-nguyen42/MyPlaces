package com.example.places.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.places.models.data.Place;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import kotlinx.coroutines.flow.Flow;

@Dao
public interface PlaceDao {
    @Query("SELECT * FROM place")
    Flowable<List<Place>> getAll();

    @Query("SELECT * FROM place WHERE name LIKE :at")
    Flowable<Place> findByPlace(String at);

    @Insert
    Completable insertAll(Place... places);

    @Delete
    Completable delete(Place place);
}
