package com.nlu.admin_food_selling_app.ui.view.sale.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.nlu.admin_food_selling_app.R;

public class SearchSale extends AppCompatActivity {
    ImageView finishSearch;
    TextView applySearch;
    Spinner foodType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_sale);

        finishSearch = findViewById(R.id.finishSearchCustomer);
        applySearch = findViewById(R.id.applySearchCustomer);
        foodType = findViewById(R.id.spinnerSearchSaleKey);

        String[] foodTypes = getResources().getStringArray(R.array.food_type_entries);
        ArrayAdapter<String> foodTypeAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, foodTypes);
        foodType.setAdapter(foodTypeAdapter);

        finishSearch.setOnClickListener(view -> {
            this.finish();
        });

        applySearch.setOnClickListener(view -> {
            Intent intent = new Intent(SearchSale.this, SearchSaleResult.class);

            int id = foodType.getSelectedItemPosition();
            Bundle bundle = new Bundle();
            bundle.putInt("id", id);
            intent.putExtra("searchBundles", bundle);
            startActivity(intent);
            SearchSale.this.finish();
        });
    }
}