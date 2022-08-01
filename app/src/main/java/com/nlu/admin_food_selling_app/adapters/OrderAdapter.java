package com.nlu.admin_food_selling_app.adapters;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nlu.admin_food_selling_app.OrderDetailsActivity;
import com.nlu.admin_food_selling_app.R;
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
            Intent intent = new Intent(view.getContext(), OrderDetailsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("date", String.valueOf(txtOrderDate.getText()));
            bundle.putParcelable("order", o);
            intent.putExtra("orderBundles", bundle);

            view.getContext().startActivity(intent);
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
