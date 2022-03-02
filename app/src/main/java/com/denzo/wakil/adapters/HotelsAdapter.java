package com.denzo.wakil.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.denzo.wakil.R;
import com.denzo.wakil.Util.HotelView;

import java.util.List;

public class HotelsAdapter extends RecyclerView.Adapter<HotelsAdapter.MyViewHolder> {

    private Context mCtx;
    private List<HotelView> hotelList;
    public HotelView hotel;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, location,rating,features;
        public ImageView thumbnail, overflow;
        public Button viewbutton;
        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            location = (TextView) view.findViewById(R.id.location);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            rating =  view.findViewById(R.id.rating);
            features = view.findViewById(R.id.features);
            viewbutton = view.findViewById(R.id.viewbutton);
        }
    }


    public HotelsAdapter(Context mContext, List<HotelView> hotelList) {
        this.mCtx = mContext;
        this.hotelList = hotelList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hotel_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        hotel = hotelList.get(position);
        holder.title.setText(hotel.getName());
        holder.location.setText("Location: "+hotel.getLocation());
        holder.rating.setText("Ratings: "+hotel.getRating());
        holder.features.setText("Features: "+hotel.getFeatures());
        // loading album cover using Glide library
        Glide.with(mContext).load(hotel.getThumbnail()).into(holder.thumbnail);

        holder.viewbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCtx, HotelViewer.class);
                intent.putExtra("hotelname",holder.title.getText().toString());
                mCtx.startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return hotelList.size();
    }
}