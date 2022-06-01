package com.example.places.activities.map;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.places.R;
import com.example.places.databinding.ActivityMapBinding;
import com.example.places.models.data.Place;
import com.example.places.utils.base.BaseActivity;
import com.example.places.utils.other.Constants;

import javax.annotation.Nullable;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MapActivity extends BaseActivity<MapViewModel, ActivityMapBinding> implements Constants {
    private ActivityMapBinding mBinding;
    private MapViewModel mViewModel;

    private @Nullable
    Place place = null;

    @Override
    protected void bind() {
        mBinding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
    }

    @Override
    protected void init() {
        mViewModel = new ViewModelProvider(this).get(MapViewModel.class);
        mViewModel.init();
        grabExtra();
        build();
    }

    @Override
    protected void listen() {

    }

    private void grabExtra() {
        if (getIntent().getExtras() != null) {
            if (getIntent().hasExtra(MAP_PLACE)) {
                place = getIntent().getParcelableExtra(MAP_PLACE);
            }
        }
    }

    private void build() {
        setupToolbar();
        buildMapFragment();
    }

    private void setupToolbar() {
        Toolbar toolbar = mBinding.toolbar;
        toolbar.setTitle(R.string.map);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            ActionBar support = getSupportActionBar();
            support.setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(v -> {
                finish();
                onBackPressed();
            });
        }
    }

    private void buildMapFragment() {
        if (place != null) {

        }
    }
}