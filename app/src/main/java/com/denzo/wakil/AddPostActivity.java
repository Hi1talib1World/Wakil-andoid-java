package com.denzo.wakil;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.denzo.wakil.Database.AppDatabase;
import com.denzo.wakil.Database.HousePostEntity;
import com.denzo.wakil.Util.CurrentUser;

public class AddPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        EditText etTitle = findViewById(R.id.et_title);
        EditText etLocation = findViewById(R.id.et_location);
        EditText etContact = findViewById(R.id.et_contact);
        EditText etFeatures = findViewById(R.id.et_features);
        Button btnPost = findViewById(R.id.btn_post);

        btnPost.setOnClickListener(v -> {
            String title = etTitle.getText().toString();
            String location = etLocation.getText().toString();
            String contact = etContact.getText().toString();
            String features = etFeatures.getText().toString();

            if (title.isEmpty() || location.isEmpty() || contact.isEmpty()) {
                Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
                return;
            }

            HousePostEntity post = new HousePostEntity(
                    CurrentUser.username,
                    title,
                    location,
                    contact,
                    features,
                    5, // Default rating
                    R.drawable.hicon1 // Default thumbnail
            );

            new Thread(() -> {
                AppDatabase.getInstance(this).housePostDao().insert(post);
                runOnUiThread(() -> {
                    Toast.makeText(this, R.string.toast_post_success, Toast.LENGTH_SHORT).show();
                    finish();
                });
            }).start();
        });
    }
}
