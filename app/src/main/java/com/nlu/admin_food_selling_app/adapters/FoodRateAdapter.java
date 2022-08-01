package com.nlu.admin_food_selling_app.adapters;

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

import java.util.ArrayList;

public class FoodRateAdapter extends RecyclerView.Adapter<FoodRateAdapter.FoodRateHolder> {
    Context context;
    ArrayList<FoodRating> foodRatingArrayList;

    public FoodRateAdapter(Context context, ArrayList<FoodRating> foodRatingArrayList) {
        this.context = context;
        this.foodRatingArrayList = foodRatingArrayList;
    }

    @NonNull
    @Override
    public FoodRateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_foodrate, parent, false);
        return new FoodRateHolder(v);
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

        public FoodRateHolder(@NonNull View itemView) {
            super(itemView);
            foodRate = itemView.findViewById(R.id.rate);
            foodComment = itemView.findViewById(R.id.comment);
            delete = itemView.findViewById(R.id.delete);

            delete.setOnClickListener(view -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                builder.setTitle("Xóa bình luận");
                builder.setMessage("Bạn có muốn xóa bình luận đó không");
                builder.setCancelable(true);
                builder.setPositiveButton("Có", (dialogInterface, i) -> {

                });
                builder.setNegativeButton("Không", (dialogInterface, i) -> {
                    dialogInterface.cancel();
                });

                builder.show().create();
            });
        }
    }
}
