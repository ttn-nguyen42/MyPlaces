package com.example.places.utils.injects;

import com.example.places.database.dao.PlaceDao;
import com.example.places.models.data.Place;
import com.example.places.repositories.PlaceRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class RepositoryModule {
    @Singleton
    @Provides
    static PlaceRepository providePlaceRepository(PlaceDao dao) {
        return new PlaceRepository(dao);
    }
}
