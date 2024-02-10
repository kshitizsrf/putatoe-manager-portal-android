package com.practice.android.putatoe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecyclerProductsAdaptor extends RecyclerView.Adapter<ProductViewHolder> {
    List<ProductsModel> vegetables = Collections.emptyList();
    Context context;

    RecyclerProductsAdaptor(Context context, ArrayList<ProductsModel> vegetables) {
        this.context = context;
        this.vegetables = vegetables;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.vegetables_row, parent, false);
        return new ProductViewHolder(row);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int index) {
        String imgUrl = vegetables.get(index).imageUrl;
        if(imgUrl != null) Picasso.get().load(imgUrl).into(holder.imageView);

        holder.brandName.setText(vegetables.get(index).brandName);
        holder.itemName.setText(vegetables.get(index).itemName);
        holder.itemPrice.setText("₹ " + vegetables.get(index).itemPrice);
        holder.itemType.setText(vegetables.get(index).itemType);
        holder.isStock.setText(vegetables.get(index).isStock ? "In Stock" : "Out of Stock!");
    }

    @Override
    public int getItemCount() {
        return vegetables.size();
    }
}
class ProductViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    View view;
    TextView brandName, itemName, itemType, itemPrice, isStock;
    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.productImageView);
        brandName = itemView.findViewById(R.id.brandNameTextView);
        itemName = itemView.findViewById(R.id.itemNameTextView);
        itemType = itemView.findViewById(R.id.itemTypeTextView);
        itemPrice = itemView.findViewById(R.id.itemPriceTextView);
        isStock = itemView.findViewById(R.id.inStockTextView);
        view  = itemView;
    }
}