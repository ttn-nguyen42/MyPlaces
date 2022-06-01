package com.example.places.utils.injects;

import android.content.Context;

import androidx.room.Room;

import com.example.places.database.dao.PlaceDao;
import com.example.places.database.local.LocalAppDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
final class ProviderModule {
    @Provides
    @Singleton
    static LocalAppDatabase provideLocalDatabase(@ApplicationContext Context context) {
        return Room
                .databaseBuilder(context, LocalAppDatabase.class, LocalAppDatabase.DATABASE_NAME)
                .fallbackToDestructiveMigration().build();
    }

    @Provides
    @Singleton
    static PlaceDao providePlaceDao(LocalAppDatabase database) {
        return database.placeDao();
    }
}
