package com.example.places.activities.main;

import android.annotation.SuppressLint;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.places.models.data.Place;
import com.example.places.repositories.PlaceRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class MainViewModel extends ViewModel {
    private final PlaceRepository repository;
    private final CompositeDisposable disposable = new CompositeDisposable();

    private final MutableLiveData<List<Place>> liveData =
            new MutableLiveData<>(new ArrayList<Place>());

    public LiveData<List<Place>> getLiveData() {
        return liveData;
    }

    @Inject
    MainViewModel(PlaceRepository repository) {
        this.repository = repository;
    }

    public void init() {
        getPlaces();
    }


    public void getPlaces() {
        disposable.add(repository.getPlaces()
                               .subscribe(liveData::setValue));
    }

    public void removePlace(Place place) {
        disposable.add(repository.remove(place).doOnComplete(this::getPlaces).subscribe());
    }
    public void dispose() {
        disposable.dispose();
    }
}
