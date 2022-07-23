package com.nlu.admin_food_selling_app.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Order implements Parcelable {
    private int id;
    private int customerId;
    private String date;
    private double totalPrice;
    private String phoneNumber;
    private String address;
    private String paymentMethod;
    private int status;

    public Order() {
    }

    public Order(int id, int customerId, String date, double totalPrice, String phoneNumber, String address, String paymentMethod, int status) {
        this.id = id;
        this.customerId = customerId;
        this.date = date;
        this.totalPrice = totalPrice;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.paymentMethod = paymentMethod;
        this.status = status;
    }

    protected Order(Parcel in) {
        id = in.readInt();
        customerId = in.readInt();
        date = in.readString();
        totalPrice = in.readDouble();
        phoneNumber = in.readString();
        address = in.readString();
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", date='" + date + '\'' +
                ", totalPrice=" + totalPrice +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", status=" + status +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(customerId);
        parcel.writeString(date);
        parcel.writeDouble(totalPrice);
        parcel.writeString(phoneNumber);
        parcel.writeString(address);
        parcel.writeString(paymentMethod);
        parcel.writeInt(status);
    }
}
