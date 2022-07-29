package com.nlu.admin_food_selling_app.ui.view.customer.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nlu.admin_food_selling_app.R;
import com.nlu.admin_food_selling_app.data.model.Customer;
import com.nlu.admin_food_selling_app.ui.view.customer_information.activity.CustomerInformationActivity;

import java.util.ArrayList;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.MyViewHolder> {
    static ArrayList<Customer> customerList;

    static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtChar, txtName, txtPhone;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtChar = itemView.findViewById(R.id.cFirstChar);
            txtName = itemView.findViewById(R.id.cName);
            txtPhone = itemView.findViewById(R.id.cPhone);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getLayoutPosition();
            Customer customer = customerList.get(position);
            Intent intent = new Intent(view.getContext(), CustomerInformationActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("customer", customer);
            intent.putExtra("customerBundles", bundle);
            view.getContext().startActivity(intent);
        }
    }

    public CustomerAdapter(ArrayList<Customer> customerList) {
        this.customerList = customerList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txtChar.setText(String.valueOf(customerList.get(position).getName().toCharArray()[0]));
        holder.txtName.setText(customerList.get(position).getName());
        holder.txtPhone.setText(customerList.get(position).getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }
}
