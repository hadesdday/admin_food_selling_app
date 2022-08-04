package com.nlu.admin_food_selling_app;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.nlu.admin_food_selling_app.data.repository.AuthenticationRepository;

public class LostPasswordActivity extends AppCompatActivity {

    EditText usernameLostPass;
    Button loginButton;
    AuthenticationRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_password);

        repository = new AuthenticationRepository(this);

        usernameLostPass = findViewById(R.id.usernameLostPass);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(view -> {
            String username = usernameLostPass.getText().toString();
            repository.forgotPasswordTask(username);
        });

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F7D716")));
        actionBar.setTitle("Quên mật khẩu");
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}