package com.denzo.wakil.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.denzo.wakil.Database.AppDatabase;
import com.denzo.wakil.Database.BookingEntity;
import com.denzo.wakil.Database.DraftEntity;
import com.denzo.wakil.Decoration.GridSpacingItemDecoration;
import com.denzo.wakil.ui.auth.LoginActivity;
import com.denzo.wakil.Util.CurrentUser;
import com.denzo.wakil.Util.Hotel;
import com.denzo.wakil.Util.HotelView;
import com.denzo.wakil.Util.Reader;
import com.denzo.wakil.adapters.HotelsAdapter;
import com.denzo.wakil.viewmodel.MainViewModel;
import com.denzo.wakil.ui.home.AddPostActivity;
import com.denzo.wakil.ui.profile.SettingsActivity;
import com.denzo.wakil.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.slider.RangeSlider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HotelsAdapter adapter;
    private Activity context;

    private List<HotelView> hotelList;
    private String username;
    private AppBarLayout appBarLayout;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

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

        viewModel.getHotels().observe(this, hotels -> {
            hotelList.clear();
            hotelList.addAll(hotels);
            adapter.updateList(hotelList);
        });

        ChipGroup filterChipGroup = findViewById(R.id.filter_chip_group);
        filterChipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                viewModel.filterHotels("All");
                return;
            }
            int checkedId = checkedIds.get(0);
            if (checkedId == R.id.chip_all) {
                viewModel.filterHotels("All");
            } else if (checkedId == R.id.chip_high_rating) {
                viewModel.filterHotels("High Rating");
            } else if (checkedId == R.id.chip_budget) {
                viewModel.filterHotels("Budget");
            } else if (checkedId == R.id.chip_luxury) {
                viewModel.filterHotels("Luxury");
            }
        });

        RangeSlider priceSlider = findViewById(R.id.price_range_slider);
        TextView tvPriceRange = findViewById(R.id.tv_price_range);
        priceSlider.setValues(0f, 500f);
        priceSlider.addOnChangeListener((slider, value, fromUser) -> {
            List<Float> values = slider.getValues();
            float min = values.get(0);
            float max = values.get(1);
            tvPriceRange.setText(String.format(java.util.Locale.US, "Price Range: $%.0f - $%.0f", min, max));
            viewModel.filterHotelsByPrice(min, max);
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                appBarLayout.setExpanded(true);
                findViewById(R.id.filter_container).setVisibility(View.VISIBLE);
                findViewById(R.id.price_filter_container).setVisibility(View.VISIBLE);
                viewModel.loadHotels(username);
                return true;
            } else if (id == R.id.nav_bookings) {
                appBarLayout.setExpanded(false);
                findViewById(R.id.filter_container).setVisibility(View.GONE);
                findViewById(R.id.price_filter_container).setVisibility(View.GONE);
                viewModel.loadMyBookings(username);
                return true;
            } else if (id == R.id.nav_saved) {
                appBarLayout.setExpanded(false);
                findViewById(R.id.filter_container).setVisibility(View.GONE);
                findViewById(R.id.price_filter_container).setVisibility(View.GONE);
                showSavedDrafts();
                return true;
            }
            return false;
        });

        viewModel.loadHotels(username);

        com.google.android.material.floatingactionbutton.FloatingActionButton fabAdd = findViewById(R.id.fab_add_post);
        fabAdd.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddPostActivity.class));
        });

        try {
            Glide.with(this).load(R.drawable.back).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        if (bottomNav.getSelectedItemId() == R.id.nav_home) {
            viewModel.loadHotels(username);
        }
    }

    private void showSavedDrafts() {
        AppDatabase db = AppDatabase.getInstance(this);
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
        } else if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
