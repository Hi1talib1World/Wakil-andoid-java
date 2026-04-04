package com.denzo.wakil.Login;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.denzo.wakil.Database.AppDatabase;
import com.denzo.wakil.Database.UserEntity;
import com.denzo.wakil.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnRegister;
    private TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnRegister = findViewById(R.id.btn_register);
        tvLogin = findViewById(R.id.tv_login);

        btnRegister.setOnClickListener(view -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
            } else {
                AppDatabase db = AppDatabase.getInstance(this);
                if (db.userDao().getUserByUsername(username) != null) {
                    Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT).show();
                    return;
                }

                db.userDao().insertUser(new UserEntity(username, password));

                Toast.makeText(this, R.string.registration_success, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        tvLogin.setOnClickListener(view -> {
            finish();
        });
    }
}
