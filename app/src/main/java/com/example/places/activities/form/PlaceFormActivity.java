package com.example.places.activities.form;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import com.example.places.R;
import com.example.places.activities.main.MainAdapter;
import com.example.places.activities.main.MainViewModel;
import com.example.places.databinding.ActivityMainBinding;
import com.example.places.databinding.ActivityPlaceFormBinding;
import com.example.places.utils.base.BaseActivity;
import com.example.places.utils.base.ViewCallback;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Objects;
import java.util.logging.Logger;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;

@AndroidEntryPoint
public class PlaceFormActivity extends BaseActivity<ActivityPlaceFormBinding, PlaceFormViewModel> implements LocationListener {

    private ActivityPlaceFormBinding mBinding;
    private PlaceFormViewModel mViewModel;
    private Disposable mFormValidationSubscription;
    private final Calendar mCalendar = Calendar.getInstance();

    private static final String TAG = "PlaceFormActivity";

    private LocationManager manager = null;

    @Override
    protected void bind() {
        mBinding = ActivityPlaceFormBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
    }

    @Override
    protected void init() {
        mViewModel = new ViewModelProvider(this).get(PlaceFormViewModel.class);
        mViewModel.init();
        build();
        setupToolbar();
    }

    @Override
    protected void listen() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewModel.dispose();
        mFormValidationSubscription.dispose();
    }

    private void build() {
        setupForms();
        setupDatePicker();
        setupLocationButton();
    }

    private void setupToolbar() {
        Toolbar toolbar = mBinding.toolbar;
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.add_place);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> {
            finish();
            onBackPressed();
        });
    }

    private void setupForms() {
        mBinding.saveButton.setEnabled(false);
        mBinding.saveButton.setOnClickListener(null);
        mFormValidationSubscription = mViewModel.getSaveButtonObservable(mBinding.tiTitle, mBinding.tiDescription,
                                                                         mBinding.tiDate, mBinding.tiLocation)
                .subscribe(enabled -> {
                    Log.d(TAG, enabled.toString());
                    mBinding.saveButton.setEnabled(enabled);
                    if (enabled) {
                        mBinding.saveButton.setOnClickListener(v -> {
                            submit();
                        });
                    } else {
                        mBinding.saveButton.setOnClickListener(null);
                    }
                });
    }

    private void submit() {
        String title = Objects.requireNonNull(mBinding.tiTitle.getText()).toString();
        String description = Objects.requireNonNull(mBinding.tiDescription.getText()).toString();
        String date = Objects.requireNonNull(mBinding.tiDate.getText()).toString();
        String location = Objects.requireNonNull(mBinding.tiLocation.getText()).toString();
        mViewModel.addPlaces(title, description, date, location, this::onBackPressed);
    }

    private void setupDatePicker() {
        DatePickerDialog.OnDateSetListener listener = (view, year, month, dayOfMonth) -> {
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, month);
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        };
        mBinding.tiDate.setOnClickListener(v -> {
            new DatePickerDialog(PlaceFormActivity.this, listener,
                                 mCalendar.get(Calendar.YEAR),
                                 mCalendar.get(Calendar.MONTH),
                                 mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            updateDateField();
        });
    }

    private void updateDateField() {
        mBinding.tiDate.setText(mViewModel.convertDate(mCalendar));
    }

    private void setupLocationButton() {
        mBinding.tiLocation.setOnClickListener(v -> {
            askForLocation();
        });
    }

    private void askForLocation() {
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = manager.getBestProvider(criteria, false);
        boolean isPermissionNotGranted =
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
        if (isPermissionNotGranted) {
            String[] permissions = {
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
            };
            ActivityCompat.requestPermissions(this, permissions, 1000);

        }
        manager.requestLocationUpdates(provider, 400, 1, this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        DecimalFormat format = new DecimalFormat("#.##");
        String locationString =
                Double.valueOf(format.format(location.getLatitude())) + ", " + Double.valueOf(format.format(location.getLongitude()));
        mBinding.tiLocation.setText(locationString);
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.removeUpdates(this);
    }
}