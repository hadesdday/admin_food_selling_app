package com.nlu.admin_food_selling_app.data.model;

import java.io.Serializable;

public class OrderDetails implements Serializable {
    int foodId;
    int billId;
    int amount;

    public OrderDetails() {
    }

    public OrderDetails(int foodId, int billId, int amount) {
        this.foodId = foodId;
        this.billId = billId;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "OrderDetails{" +
                "foodId=" + foodId +
                ", billId=" + billId +
                ", amount=" + amount +
                '}';
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
