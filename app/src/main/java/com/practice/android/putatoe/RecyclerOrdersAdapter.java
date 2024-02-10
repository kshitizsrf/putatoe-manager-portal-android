package com.practice.android.putatoe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class RecyclerOrdersAdapter extends RecyclerView.Adapter<OrdersCardViewHolder> {
    Context context;
    List<OrdersModel> orders = Collections.emptyList();
    String amount = "Amount: ",
            name = "Username: ",
            mobile = "Mobile Number: ",
            datetime = "Order Date: ";
    public RecyclerOrdersAdapter(Context context, List<OrdersModel> orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrdersCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrdersCardViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_orders, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OrdersCardViewHolder holder, int position) {
        holder.orderIdTextView.setText(orders.get(position).orderId);
        holder.orderAmountTextView.setText(amount + orders.get(position).amount);
        holder.userNameTextView.setText(name + orders.get(position).username);
        holder.mobileTextView.setText(mobile + orders.get(position).mobile);
        holder.orderDateTextView.setText(datetime + orders.get(position).orderDateTime);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }
}
class OrdersCardViewHolder extends RecyclerView.ViewHolder {
    TextView orderIdTextView, orderAmountTextView, userNameTextView, mobileTextView, orderDateTextView;
    public OrdersCardViewHolder(@NonNull View itemView) {
        super(itemView);

        orderIdTextView = itemView.findViewById(R.id.orderIdTextView);
        orderAmountTextView = itemView.findViewById(R.id.orderAmountTextView);
        userNameTextView = itemView.findViewById(R.id.userNameTextView);
        mobileTextView = itemView.findViewById(R.id.mobileTextView);
        orderDateTextView = itemView.findViewById(R.id.orderDateTextView);
    }
}
