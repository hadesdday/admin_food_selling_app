package com.nlu.admin_food_selling_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    TextView lostPassword;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lostPassword = findViewById(R.id.lostPassword);
        lostPassword.setOnClickListener(view -> lostPasswordActivity());

        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(view -> startHomeActivity());
    }

    public void startHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void lostPasswordActivity() {
        Intent intent = new Intent(this, LostPasswordActivity.class);
        startActivity(intent);
    }
}