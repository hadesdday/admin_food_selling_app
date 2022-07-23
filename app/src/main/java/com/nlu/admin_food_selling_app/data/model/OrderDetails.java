package com.nlu.admin_food_selling_app.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderDetails implements Parcelable {
    private int foodId;
    private int billId;
    private int amount;

    public OrderDetails() {
    }

    public OrderDetails(int foodId, int billId, int amount) {
        this.foodId = foodId;
        this.billId = billId;
        this.amount = amount;
    }

    protected OrderDetails(Parcel in) {
        foodId = in.readInt();
        billId = in.readInt();
        amount = in.readInt();
    }

    public static final Creator<OrderDetails> CREATOR = new Creator<OrderDetails>() {
        @Override
        public OrderDetails createFromParcel(Parcel in) {
            return new OrderDetails(in);
        }

        @Override
        public OrderDetails[] newArray(int size) {
            return new OrderDetails[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(foodId);
        parcel.writeInt(billId);
        parcel.writeInt(amount);
    }
}
