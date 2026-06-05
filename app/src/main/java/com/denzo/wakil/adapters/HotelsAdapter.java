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
import com.denzo.wakil.ui.booking.HotelViewer;
import com.denzo.wakil.R;
import com.denzo.wakil.Util.HotelView;

import android.net.Uri;
import android.widget.Toast;
import com.denzo.wakil.Database.AppDatabase;
import com.denzo.wakil.Database.BlockedEntity;
import com.denzo.wakil.Util.CurrentUser;
import java.util.ArrayList;
import java.util.List;

public class HotelsAdapter extends RecyclerView.Adapter<HotelsAdapter.MyViewHolder> implements Filterable {

    private final Context mCtx;
    private List<HotelView> hotelList;
    private List<HotelView> hotelListFull;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, location, rating, features, bookingInfo;
        public ImageView thumbnail;
        public Button contactButton, shareButton, blockButton, cancelButton;
        public View cardView;

        public MyViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.card_view);
            title = view.findViewById(R.id.title);
            location = view.findViewById(R.id.location);
            thumbnail = view.findViewById(R.id.thumbnail);
            rating = view.findViewById(R.id.rating);
            features = view.findViewById(R.id.features);
            bookingInfo = view.findViewById(R.id.booking_info);
            contactButton = view.findViewById(R.id.btn_contact);
            shareButton = view.findViewById(R.id.btn_share);
            blockButton = view.findViewById(R.id.btn_block);
            cancelButton = view.findViewById(R.id.btn_cancel_booking);
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
        holder.location.setText(mCtx.getString(R.string.label_location, hotel.getLocation()));
        holder.rating.setText(mCtx.getString(R.string.label_rating, String.valueOf(hotel.getRating())));
        holder.features.setText(mCtx.getString(R.string.label_features, hotel.getFeatures()));

        if (hotel.isBooked()) {
            holder.bookingInfo.setVisibility(View.VISIBLE);
            String info = "Check-in: " + hotel.getCheckInDate() + "\n" +
                        "Check-out: " + hotel.getCheckOutDate() + "\n" +
                        "Guests: " + hotel.getGuestsCount() + " | Total: $" + hotel.getTotalPrice() + "\n" +
                        "Status: " + hotel.getStatus();
            holder.bookingInfo.setText(info);
            holder.cancelButton.setVisibility(View.VISIBLE);
        } else {
            holder.bookingInfo.setVisibility(View.GONE);
            holder.cancelButton.setVisibility(View.GONE);
        }

        Glide.with(mCtx).load(hotel.getThumbnail()).into(holder.thumbnail);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCtx, HotelViewer.class);
                intent.putExtra("hotelname", hotel.getName());
                mCtx.startActivity(intent);
            }
        });

        holder.contactButton.setOnClickListener(v -> {
            if (hotel.getContact() != null) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + hotel.getContact()));
                mCtx.startActivity(intent);
            } else {
                Toast.makeText(mCtx, R.string.toast_contact_unavailable, Toast.LENGTH_SHORT).show();
            }
        });

        holder.shareButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String shareMessage = mCtx.getString(R.string.share_house_title) + ": " + hotel.getName() + "\n" +
                                mCtx.getString(R.string.hint_location) + ": " + hotel.getLocation();
            intent.putExtra(Intent.EXTRA_SUBJECT, mCtx.getString(R.string.share_house_title));
            intent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            mCtx.startActivity(Intent.createChooser(intent, mCtx.getString(R.string.share_via)));
        });

        holder.blockButton.setOnClickListener(v -> {
            AppDatabase db = AppDatabase.getInstance(mCtx);
            new Thread(() -> {
                db.blockedDao().insert(new BlockedEntity(CurrentUser.username, hotel.getOwnerUsername(), hotel.getId()));
                ((android.app.Activity) mCtx).runOnUiThread(() -> {
                    Toast.makeText(mCtx, R.string.toast_content_blocked, Toast.LENGTH_SHORT).show();
                    int pos = holder.getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        hotelList.remove(pos);
                        notifyItemRemoved(pos);
                    }
                });
            }).start();
        });

        holder.cancelButton.setOnClickListener(v -> {
            AppDatabase db = AppDatabase.getInstance(mCtx);
            new Thread(() -> {
                com.denzo.wakil.Database.BookingEntity booking = db.bookingDao().getSpecificBooking(CurrentUser.username, hotel.getId());
                if (booking != null) {
                    db.bookingDao().deleteBooking(booking);
                    ((android.app.Activity) mCtx).runOnUiThread(() -> {
                        Toast.makeText(mCtx, R.string.toast_booking_cancelled, Toast.LENGTH_SHORT).show();
                        int pos = holder.getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            hotelList.remove(pos);
                            notifyItemRemoved(pos);
                        }
                    });
                }
            }).start();
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
