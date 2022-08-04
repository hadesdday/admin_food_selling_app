package com.nlu.admin_food_selling_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.nlu.admin_food_selling_app.data.repository.AuthenticationRepository;

public class MainActivity extends AppCompatActivity {
    TextView lostPassword;
    Button loginButton;
    EditText username, passwordLogin;
    AuthenticationRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        repository = new AuthenticationRepository(this);

        username = findViewById(R.id.username);
        passwordLogin = findViewById(R.id.passwordLogin);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        lostPassword = findViewById(R.id.lostPassword);
        lostPassword.setOnClickListener(view -> lostPasswordActivity());

        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(view -> {
            String uname = username.getText().toString();
            String pass = passwordLogin.getText().toString();
            repository.loginTask(uname, pass);
        });
    }

    public void lostPasswordActivity() {
        Intent intent = new Intent(this, LostPasswordActivity.class);
        startActivity(intent);
    }
}