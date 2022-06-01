package com.example.places.activities.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.places.R;
import com.example.places.activities.form.PlaceFormActivity;
import com.example.places.activities.map.MapActivity;
import com.example.places.databinding.ActivityMainBinding;
import com.example.places.models.data.Place;
import com.example.places.utils.base.BaseActivity;
import com.example.places.utils.other.Constants;

import java.util.List;
import java.util.logging.Logger;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> implements Constants {
    private ActivityMainBinding mBinding;
    private MainViewModel mViewModel;
    private MainAdapter mAdapter;

    private static final String TAG = "MainActivity";

    @Override
    protected void bind() {
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
    }

    @Override
    protected void init() {
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mViewModel.init();
        build();
        setupToolbar();
        setupRecyclerView();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void listen() {
        mViewModel.getLiveData().observe(this, (List<Place> places) -> {
            if (mViewModel.getLiveData().getValue() == null) {
                mBinding.mainRecyclerView.setVisibility(View.INVISIBLE);
                mBinding.tvEmptyList.setVisibility(View.VISIBLE);
                return;
            } else if (mViewModel.getLiveData().getValue().isEmpty() && mViewModel.getLiveData().getValue() != null) {
                mBinding.mainRecyclerView.setVisibility(View.INVISIBLE);
                mBinding.tvEmptyList.setVisibility(View.VISIBLE);
                return;
            }
            mBinding.mainRecyclerView.setVisibility(View.VISIBLE);
            mBinding.tvEmptyList.setVisibility(View.INVISIBLE);
            mAdapter.updateList(places);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewModel.getPlaces();
    }

    private void setupToolbar() {
        Toolbar mToolbar = mBinding.toolbar;
        setSupportActionBar(mToolbar);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewModel.dispose();
    }

    private void setupRecyclerView() {
        mAdapter = new MainAdapter();
        mBinding.mainRecyclerView.setAdapter(mAdapter);
        mBinding.mainRecyclerView.setLayoutManager(mAdapter.getLayoutManager(this));
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelpCallback(0,
                                                                               ItemTouchHelper.RIGHT));
        helper.attachToRecyclerView(mBinding.mainRecyclerView);
        mAdapter.setListener((position, data) -> {
            moveToMap(data);
        });
    }

    private void build() {
        mBinding.mainFab.setOnClickListener(v -> {
            startFormActivity();
        });
    }

    private void startFormActivity() {
        Intent formIntent = new Intent(this, PlaceFormActivity.class);
        startActivity(formIntent);
    }

    private void moveToMap(Place place) {
        Intent mapIntent = new Intent(this, MapActivity.class);
        mapIntent.putExtra(MAP_PLACE, place);
        startActivity(mapIntent);
    }

    public Context getContext() {
        return this;
    }

    public class ItemTouchHelpCallback extends ItemTouchHelper.SimpleCallback {

        public ItemTouchHelpCallback(int dragDirs, int swipeDirs) {
            super(dragDirs, swipeDirs);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            List<Place> places = mViewModel.getLiveData().getValue();
            if (places != null) {
                Place place = places.get(position);
                if (direction == ItemTouchHelper.RIGHT) {
                    mViewModel.removePlace(place);
                }
            }
        }
    }
}