package com.practice.android.putatoe;

import androidx.annotation.NonNull;

public class OrdersModel {
    String orderId, amount, username, mobile, orderDateTime;
    public OrdersModel(String orderId, String amount, String username, String mobile, String orderDate) {
        this.orderId = orderId;
        this.amount = amount;
        this.username = username;
        this.mobile = mobile;
        this.orderDateTime = orderDate;
    }

    @NonNull
    @Override
    public String toString() {
        return "OrdersModel{" +
                "orderId='" + orderId + '\'' +
                ", amount='" + amount + '\'' +
                ", username='" + username + '\'' +
                ", mobile='" + mobile + '\'' +
                ", orderDate='" + orderDateTime + '\'' +
                '}';
    }
}
