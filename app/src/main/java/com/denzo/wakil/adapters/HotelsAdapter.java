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

import android.graphics.Color;
import android.net.Uri;
import android.widget.Toast;
import com.denzo.wakil.Database.AppDatabase;
import com.denzo.wakil.Database.BlockedEntity;
import com.denzo.wakil.Database.BookingEntity;
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
        
        if (hotel.getName() != null) {
            holder.title.setText(hotel.getName());
        } else {
            holder.title.setText(mCtx.getString(R.string.unknown_hotel));
        }

        if (hotel.getLocation() != null && !hotel.getLocation().isEmpty()) {
            holder.location.setText(mCtx.getString(R.string.label_location, hotel.getLocation()));
            holder.location.setVisibility(View.VISIBLE);
        } else {
            holder.location.setVisibility(View.GONE);
        }

        holder.rating.setText(mCtx.getString(R.string.label_rating, String.valueOf(hotel.getRating())));
        
        // Granular Rating color and label logic
        String ratingLabel;
        if (hotel.getRating() >= 5) {
            holder.rating.setTextColor(Color.parseColor("#2E7D32")); // Dark Green
            ratingLabel = "Excellent";
        } else if (hotel.getRating() == 4) {
            holder.rating.setTextColor(Color.parseColor("#4CAF50")); // Green
            ratingLabel = "Very Good";
        } else if (hotel.getRating() == 3) {
            holder.rating.setTextColor(Color.parseColor("#FF9800")); // Orange
            ratingLabel = "Good";
        } else if (hotel.getRating() == 2) {
            holder.rating.setTextColor(Color.parseColor("#FF5722")); // Deep Orange
            ratingLabel = "Fair";
        } else if (hotel.getRating() == 1) {
            holder.rating.setTextColor(Color.parseColor("#D32F2F")); // Red
            ratingLabel = "Poor";
        } else {
            holder.rating.setTextColor(Color.GRAY);
            ratingLabel = "No Rating";
        }
        
        if (!ratingLabel.equals("No Rating")) {
            holder.rating.setText(mCtx.getString(R.string.label_rating, String.valueOf(hotel.getRating())) + " - " + ratingLabel);
        }

        if (hotel.getFeatures() != null && !hotel.getFeatures().isEmpty()) {
            String featuresText = hotel.getFeatures();
            // Add price category to features if available
            if (hotel.getPricePerNight() > 0) {
                String priceCat;
                if (hotel.getPricePerNight() < 50) {
                    priceCat = "[Budget]";
                } else if (hotel.getPricePerNight() <= 150) {
                    priceCat = "[Mid-range]";
                } else {
                    priceCat = "[Luxury]";
                }
                featuresText = priceCat + " " + featuresText;
            }
            holder.features.setText(mCtx.getString(R.string.label_features, featuresText));
            holder.features.setVisibility(View.VISIBLE);
        } else {
            holder.features.setVisibility(View.GONE);
        }

        if (hotel.isBooked()) {
            holder.bookingInfo.setVisibility(View.VISIBLE);
            String info = mCtx.getString(R.string.label_booking_details,
                    hotel.getCheckInDate() != null ? hotel.getCheckInDate() : mCtx.getString(R.string.label_na),
                    hotel.getCheckOutDate() != null ? hotel.getCheckOutDate() : mCtx.getString(R.string.label_na),
                    hotel.getGuestsCount(),
                    hotel.getTotalPrice(),
                    hotel.getStatus() != null ? hotel.getStatus() : mCtx.getString(R.string.status_unknown));
            holder.bookingInfo.setText(info);
            
            // Status color logic
            if ("Confirmed".equalsIgnoreCase(hotel.getStatus())) {
                holder.bookingInfo.setTextColor(Color.parseColor("#2E7D32"));
                holder.cancelButton.setVisibility(View.VISIBLE);
                holder.cancelButton.setEnabled(true);
                holder.cancelButton.setAlpha(1.0f);
            } else if ("Pending".equalsIgnoreCase(hotel.getStatus())) {
                holder.bookingInfo.setTextColor(Color.parseColor("#EF6C00"));
                holder.cancelButton.setVisibility(View.VISIBLE);
                holder.cancelButton.setEnabled(true);
                holder.cancelButton.setAlpha(0.8f);
            } else if ("Cancelled".equalsIgnoreCase(hotel.getStatus())) {
                holder.bookingInfo.setTextColor(Color.parseColor("#D32F2F"));
                holder.cancelButton.setVisibility(View.GONE); // Hide cancel for already cancelled
            } else {
                holder.bookingInfo.setTextColor(Color.GRAY);
                holder.cancelButton.setVisibility(View.VISIBLE);
            }
        } else {
            holder.bookingInfo.setVisibility(View.GONE);
            holder.cancelButton.setVisibility(View.GONE);
        }

        if (hotel.getThumbnail() != 0) {
            Glide.with(mCtx).load(hotel.getThumbnail()).into(holder.thumbnail);
        } else {
            Glide.with(mCtx).load(R.drawable.hicon1).into(holder.thumbnail); // Placeholder
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCtx, HotelViewer.class);
                intent.putExtra("hotelname", hotel.getName());
                mCtx.startActivity(intent);
            }
        });

        // Contact button availability
        if (hotel.getContact() == null || hotel.getContact().isEmpty()) {
            holder.contactButton.setAlpha(0.5f);
        } else {
            holder.contactButton.setAlpha(1.0f);
        }

        holder.contactButton.setOnClickListener(v -> {
            if (hotel.getContact() != null && !hotel.getContact().isEmpty()) {
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
            StringBuilder shareMessage = new StringBuilder();
            
            if (hotel.getName() != null) {
                shareMessage.append(mCtx.getString(R.string.share_house_title)).append(": ").append(hotel.getName()).append("\n");
            } else {
                shareMessage.append(mCtx.getString(R.string.share_house_title)).append("\n");
            }
            
            if (hotel.getLocation() != null && !hotel.getLocation().isEmpty()) {
                shareMessage.append(mCtx.getString(R.string.hint_location)).append(": ").append(hotel.getLocation()).append("\n");
            }
            
            if (hotel.getFeatures() != null && !hotel.getFeatures().isEmpty()) {
                shareMessage.append(mCtx.getString(R.string.label_features, hotel.getFeatures())).append("\n");
            }
            
            if (hotel.getRating() > 0) {
                shareMessage.append(mCtx.getString(R.string.label_rating, String.valueOf(hotel.getRating()))).append("\n");
            }

            if (hotel.getPricePerNight() > 0) {
                shareMessage.append("Price: $").append(hotel.getPricePerNight()).append(" per night");
            }

            intent.putExtra(Intent.EXTRA_SUBJECT, mCtx.getString(R.string.share_house_title));
            intent.putExtra(Intent.EXTRA_TEXT, shareMessage.toString());
            mCtx.startActivity(Intent.createChooser(intent, mCtx.getString(R.string.share_via)));
        });

        // Hide buttons for own posts
        if (hotel.getOwnerUsername() != null && hotel.getOwnerUsername().equals(CurrentUser.username)) {
            holder.blockButton.setVisibility(View.GONE);
            // Also might want to hide contact button for self?
            holder.contactButton.setVisibility(View.GONE);
        } else {
            holder.blockButton.setVisibility(View.VISIBLE);
            holder.contactButton.setVisibility(View.VISIBLE);
        }

        holder.blockButton.setOnClickListener(v -> {
            AppDatabase db = AppDatabase.getInstance(mCtx);
            new Thread(() -> {
                db.blockedDao().insert(new BlockedEntity(CurrentUser.username, hotel.getOwnerUsername(), hotel.getId()));
                if (mCtx instanceof android.app.Activity) {
                    ((android.app.Activity) mCtx).runOnUiThread(() -> {
                        Toast.makeText(mCtx, R.string.toast_content_blocked, Toast.LENGTH_SHORT).show();
                        int pos = holder.getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            HotelView removed = hotelList.remove(pos);
                            hotelListFull.remove(removed);
                            notifyItemRemoved(pos);
                        }
                    });
                }
            }).start();
        });

        holder.cancelButton.setOnClickListener(v -> {
            AppDatabase db = AppDatabase.getInstance(mCtx);
            new Thread(() -> {
                BookingEntity booking = db.bookingDao().getSpecificBooking(CurrentUser.username, hotel.getId());
                if (booking != null) {
                    db.bookingDao().deleteBooking(booking);
                    if (mCtx instanceof android.app.Activity) {
                        ((android.app.Activity) mCtx).runOnUiThread(() -> {
                            Toast.makeText(mCtx, R.string.toast_booking_cancelled, Toast.LENGTH_SHORT).show();
                            int pos = holder.getAdapterPosition();
                            if (pos != RecyclerView.NO_POSITION) {
                                HotelView removed = hotelList.remove(pos);
                                hotelListFull.remove(removed);
                                notifyItemRemoved(pos);
                            }
                        });
                    }
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

                if (filterPattern.startsWith("rating:")) {
                    try {
                        int minRating = Integer.parseInt(filterPattern.replace("rating:", "").trim());
                        for (HotelView item : hotelListFull) {
                            if (item.getRating() >= minRating) {
                                filteredList.add(item);
                            }
                        }
                    } catch (NumberFormatException e) {
                        filteredList.addAll(hotelListFull);
                    }
                } else if (filterPattern.startsWith("price<")) {
                    try {
                        double maxPrice = Double.parseDouble(filterPattern.replace("price<", "").trim());
                        for (HotelView item : hotelListFull) {
                            if (item.getPricePerNight() <= maxPrice) {
                                filteredList.add(item);
                            }
                        }
                    } catch (NumberFormatException e) {
                        filteredList.addAll(hotelListFull);
                    }
                } else if (filterPattern.startsWith("price>")) {
                    try {
                        double minPrice = Double.parseDouble(filterPattern.replace("price>", "").trim());
                        for (HotelView item : hotelListFull) {
                            if (item.getPricePerNight() >= minPrice) {
                                filteredList.add(item);
                            }
                        }
                    } catch (NumberFormatException e) {
                        filteredList.addAll(hotelListFull);
                    }
                } else if (filterPattern.startsWith("location:")) {
                    String locQuery = filterPattern.replace("location:", "").trim();
                    for (HotelView item : hotelListFull) {
                        if (item.getLocation() != null && item.getLocation().toLowerCase().contains(locQuery)) {
                            filteredList.add(item);
                        }
                    }
                } else if (filterPattern.startsWith("feature:")) {
                    String featQuery = filterPattern.replace("feature:", "").trim();
                    for (HotelView item : hotelListFull) {
                        if (item.getFeatures() != null && item.getFeatures().toLowerCase().contains(featQuery)) {
                            filteredList.add(item);
                        }
                    }
                } else if (filterPattern.startsWith("status:")) {
                    String statusQuery = filterPattern.replace("status:", "").trim();
                    for (HotelView item : hotelListFull) {
                        if (item.getStatus() != null && item.getStatus().toLowerCase().contains(statusQuery)) {
                            filteredList.add(item);
                        }
                    }
                } else if (filterPattern.startsWith("owner:")) {
                    String ownerQuery = filterPattern.replace("owner:", "").trim();
                    for (HotelView item : hotelListFull) {
                        if (item.getOwnerUsername() != null && item.getOwnerUsername().toLowerCase().equals(ownerQuery)) {
                            filteredList.add(item);
                        }
                    }
                } else if (filterPattern.equals("booked")) {
                    for (HotelView item : hotelListFull) {
                        if (item.isBooked()) {
                            filteredList.add(item);
                        }
                    }
                } else if (filterPattern.equals("available")) {
                    for (HotelView item : hotelListFull) {
                        if (!item.isBooked()) {
                            filteredList.add(item);
                        }
                    }
                } else if (filterPattern.equals("sort:price")) {
                    filteredList.addAll(hotelListFull);
                    java.util.Collections.sort(filteredList, (h1, h2) -> Double.compare(h1.getPricePerNight(), h2.getPricePerNight()));
                } else if (filterPattern.equals("sort:rating")) {
                    filteredList.addAll(hotelListFull);
                    java.util.Collections.sort(filteredList, (h1, h2) -> Integer.compare(h2.getRating(), h1.getRating()));
                } else {
                    for (HotelView item : hotelListFull) {
                        if (item.getName().toLowerCase().contains(filterPattern) ||
                            item.getLocation().toLowerCase().contains(filterPattern) ||
                            item.getFeatures().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
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
            if (results.values != null) {
                hotelList.addAll((List) results.values);
            }
            notifyDataSetChanged();
        }
    };
}
