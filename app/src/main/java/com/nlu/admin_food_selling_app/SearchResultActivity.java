package com.nlu.admin_food_selling_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

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
        createTempList(keyword);

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

        finishSearch = findViewById(R.id.finishSearch);
        finishSearch.setOnClickListener(view -> {
            this.finish();
        });
    }

    private void createTempList(int keyword) {
        addWithCondition(new Order(1, 2, "21/10/2022", 123123, "09876654321", "o nha", "momo", 1), keyword);
        addWithCondition(new Order(2, 3, "22/10/2022", 223123, "09876654321", "o nha", "momo", 1), keyword);
        addWithCondition(new Order(3, 4, "23/10/2022", 323123, "09876654321", "o nha", "momo", 1), keyword);
        addWithCondition(new Order(4, 5, "24/10/2022", 423123, "09876654321", "o nha", "momo", 1), keyword);
        addWithCondition(new Order(5, 6, "25/10/2022", 53123, "09876654321", "o nha", "momo", 1), keyword);
        addWithCondition(new Order(6, 7, "26/10/2022", 623123, "09876654321", "o nha", "momo", 1), keyword);
        addWithCondition(new Order(7, 8, "26/10/2022", 623123, "09876654321", "o nha", "momo", 1), keyword);
        addWithCondition(new Order(8, 2, "26/10/2022", 623123, "09876654321", "o nha", "momo", 1), keyword);
        addWithCondition(new Order(9, 10, "26/10/2022", 623123, "09876654321", "o nha", "momo", 1), keyword);
        addWithCondition(new Order(10, 11, "26/10/2022", 623123, "09876654321", "o nha", "momo", 1), keyword);
        addWithCondition(new Order(11, 12, "26/10/2022", 623123, "09876654321", "o nha", "momo", 1), keyword);
        addWithCondition(new Order(12, 13, "26/10/2022", 623123, "09876654321", "o nha", "momo", 1), keyword);
        addWithCondition(new Order(13, 14, "26/10/2022", 623123, "09876654321", "o nha", "momo", 1), keyword);
        addWithCondition(new Order(14, 15, "26/10/2022", 623123, "09876654321", "o nha", "momo", 1), keyword);
    }

    public void addWithCondition(Order o, int keyword) {
        if (o.getCustomerId() == keyword) {
            orderList.add(o);
        }
    }
}