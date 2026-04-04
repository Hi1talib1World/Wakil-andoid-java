package com.denzo.wakil;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.denzo.wakil.Database.AppDatabase;
import com.denzo.wakil.Database.BookingEntity;
import com.denzo.wakil.Database.DraftEntity;
import com.denzo.wakil.Decoration.GridSpacingItemDecoration;
import com.denzo.wakil.Login.LoginActivity;
import com.denzo.wakil.Util.CurrentUser;
import com.denzo.wakil.Util.Hotel;
import com.denzo.wakil.Util.HotelView;
import com.denzo.wakil.Util.Reader;
import com.denzo.wakil.adapters.HotelsAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HotelsAdapter adapter;
    private Activity context;

    private List<HotelView> hotelList;
    private String username;
    private AppBarLayout appBarLayout;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        db = AppDatabase.getInstance(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        username = CurrentUser.username;
        appBarLayout = findViewById(R.id.appbar);
        initCollapsingToolbar();

        TextView tvWelcome = findViewById(R.id.tv_welcome);
        if (username != null) {
            tvWelcome.setText(getString(R.string.welcome_user, username));
        }

        recyclerView = findViewById(R.id.recycler_view);
        hotelList = new ArrayList<>();
        adapter = new HotelsAdapter(context, hotelList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                appBarLayout.setExpanded(true);
                prepareHotels();
                return true;
            } else if (id == R.id.nav_bookings) {
                appBarLayout.setExpanded(false);
                showMyBookings();
                return true;
            } else if (id == R.id.nav_saved) {
                appBarLayout.setExpanded(false);
                showSavedDrafts();
                return true;
            }
            return false;
        });

        prepareHotels();

        try {
            Glide.with(this).load(R.drawable.back).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showMyBookings() {
        List<BookingEntity> bookings = db.bookingDao().getBookingsByUser(username);
        List<Hotel> allHotels = Reader.getRestaurantList(this);
        List<HotelView> bookedList = new ArrayList<>();
        int[] cover = {R.drawable.hicon1, R.drawable.hicon2, R.drawable.hicon3, R.drawable.hicon4};
        Random random = new Random();

        if (allHotels != null) {
            List<Integer> bookedIds = new ArrayList<>();
            for (BookingEntity be : bookings) bookedIds.add(be.getHotelId());
            
            for (Hotel h : allHotels) {
                if (bookedIds.contains(h.getId())) {
                    int idx = random.nextInt(4);
                    bookedList.add(new HotelView(h.getName(), h.getLocation(), cover[idx], h.getRating(), h.getFeats()));
                }
            }
        }
        adapter.updateList(bookedList);
        if (bookedList.isEmpty()) {
            Toast.makeText(this, R.string.no_bookings, Toast.LENGTH_SHORT).show();
        }
    }

    private void showSavedDrafts() {
        List<DraftEntity> drafts = db.draftDao().getDraftsByUser(username);
        List<Hotel> allHotels = Reader.getRestaurantList(this);
        List<HotelView> draftsList = new ArrayList<>();
        int[] cover = {R.drawable.hicon1, R.drawable.hicon2, R.drawable.hicon3, R.drawable.hicon4};
        Random random = new Random();

        if (allHotels != null) {
            List<Integer> draftIds = new ArrayList<>();
            for (DraftEntity de : drafts) draftIds.add(de.getHotelId());
            
            for (Hotel h : allHotels) {
                if (draftIds.contains(h.getId())) {
                    int idx = random.nextInt(4);
                    draftsList.add(new HotelView(h.getName(), h.getLocation(), cover[idx], h.getRating(), h.getFeats()));
                }
            }
        }
        adapter.updateList(draftsList);
    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    private void prepareHotels() {
        int[] cover = {R.drawable.hicon1, R.drawable.hicon2, R.drawable.hicon3, R.drawable.hicon4};
        Random random = new Random();
        List<BookingEntity> bookings = db.bookingDao().getBookingsByUser(username);
        List<Hotel> hotels = Reader.getRestaurantList(getApplicationContext());

        hotelList.clear();
        if (hotels != null) {
            List<Integer> bookedIds = new ArrayList<>();
            for (BookingEntity be : bookings) bookedIds.add(be.getHotelId());
            
            for (Hotel h : hotels) {
                int idx = random.nextInt(4);
                if (!bookedIds.contains(h.getId())) {
                    hotelList.add(new HotelView(h.getName(), h.getLocation(), cover[idx], h.getRating(), h.getFeats()));
                }
            }
        }
        adapter.updateList(hotelList);
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_hotel, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            CurrentUser.username = null;
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
