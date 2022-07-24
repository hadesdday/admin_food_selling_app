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
    View noDataView;

    public OrderFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order, container, false);

        noDataView = v.findViewById(R.id.noDataView);
        RecyclerView recyclerView = v.findViewById(R.id.orderRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        OrderAdapter adapter = new OrderAdapter(orderList);
        recyclerView.setAdapter(adapter);
        if (orderList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            noDataView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            noDataView.setVisibility(View.GONE);
        }
        SwipeRefreshLayout pullToRefresh = v.findViewById(R.id.refreshOrderView);
        pullToRefresh.setOnRefreshListener(() -> {
            //reload data here
            orderList = new ArrayList<>();
            adapter.notifyDataSetChanged();
//            recyclerView.notify();
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
}