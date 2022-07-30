package com.nlu.admin_food_selling_app.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Voucher implements Parcelable {
    private String id;
    private double rate;
    private int active;

    public Voucher() {
    }

    public Voucher(String id, double rate, int active) {
        this.id = id;
        this.rate = rate;
        this.active = active;
    }

    protected Voucher(Parcel in) {
        id = in.readString();
        rate = in.readDouble();
        active = in.readInt();
    }

    public static final Creator<Voucher> CREATOR = new Creator<Voucher>() {
        @Override
        public Voucher createFromParcel(Parcel in) {
            return new Voucher(in);
        }

        @Override
        public Voucher[] newArray(int size) {
            return new Voucher[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeDouble(rate);
        parcel.writeInt(active);
    }

    @Override
    public String toString() {
        return "Voucher{" +
                "id='" + id + '\'' +
                ", rate=" + rate +
                ", active=" + active +
                '}';
    }
}
