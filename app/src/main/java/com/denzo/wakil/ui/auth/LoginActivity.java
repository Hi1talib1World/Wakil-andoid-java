package com.denzo.wakil.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.denzo.wakil.BuildConfig;
import com.denzo.wakil.ui.home.MainActivity;
import com.denzo.wakil.R;
import com.denzo.wakil.Util.CurrentUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin, btnSkip;
    private TextView tvRegister;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvRegister = findViewById(R.id.tv_register);
        btnSkip = findViewById(R.id.btn_skip);

        if (BuildConfig.DEBUG) {
            btnSkip.setVisibility(View.VISIBLE);
            btnSkip.setOnClickListener(view -> {
                CurrentUser.username = "debug_user";
                startActivity(new Intent(this, MainActivity.class));
                finish();
            });
        }

        btnLogin.setOnClickListener(view -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
            } else {
                btnLogin.setEnabled(false);
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, task -> {
                            btnLogin.setEnabled(true);
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    CurrentUser.username = user.getEmail();
                                }
                                startActivity(new Intent(this, MainActivity.class));
                                finish();
                            } else {
                                String error = task.getException() != null ? task.getException().getMessage() : getString(R.string.invalid_credentials);
                                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        tvRegister.setOnClickListener(view -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            CurrentUser.username = currentUser.getEmail();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}
