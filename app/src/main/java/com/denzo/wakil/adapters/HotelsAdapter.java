package com.denzo.wakil.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.denzo.wakil.HotelViewer;
import com.denzo.wakil.R;
import com.denzo.wakil.Util.HotelView;

import java.util.ArrayList;
import java.util.List;

public class HotelsAdapter extends RecyclerView.Adapter<HotelsAdapter.MyViewHolder> implements Filterable {

    private final Context mCtx;
    private List<HotelView> hotelList;
    private List<HotelView> hotelListFull;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, location, rating, features;
        public ImageView thumbnail;
        public Button viewbutton;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            location = view.findViewById(R.id.location);
            thumbnail = view.findViewById(R.id.thumbnail);
            rating = view.findViewById(R.id.rating);
            features = view.findViewById(R.id.features);
            viewbutton = view.findViewById(R.id.viewbutton);
        }
    }

    public HotelsAdapter(Context mContext, List<HotelView> hotelList) {
        this.mCtx = mContext;
        this.hotelList = hotelList;
        this.hotelListFull = new ArrayList<>(hotelList);
    }

    public void updateList(List<HotelView> newList) {
        this.hotelList = newList;
        this.hotelListFull = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hotel_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final HotelView hotel = hotelList.get(position);
        holder.title.setText(hotel.getName());
        String locationText = "Location: " + hotel.getLocation();
        holder.location.setText(locationText);
        String ratingText = "Ratings: " + hotel.getRating();
        holder.rating.setText(ratingText);
        String featuresText = "Features: " + hotel.getFeatures();
        holder.features.setText(featuresText);

        Glide.with(mCtx).load(hotel.getThumbnail()).into(holder.thumbnail);

        holder.viewbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCtx, HotelViewer.class);
                intent.putExtra("hotelname", hotel.getName());
                mCtx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hotelList.size();
    }

    @Override
    public Filter getFilter() {
        return hotelFilter;
    }

    private Filter hotelFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<HotelView> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(hotelListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (HotelView item : hotelListFull) {
                    if (item.getName().toLowerCase().contains(filterPattern) ||
                        item.getLocation().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            hotelList.clear();
            hotelList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
