package com.nlu.admin_food_selling_app.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Order implements Parcelable {
    private int id;
    private int customerId;
    private String date;
    private double totalPrice;
    private String paymentMethod;
    private int status;
    private String voucher;

    public Order() {
    }

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    public Order(int customerId, double totalPrice, String paymentMethod, int status,String voucher) {
        this.customerId = customerId;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.voucher = voucher;
    }


    protected Order(Parcel in) {
        id = in.readInt();
        customerId = in.readInt();
        totalPrice = in.readDouble();
        paymentMethod = in.readString();
        status = in.readInt();
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(customerId);
        parcel.writeDouble(totalPrice);
        parcel.writeString(paymentMethod);
        parcel.writeInt(status);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", totalPrice=" + totalPrice +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", status=" + status +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static Creator<Order> getCREATOR() {
        return CREATOR;
    }
}
