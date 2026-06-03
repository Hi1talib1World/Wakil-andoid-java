package com.denzo.wakil;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.denzo.wakil.Database.AppDatabase;
import com.denzo.wakil.Database.UserEntity;
import com.denzo.wakil.Util.CurrentUser;

public class SettingsActivity extends AppCompatActivity {

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        db = AppDatabase.getInstance(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        findViewById(R.id.btn_clear_bookings).setOnClickListener(v -> {
            new Thread(() -> {
                db.bookingDao().clearAllBookings(CurrentUser.username);
                runOnUiThread(() -> Toast.makeText(this, "All bookings cleared", Toast.LENGTH_SHORT).show());
            }).start();
        });

        findViewById(R.id.btn_clear_drafts).setOnClickListener(v -> {
            new Thread(() -> {
                db.draftDao().clearAllDrafts(CurrentUser.username);
                runOnUiThread(() -> Toast.makeText(this, "All saved drafts cleared", Toast.LENGTH_SHORT).show());
            }).start();
        });

        EditText etNewPassword = findViewById(R.id.et_new_password);
        findViewById(R.id.btn_update_password).setOnClickListener(v -> {
            String newPass = etNewPassword.getText().toString();
            if (newPass.isEmpty()) {
                Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                UserEntity user = db.userDao().getUserByUsername(CurrentUser.username);
                if (user != null) {
                    user.setPassword(newPass);
                    db.userDao().insertUser(user); // Replace existing
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Password updated", Toast.LENGTH_SHORT).show();
                        etNewPassword.setText("");
                    });
                }
            }).start();
        });

        findViewById(R.id.btn_about).setOnClickListener(v -> {
            Toast.makeText(this, "Wakil App v1.0 - Hotel Reservation System", Toast.LENGTH_LONG).show();
        });
    }
}
