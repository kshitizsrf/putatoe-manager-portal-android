package com.practice.android.putatoe;

import androidx.annotation.NonNull;

public class ProductsModel {
    String imageUrl, brandName, itemName, itemType; int itemPrice; boolean isStock;

    public ProductsModel(String imageUrl, String brandName, String itemName, String itemType, int itemPrice, boolean isStock) {
        this.imageUrl = imageUrl;
        this.brandName = brandName;
        this.itemName = itemName;
        this.itemType = itemType;
        this.itemPrice = itemPrice;
        this.isStock = isStock;
    }


    @NonNull
    @Override
    public String toString() {
        return "ProductsModel{" +
                "imageUrl='" + imageUrl + '\'' +
                ", brandName='" + brandName + '\'' +
                ", itemName='" + itemName + '\'' +
                ", itemType='" + itemType + '\'' +
                ", itemPrice=" + itemPrice +
                ", isStock=" + isStock +
                '}';
    }
}
