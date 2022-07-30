package com.nlu.admin_food_selling_app.ui.view.sale.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nlu.admin_food_selling_app.R;
import com.nlu.admin_food_selling_app.data.model.Sale;
import com.nlu.admin_food_selling_app.ui.view.sale.activity.SaleInformation;

import java.util.ArrayList;

public class SaleAdapter extends RecyclerView.Adapter<SaleAdapter.MyViewHolder> {
    static ArrayList<Sale> saleList;

    static String[] foodTypes;
    static String[] status;

    static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtSaleId, txtFoodType, txtEndTime, txtActive;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtSaleId = itemView.findViewById(R.id.txtSaleId);
            txtFoodType = itemView.findViewById(R.id.txtFoodType);
            txtEndTime = itemView.findViewById(R.id.txtEndTime);
            txtActive = itemView.findViewById(R.id.txtActive);

            foodTypes = itemView.getResources().getStringArray(R.array.food_type_entries);
            status = itemView.getResources().getStringArray(R.array.sale_status_entries);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getLayoutPosition();
            Sale sale = saleList.get(position);
            Intent intent = new Intent(view.getContext(), SaleInformation.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("sale", sale);
            intent.putExtra("saleBundles", bundle);
            view.getContext().startActivity(intent);
        }
    }

    public SaleAdapter(ArrayList<Sale> saleList) {
        this.saleList = saleList;
    }

    @NonNull
    @Override
    public SaleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SaleAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.sale_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SaleAdapter.MyViewHolder holder, int position) {
        holder.txtSaleId.setText(Integer.toString(saleList.get(position).getId()));
        holder.txtFoodType.setText(foodTypes[saleList.get(position).getFoodType() - 1]);
        holder.txtEndTime.setText(saleList.get(position).getEndTime());
        holder.txtActive.setText(status[saleList.get(position).getActive() - 1]);
    }

    @Override
    public int getItemCount() {
        return saleList.size();
    }
}