package com.example.places.repositories;

import com.example.places.database.dao.PlaceDao;
import com.example.places.models.data.Place;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PlaceRepository {
    private final PlaceDao dao;

    @Inject
    public PlaceRepository(PlaceDao dao) {
        this.dao = dao;
    }

    public Completable addPlace(Place place) {
        return dao.insertAll(place).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io());
    }

    public Flowable<List<Place>> getPlaces() {
        return dao.getAll().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io());
    }

    public Completable remove(Place place) {
        return dao.delete(place).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io());
    }
}
