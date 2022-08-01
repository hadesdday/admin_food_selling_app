package com.nlu.admin_food_selling_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nlu.admin_food_selling_app.adapters.OrderAdapter;
import com.nlu.admin_food_selling_app.data.model.Order;

import java.util.ArrayList;

public class SearchResultActivity extends AppCompatActivity {
    ImageButton finishSearch;
    TextView keywordTitle;
    ArrayList<Order> orderList;
    View noSearchDataView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        noSearchDataView = findViewById(R.id.noSearchDataView);

        orderList = new ArrayList<>();

        Intent callerIntent = getIntent();
        Bundle searchBundles = callerIntent.getBundleExtra("searchBundles");
        int keyword = searchBundles.getInt("keyword");

        keywordTitle = findViewById(R.id.searchKeywordTitle);
        keywordTitle.setText("Mã khách hàng : " + String.valueOf(keyword));

        RecyclerView recyclerView = findViewById(R.id.searchResultList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(new OrderAdapter(orderList));

        if (orderList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            noSearchDataView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            noSearchDataView.setVisibility(View.GONE);
        }

        finishSearch = findViewById(R.id.finishOrderDetails);
        finishSearch.setOnClickListener(view -> {
            this.finish();
        });
    }

}