package com.codepath.tripplannerapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder>{
    private Context context;
    private List<Trip> trips;

    public TripAdapter(Context context, List<Trip> trips) {
        this.trips = trips;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.trip_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  TripAdapter.ViewHolder holder, int position) {
        Trip trip = trips.get(position);
        holder.bind(trip);
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

     public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivTripImageList;
        private TextView tvTripNameList;
        private TextView tvTripDates;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivTripImageList = itemView.findViewById(R.id.ivTripImageList);
            tvTripNameList = itemView.findViewById(R.id.tvTripNameList);
            tvTripDates = itemView.findViewById(R.id.tvTripDates);
        }

        public void bind(Trip trip) {
            // Bind the post data to the view elements
            tvTripNameList.setText(trip.getTripName());
            Log.i("TripAdapter", trip.getTripName());
            ParseFile image = trip.getTripImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(ivTripImageList);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, TripDetailsActivity.class);
                    intent.putExtra("trip", Parcels.wrap(trip));
                    context.startActivity(intent);
                    ((Activity) itemView.getContext()).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });

        }

     }

    public void clear() {
        trips.clear();
        notifyDataSetChanged();
    }

}
