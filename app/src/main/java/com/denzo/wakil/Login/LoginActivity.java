package com.denzo.wakil.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.denzo.wakil.MainActivity;
import com.denzo.wakil.R;

public class LoginActivity extends AppCompatActivity {

    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        login = findViewById(R.id.login);
        login.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });
    }
}