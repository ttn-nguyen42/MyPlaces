package com.example.places.activities.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.places.databinding.PlaceItemBinding;
import com.example.places.models.data.Place;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private List<Place> placeList;
    private OnClickListener clickListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(PlaceItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Place place = placeList.get(position);
        holder.bindItem(getListener(), position, place);
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    public MainAdapter() {
        this.placeList = new ArrayList<Place>();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final PlaceItemBinding binding;

        public ViewHolder(PlaceItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindItem(OnClickListener listener, int position, Place place) {
            binding.tvName.setText(place.name);
            binding.tvDescription.setText(place.description);
            if (listener != null) {
                binding.backgroundCard.setOnClickListener(v -> listener.onClick(position, place));
            }
        }

    }

    public LinearLayoutManager getLayoutManager(Context context) {
        return new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<Place> newList) {
        placeList.clear();
        placeList = newList;
        this.notifyDataSetChanged();
    }

    public interface OnClickListener {
        void onClick(int position, Place data);
    }

    public void setListener(OnClickListener listener) {
        this.clickListener = listener;
    }

    public OnClickListener getListener() {
        return this.clickListener;
    }
}
