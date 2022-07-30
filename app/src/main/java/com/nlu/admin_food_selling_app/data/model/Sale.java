package com.nlu.admin_food_selling_app.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Sale implements Parcelable {
    private int id;
    private int foodType;
    private double rate;
    private String endTime;
    private String description;
    private int active;

    public Sale() {
    }

    public Sale(int foodType, double rate, String endTime, String description, int active) {
        this.foodType = foodType;
        this.rate = rate;
        this.endTime = endTime;
        this.description = description;
        this.active = active;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "id=" + id +
                ", foodType=" + foodType +
                ", rate=" + rate +
                ", endTime='" + endTime + '\'' +
                ", description='" + description + '\'' +
                ", active=" + active +
                '}';
    }

    protected Sale(Parcel in) {
        id = in.readInt();
        foodType = in.readInt();
        rate = in.readDouble();
        endTime = in.readString();
        description = in.readString();
        active = in.readInt();
    }

    public static final Creator<Sale> CREATOR = new Creator<Sale>() {
        @Override
        public Sale createFromParcel(Parcel in) {
            return new Sale(in);
        }

        @Override
        public Sale[] newArray(int size) {
            return new Sale[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(foodType);
        parcel.writeDouble(rate);
        parcel.writeString(endTime);
        parcel.writeString(description);
        parcel.writeInt(active);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFoodType() {
        return foodType;
    }

    public void setFoodType(int foodType) {
        this.foodType = foodType;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public static Creator<Sale> getCREATOR() {
        return CREATOR;
    }
}
