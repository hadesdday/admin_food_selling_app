package com.nlu.admin_food_selling_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nlu.admin_food_selling_app.data.model.Order;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {
    static ArrayList<Order> orderList;

    static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtOrderId, txtOrderPrice, txtOrderDate, txtOrderStatus, txtCustomerId, txtPaymentMethod;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderId = itemView.findViewById(R.id.txtOrderId);
            txtOrderPrice = itemView.findViewById(R.id.txtOrderPrice);
            txtOrderDate = itemView.findViewById(R.id.txtOrderDate);
            txtOrderStatus = itemView.findViewById(R.id.txtOrderStatus);
            txtCustomerId = itemView.findViewById(R.id.txtOrderCustomerId);
            txtPaymentMethod = itemView.findViewById(R.id.txtOrderPaymentMethod);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getLayoutPosition();
            Order o = orderList.get(position);
            Toast.makeText(view.getContext(), o.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public OrderAdapter(ArrayList<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txtOrderId.setText(Integer.toString(orderList.get(position).getId()));
        holder.txtOrderPrice.setText(String.valueOf(orderList.get(position).getTotalPrice()));
        holder.txtOrderStatus.setText(Integer.toString(orderList.get(position).getStatus()));
        holder.txtOrderDate.setText(orderList.get(position).getDate());
        holder.txtCustomerId.setText(Integer.toString(orderList.get(position).getCustomerId()));
        holder.txtPaymentMethod.setText(orderList.get(position).getPaymentMethod());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
