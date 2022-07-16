package com.nlu.admin_food_selling_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView lostPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lostPassword = findViewById(R.id.lostPassword);
        lostPassword.setOnClickListener(view -> lostPasswordActivity());
    }


    public void lostPasswordActivity() {
        Intent intent = new Intent(this, LostPasswordActivity.class);
        startActivity(intent);
    }
}