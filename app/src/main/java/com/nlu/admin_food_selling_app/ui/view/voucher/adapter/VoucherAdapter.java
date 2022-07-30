package com.nlu.admin_food_selling_app.ui.view.voucher.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nlu.admin_food_selling_app.R;
import com.nlu.admin_food_selling_app.data.model.Voucher;
import com.nlu.admin_food_selling_app.ui.view.voucher.activity.VoucherInformation;

import java.util.ArrayList;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.MyViewHolder> {
    static ArrayList<Voucher> voucherList;
    static String[] status;

    static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtVoucherId, txtVoucherActive;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtVoucherId = itemView.findViewById(R.id.txtVoucherId);
            txtVoucherActive = itemView.findViewById(R.id.txtVoucherActive);
            status = itemView.getResources().getStringArray(R.array.voucher_status_entries);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getLayoutPosition();
            Voucher sale = voucherList.get(position);
            Intent intent = new Intent(view.getContext(), VoucherInformation.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("voucher", sale);
            intent.putExtra("voucherBundles", bundle);
            view.getContext().startActivity(intent);
        }
    }

    public VoucherAdapter(ArrayList<Voucher> voucherList) {
        VoucherAdapter.voucherList = voucherList;
    }

    @NonNull
    @Override
    public VoucherAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VoucherAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.voucher_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherAdapter.MyViewHolder holder, int position) {
        holder.txtVoucherId.setText(voucherList.get(position).getId());
        holder.txtVoucherActive.setText(status[voucherList.get(position).getActive() - 1]);
    }

    @Override
    public int getItemCount() {
        return voucherList.size();
    }
}