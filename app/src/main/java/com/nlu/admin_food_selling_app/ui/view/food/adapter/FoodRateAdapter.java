package com.nlu.admin_food_selling_app.ui.view.food.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nlu.admin_food_selling_app.R;
import com.nlu.admin_food_selling_app.data.model.FoodRating;
import com.nlu.admin_food_selling_app.ui.view.food.activity.FoodRateActivity;

import java.util.ArrayList;

public class FoodRateAdapter extends RecyclerView.Adapter<FoodRateAdapter.FoodRateHolder> {
    Context context;
    ArrayList<FoodRating> foodRatingArrayList;
    OnClickListener listener;

    public FoodRateAdapter(Context context, ArrayList<FoodRating> foodRatingArrayList) {
        this.context = context;
        this.foodRatingArrayList = foodRatingArrayList;
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public FoodRateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_foodrate, parent, false);
        return new FoodRateHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodRateHolder holder, int position) {
        FoodRating rating = foodRatingArrayList.get(position);
        holder.foodRate.setText(String.format("%s", rating.getFoodRate()).concat("★"));
        holder.foodComment.setText(rating.getFoodComment());
    }

    @Override
    public int getItemCount() {
        return foodRatingArrayList.size();
    }

    static class FoodRateHolder extends RecyclerView.ViewHolder {
        TextView foodRate, foodComment;
        ImageButton delete;

        public FoodRateHolder(@NonNull View itemView, OnClickListener listener) {
            super(itemView);
            foodRate = itemView.findViewById(R.id.rate);
            foodComment = itemView.findViewById(R.id.comment);
            delete = itemView.findViewById(R.id.delete);

            delete.setOnClickListener(view -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION)
                        listener.onDelete(position);
                }
            });
        }
    }

    public interface OnClickListener {
        void onDelete(int position);
    }
}
