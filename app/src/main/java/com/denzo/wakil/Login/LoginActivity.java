package com.denzo.wakil.Login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.denzo.wakil.Database.AppDatabase;
import com.denzo.wakil.Database.UserEntity;
import com.denzo.wakil.MainActivity;
import com.denzo.wakil.R;
import com.denzo.wakil.Util.CurrentUser;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvRegister = findViewById(R.id.tv_register);

        btnLogin.setOnClickListener(view -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
            } else {
                AppDatabase db = AppDatabase.getInstance(this);
                UserEntity user = db.userDao().login(username, password);
                
                if (user != null) {
                    CurrentUser.username = username;
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                } else {
                    // Check for default admin
                    if (username.equals("admin") && password.equals("admin")) {
                        CurrentUser.username = username;
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, R.string.invalid_credentials, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        tvRegister.setOnClickListener(view -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }
}
