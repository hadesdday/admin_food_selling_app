package com.nlu.admin_food_selling_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SearchActivity extends AppCompatActivity {
    ImageView finishSearch;
    TextView applySearch;
    EditText customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        finishSearch = findViewById(R.id.finishOrderDetails);
        applySearch = findViewById(R.id.applySearch);
        customerId = findViewById(R.id.sCustomerId);

        finishSearch.setOnClickListener(view -> {
            this.finish();
        });

        applySearch.setOnClickListener(view -> {
            Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);

            String id = String.valueOf(customerId.getText());

            if (id.isEmpty()) {
                customerId.setError("Vui lòng nhập mã khách hàng");
                customerId.requestFocus();
            } else {
                int keyword = Integer.parseInt(id);
                Bundle bundle = new Bundle();
                bundle.putInt("keyword", keyword);
                intent.putExtra("searchBundles", bundle);
                startActivity(intent);
                SearchActivity.this.finish();
            }
        });
    }

}