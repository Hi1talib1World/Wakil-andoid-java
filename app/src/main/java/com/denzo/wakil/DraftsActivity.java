package com.denzo.wakil;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzo.wakil.Decoration.GridSpacingItemDecoration;
import com.denzo.wakil.Util.CurrentUser;
import com.denzo.wakil.Util.Drafts;
import com.denzo.wakil.Util.Hotel;
import com.denzo.wakil.Util.HotelView;
import com.denzo.wakil.Util.Reader;
import com.denzo.wakil.adapters.HotelsAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DraftsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HotelsAdapter adapter;
    private List<HotelView> hotelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drafts);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Saved for Later");
        }

        recyclerView = findViewById(R.id.recycler_view_drafts);
        adapter = new HotelsAdapter(this, hotelList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, 10, true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareDrafts();
    }

    private void prepareDrafts() {
        int[] cover = {R.drawable.hicon1, R.drawable.hicon2, R.drawable.hicon3, R.drawable.hicon4};
        Random random = new Random();
        ArrayList<Drafts> allDrafts = Reader.getDraftsList(this);
        List<Hotel> allHotels = Reader.getRestaurantList(this);

        hotelList.clear();
        if (allDrafts != null && allHotels != null) {
            for (Drafts d : allDrafts) {
                if (d.getName().equalsIgnoreCase(CurrentUser.username)) {
                    List<Integer> savedIds = d.getId();
                    for (Hotel h : allHotels) {
                        if (savedIds.contains(h.getId())) {
                            int idx = random.nextInt(4);
                            hotelList.add(new HotelView(h.getName(), h.getLocation(), cover[idx], h.getRating(), h.getFeats()));
                        }
                    }
                    break;
                }
            }
        }
        adapter.updateList(hotelList);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
