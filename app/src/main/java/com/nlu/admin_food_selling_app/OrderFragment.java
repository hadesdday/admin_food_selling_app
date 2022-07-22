package com.nlu.admin_food_selling_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nlu.admin_food_selling_app.data.model.Order;

import java.util.ArrayList;

public class OrderFragment extends Fragment {
    ArrayList<Order> orderList;
    ImageView plusAction, searchAction;

    public OrderFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderList = new ArrayList<>();
        createTempList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.orderRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(new OrderAdapter(orderList));

        SwipeRefreshLayout pullToRefresh = v.findViewById(R.id.refreshOrderView);
        pullToRefresh.setOnRefreshListener(() -> {
            //reload data here
            pullToRefresh.setRefreshing(false);
        });

        plusAction = v.findViewById(R.id.plusAction);
        plusAction.setOnClickListener(view -> {
            openAddOrderView();
        });

        searchAction = v.findViewById(R.id.searchAct);
        searchAction.setOnClickListener(view -> {
            openSearchOrderView();
        });
        return v;
    }

    public void openAddOrderView() {
        Intent intent = new Intent(getActivity(), AddNewOrder.class);
        startActivity(intent);
    }

    public void openSearchOrderView() {
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        startActivity(intent);
    }

    private void createTempList() {
        orderList.add(new Order(1, 2, "21/10/2022", 123123, "09876654321", "o nha", "momo", 1));
        orderList.add(new Order(2, 3, "22/10/2022", 223123, "09876654321", "o nha", "momo", 1));
        orderList.add(new Order(3, 4, "23/10/2022", 323123, "09876654321", "o nha", "momo", 1));
        orderList.add(new Order(4, 5, "24/10/2022", 423123, "09876654321", "o nha", "momo", 1));
        orderList.add(new Order(5, 6, "25/10/2022", 53123, "09876654321", "o nha", "momo", 1));
        orderList.add(new Order(6, 7, "26/10/2022", 623123, "09876654321", "o nha", "momo", 1));
        orderList.add(new Order(7, 8, "26/10/2022", 623123, "09876654321", "o nha", "momo", 1));
        orderList.add(new Order(8, 9, "26/10/2022", 623123, "09876654321", "o nha", "momo", 1));
        orderList.add(new Order(9, 10, "26/10/2022", 623123, "09876654321", "o nha", "momo", 1));
        orderList.add(new Order(10, 11, "26/10/2022", 623123, "09876654321", "o nha", "momo", 1));
        orderList.add(new Order(11, 12, "26/10/2022", 623123, "09876654321", "o nha", "momo", 1));
        orderList.add(new Order(12, 13, "26/10/2022", 623123, "09876654321", "o nha", "momo", 1));
        orderList.add(new Order(13, 14, "26/10/2022", 623123, "09876654321", "o nha", "momo", 1));
        orderList.add(new Order(14, 15, "26/10/2022", 623123, "09876654321", "o nha", "momo", 1));
    }
}