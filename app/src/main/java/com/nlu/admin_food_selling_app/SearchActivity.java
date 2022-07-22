package com.nlu.admin_food_selling_app;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class SearchActivity extends AppCompatActivity {

    ImageView finishSearch;
    TextView applySearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        finishSearch = findViewById(R.id.finishSearch);
        applySearch = findViewById(R.id.applySearch);

        finishSearch.setOnClickListener(view->{
            this.finish();
        });

        applySearch.setOnClickListener(view->{

        });
    }

}