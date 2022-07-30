package com.nlu.admin_food_selling_app.ui.view.order_details.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nlu.admin_food_selling_app.R;
import com.nlu.admin_food_selling_app.data.model.OrderDetails;

import java.util.ArrayList;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.MyViewHolder> {
    static ArrayList<OrderDetails> orderDetailsList;

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView odFoodId, odAmount;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            odFoodId = itemView.findViewById(R.id.odFoodId);
            odAmount = itemView.findViewById(R.id.odAmount);
        }
    }

    public OrderDetailsAdapter(ArrayList<OrderDetails> orderDetailsList) {
        OrderDetailsAdapter.orderDetailsList = orderDetailsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_details_rows, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.odFoodId.setText(Integer.toString(orderDetailsList.get(position).getFoodId()));
        holder.odAmount.setText(Integer.toString(orderDetailsList.get(position).getAmount()));
    }

    @Override
    public int getItemCount() {
        return orderDetailsList.size();
    }
}
