package com.nlu.admin_food_selling_app.data.model;

import java.io.Serializable;

public class FoodType implements Serializable {
    int foodTypeId;
    String foodTypeName;

    public FoodType(int foodTypeId, String foodTypeName) {
        this.foodTypeId = foodTypeId;
        this.foodTypeName = foodTypeName;
    }

    public int getFoodTypeId() {
        return foodTypeId;
    }

    public void setFoodTypeId(int foodTypeId) {
        this.foodTypeId = foodTypeId;
    }

    public String getFoodTypeName() {
        return foodTypeName;
    }

    public void setFoodTypeName(String foodTypeName) {
        this.foodTypeName = foodTypeName;
    }

    @Override
    public String toString() {
        return foodTypeId + " - " + foodTypeName;
    }
}
