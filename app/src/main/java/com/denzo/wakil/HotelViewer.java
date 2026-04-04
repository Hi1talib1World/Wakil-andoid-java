package com.denzo.wakil;

import static com.smarteist.autoimageslider.IndicatorView.utils.DensityUtils.dpToPx;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzo.wakil.Database.AppDatabase;
import com.denzo.wakil.Database.BookingEntity;
import com.denzo.wakil.Database.DraftEntity;
import com.denzo.wakil.Decoration.GridSpacingItemDecoration;
import com.denzo.wakil.Util.Bookings;
import com.denzo.wakil.Util.CurrentUser;
import com.denzo.wakil.Util.Hotel;
import com.denzo.wakil.Util.HotelView;
import com.denzo.wakil.Util.Reader;
import com.denzo.wakil.adapters.Rec_HotelsAdapter;
import com.denzo.wakil.adapters.SliderAdapter;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HotelViewer extends AppCompatActivity {
    public String hotelName ;
    public List<Hotel> hotels;
    public Hotel hotel = null;
    SliderView sliderView;
    private List<HotelView> hotelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private Rec_HotelsAdapter mAdapter;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_viewer);
        
        db = AppDatabase.getInstance(this);
        
        Intent intent = getIntent();
        hotelName = intent.getStringExtra("hotelname");
        sliderView = findViewById(R.id.imageSlider);

        //Initializing Image Slider
        final SliderAdapter adapter = new SliderAdapter(this);
        adapter.setCount(2);

        sliderView.setSliderAdapter(adapter);

        sliderView.setIndicatorAnimation(IndicatorAnimations.SLIDE); 
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.DRAG_FLAG_GLOBAL);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);

        sliderView.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {
                sliderView.setCurrentPagePosition(position);
            }
        });
        setValues(hotelName,this);

        //Initializing Recommendation RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.rec_recycler_view);
        mAdapter = new Rec_HotelsAdapter(getApplicationContext(),hotelList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        prepareHotelData();
    }

    private List<BookingEntity> getBookedEntities(){
        return db.bookingDao().getBookingsByUser(CurrentUser.username);
    }

    public void prepareHotelData(){
        int[] cover = {R.drawable.hicon1, R.drawable.hicon2, R.drawable.hicon3, R.drawable.hicon4};
        TextView title = findViewById(R.id.View_title);
        TextView feats = findViewById(R.id.View_features);
        String featsStr = feats.getText().toString();
        String[] curr_feats = featsStr.contains(": ") ? featsStr.split(": ")[1].split(" , ") : new String[]{"", ""};

        TextView locs = findViewById(R.id.View_location);
        String locStr = locs.getText().toString();
        String location = locStr.contains(": ") ? locStr.split(": ")[1] : "";
        
        Random random = new Random();
        List<BookingEntity> userBookings = getBookedEntities();
        List<Hotel> allHotels = Reader.getRestaurantList(getApplicationContext());

        hotelList.clear();
        if(userBookings.isEmpty())
        {
            for(Hotel h : allHotels) {
                int idx = random.nextInt(4);
                if(h.getLocation().equalsIgnoreCase(location) && !h.getName().equalsIgnoreCase(title.getText().toString())) {
                    hotelList.add(new HotelView(h.getName(), h.getLocation(), cover[idx], h.getRating(), h.getFeats()));
                }
            }
        } else {
            List<Integer> bookedIds = new ArrayList<>();
            for(BookingEntity be : userBookings) bookedIds.add(be.getHotelId());
            
            for(Hotel h : allHotels) {
                int idx = random.nextInt(4);
                if(!bookedIds.contains(h.getId())) {
                    if((h.getFeatures().contains(curr_feats[0]) || (curr_feats.length > 1 && h.getFeatures().contains(curr_feats[1])))
                            && (!h.getName().equalsIgnoreCase(title.getText().toString()))) {
                        hotelList.add(new HotelView(h.getName(), h.getLocation(), cover[idx], h.getRating(), h.getFeats()));
                    }
                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    public void setValues(String hotelName, Context context){
        TextView viewtitle , viewlocation , viewrating , viewfeature , viewcontact;
        final Button viewbook,viewsave;
        viewtitle = findViewById(R.id.View_title);
        viewlocation = findViewById(R.id.View_location);
        viewrating = findViewById(R.id.View_rating);
        viewfeature = findViewById(R.id.View_features);
        viewcontact = findViewById(R.id.View_contact);
        viewbook = findViewById(R.id.View_book);
        viewsave = findViewById(R.id.View_save);
        
        hotels = Reader.getRestaurantList(context);
        for(Hotel hot : hotels) {
            if(hot.getName().equalsIgnoreCase(hotelName)) {
                hotel = hot;
                break;
            }
        }
        
        if (hotel == null) return;

        viewtitle.setText(hotel.getName());
        viewlocation.setText("Location: "+ hotel.getLocation());
        viewfeature.setText("Features: "+hotel.getFeats());
        viewrating.setText("User Rating: "+hotel.getRating());
        viewcontact.setText("Contact: "+hotel.getContact());

        // Check if already booked
        if (db.bookingDao().getSpecificBooking(CurrentUser.username, hotel.getId()) != null) {
            viewbook.setText("Booked");
        }

        // Check if already saved
        if (db.draftDao().getSpecificDraft(CurrentUser.username, hotel.getId()) != null) {
            viewsave.setText("Saved");
        }

        viewbook.setOnClickListener(v -> {
            if(viewbook.getText().toString().equalsIgnoreCase("book")) {
                db.bookingDao().insertBooking(new BookingEntity(CurrentUser.username, hotel.getId()));
                viewbook.setText("Booked");
                Toast.makeText(this, "Hotel Booked!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Already Booked", Toast.LENGTH_SHORT).show();
            }
        });

        viewsave.setOnClickListener(v -> {
            if(viewsave.getText().toString().equalsIgnoreCase("save")) {
                db.draftDao().insertDraft(new DraftEntity(CurrentUser.username, hotel.getId()));
                viewsave.setText("Saved");
                Toast.makeText(this, "Saved for later!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Already Saved", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_hotel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.show_bookings) {
            // Simplified logic for demo
            Toast.makeText(this, "Check your Bookings in Main Screen", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}
