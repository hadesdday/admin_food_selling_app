package com.nlu.admin_food_selling_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nlu.admin_food_selling_app.data.model.Order;

import java.util.ArrayList;

public class OrderFragment extends Fragment {
    ArrayList<Order> orderList;

    public OrderFragment() {
    }

    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderList = new ArrayList<>();
        createTempList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.orderRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(new OrderAdapter(orderList));

        return v;
    }

    private void createTempList() {
        orderList.add(new Order(1, "21/10/2022", 123123, "09876654321", "o nha", "momo", 1));
        orderList.add(new Order(2, "22/10/2022", 223123, "09876654321", "o nha", "momo", 1));
        orderList.add(new Order(3, "23/10/2022", 323123, "09876654321", "o nha", "momo", 1));
        orderList.add(new Order(4, "24/10/2022", 423123, "09876654321", "o nha", "momo", 1));
        orderList.add(new Order(5, "25/10/2022", 53123, "09876654321", "o nha", "momo", 1));
        orderList.add(new Order(6, "26/10/2022", 623123, "09876654321", "o nha", "momo", 1));
        orderList.add(new Order(7, "26/10/2022", 623123, "09876654321", "o nha", "momo", 1));
        orderList.add(new Order(8, "26/10/2022", 623123, "09876654321", "o nha", "momo", 1));
        orderList.add(new Order(9, "26/10/2022", 623123, "09876654321", "o nha", "momo", 1));
        orderList.add(new Order(10, "26/10/2022", 623123, "09876654321", "o nha", "momo", 1));
        orderList.add(new Order(11, "26/10/2022", 623123, "09876654321", "o nha", "momo", 1));
        orderList.add(new Order(12, "26/10/2022", 623123, "09876654321", "o nha", "momo", 1));
        orderList.add(new Order(13, "26/10/2022", 623123, "09876654321", "o nha", "momo", 1));
        orderList.add(new Order(14, "26/10/2022", 623123, "09876654321", "o nha", "momo", 1));
    }
}