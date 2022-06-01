package com.example.places.activities.form;

import android.widget.Button;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.places.models.data.Place;
import com.example.places.repositories.PlaceRepository;
import com.example.places.utils.base.Validation;
import com.example.places.utils.base.ViewCallback;
import com.google.android.material.textfield.TextInputEditText;
import com.jakewharton.rxbinding4.widget.RxTextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Function4;
import io.reactivex.schedulers.Schedulers;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function3;

@HiltViewModel
public class PlaceFormViewModel extends ViewModel {
    private final PlaceRepository repository;
    private final CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    PlaceFormViewModel(PlaceRepository repository) {
        this.repository = repository;
    }

    public void init() {
    }

    public void dispose() {
        disposable.dispose();
    }

    public Observable<Validation> getTitleObservable(TextInputEditText ti) {
        return RxTextView.textChanges(ti).debounce(500, TimeUnit.MILLISECONDS).skip(1)
                .observeOn(AndroidSchedulers.mainThread())
                .map(title -> {
                    Validation validation = Validation.validateEmptiness(title.toString());
                    if (!validation.isValidated()) {
                        ti.setError(validation.getReason());
                    } else {
                        ti.setError(null);
                    }
                    return validation;
                });
    }

    public Observable<Validation> getDescriptionObservable(TextInputEditText ti) {
        return RxTextView.textChanges(ti).debounce(500, TimeUnit.MILLISECONDS).skip(1)
                .observeOn(AndroidSchedulers.mainThread())
                .map(description -> {
                    Validation validation = Validation.validateEmptiness(description.toString());
                    if (!validation.isValidated()) {
                        ti.setError(validation.getReason());
                    } else {
                        ti.setError(null);
                    }
                    return validation;
                });
    }

    public Observable<Validation> getDateObservable(TextInputEditText ti) {
        return RxTextView.textChanges(ti).debounce(500, TimeUnit.MILLISECONDS).skip(1)
                .observeOn(AndroidSchedulers.mainThread())
                .map(date -> {
                    Validation validation = Validation.validateEmptiness(date.toString());
                    if (!validation.isValidated()) {
                        ti.setError(validation.getReason());
                    } else {
                        ti.setError(null);
                    }
                    return validation;
                });
    }

    public Observable<Validation> getLocationObservable(TextInputEditText ti) {
        return RxTextView.textChanges(ti).debounce(500, TimeUnit.MILLISECONDS).skip(1)
                .observeOn(AndroidSchedulers.mainThread())
                .map(location -> {
                    Validation validation = Validation.validateEmptiness(location.toString());
                    if (!validation.isValidated()) {
                        ti.setError(validation.getReason());
                    } else {
                        ti.setError(null);
                    }
                    return validation;
                });
    }

    public Observable<Boolean> getSaveButtonObservable(TextInputEditText title, TextInputEditText desc,
                                                       TextInputEditText date, TextInputEditText location) {
        return Observable.combineLatest(getTitleObservable(title), getDateObservable(date),
                                        getDescriptionObservable(desc),
                                        getLocationObservable(location),
                                        (title1, desc1, date1, location1) -> title1.isValidated() && desc1.isValidated() && date1.isValidated() && location1.isValidated());
    }

    public void addPlaces(String title, String desc, String date, String location, ViewCallback callback) {
        String[] locationIndexes = location.split(",");
        double latitude = Double.parseDouble(locationIndexes[0].trim());
        double longitude = Double.parseDouble(locationIndexes[0].trim());
        Place place = new Place(title, desc, date, location, latitude, longitude);
        disposable.add(repository.addPlace(place).subscribe());
        callback.callback();
    }

    public String convertDate(Calendar calendar) {
        String format = "dd.MM.yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

}
