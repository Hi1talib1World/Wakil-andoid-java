package com.denzo.wakil.ui.booking;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.denzo.wakil.Database.AppDatabase;
import com.denzo.wakil.Database.DraftEntity;
import com.denzo.wakil.Util.CurrentUser;
import com.denzo.wakil.Util.Hotel;
import com.denzo.wakil.Util.HotelView;
import com.denzo.wakil.Util.Reader;
import com.denzo.wakil.adapters.HotelsAdapter;
import com.denzo.wakil.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DraftsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drafts);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        RecyclerView recyclerView = findViewById(R.id.recycler_view_drafts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String username = CurrentUser.username;
        AppDatabase db = AppDatabase.getInstance(this);
        List<DraftEntity> drafts = db.draftDao().getDraftsByUser(username);
        List<Hotel> allHotels = Reader.getRestaurantList(this);
        List<HotelView> draftsList = new ArrayList<>();
        int[] cover = {R.drawable.hicon1, R.drawable.hicon2, R.drawable.hicon3, R.drawable.hicon4};
        Random random = new Random();

        if (allHotels != null) {
            for (DraftEntity de : drafts) {
                for (Hotel h : allHotels) {
                    if (de.getHotelId() == h.getId()) {
                        int idx = random.nextInt(4);
                        draftsList.add(new HotelView(h.getName(), h.getLocation(), cover[idx], h.getRating(), h.getFeats()));
                        break;
                    }
                }
            }
        }

        HotelsAdapter adapter = new HotelsAdapter(this, draftsList);
        recyclerView.setAdapter(adapter);
    }
}
