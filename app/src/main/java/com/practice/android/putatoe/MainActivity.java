package com.practice.android.putatoe;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public static final MediaType JSON = MediaType.get("application/json");
    public static final String AUTH_TOKEN = "Y25SDL0LPPMICE91AY54XVQ92EUVPKWBLTWTHRA2XGE89QGFOF";
    List<SlideModel> banners;
    ArrayList<ProductsModel> vegetables;
    ArrayList<ProductsModel> groceries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageSlider imageSlider = findViewById(R.id.imageSlider);
        RecyclerView vegetableRecyclerView = findViewById(R.id.vegetableRecyclerView);
        RecyclerView groceriesRecyclerView = findViewById(R.id.groceriesRecyclerView);

        vegetableRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        groceriesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        Thread thread = new Thread(() -> {
            try {
                banners = sendBannerRequest();
                runOnUiThread(() -> imageSlider.setImageList(banners));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Thread thread1 = new Thread(() -> {
            try {
                Map<String, ArrayList<ProductsModel>> data = sendProductRequest();

                vegetables = data.get("vegetables");
                groceries = data.get("groceries");

                Log.d("vegetables", "onCreate: " + vegetables);
                Log.d("groceries", "onCreate: " + groceries);

                runOnUiThread(() -> {
                    vegetableRecyclerView.setAdapter(new RecyclerProductsAdaptor(getApplicationContext(), vegetables));
                    groceriesRecyclerView.setAdapter(new RecyclerProductsAdaptor(getApplicationContext(), groceries));
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
        thread1.start();
    }

    public List<SlideModel> sendBannerRequest() throws IOException, JSONException {
        List<SlideModel> banner = new LinkedList<>();
        String body = "{\"subservice_id\": null,\"service_id\": 56}";
        String data = getPostApiResponse(getString(R.string.bannersApiUrl), body);
        JSONArray jsonArray = new JSONObject(data).getJSONArray("Banner details");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            banner.add(new SlideModel((String) jsonObject.get("image"), ScaleTypes.FIT));
        }
        return banner;
    }

    public static String getPostApiResponse(String url, String json) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public Map<String, ArrayList<ProductsModel>> sendProductRequest() throws IOException, JSONException {

        ArrayList<ProductsModel> vegetables = new ArrayList<>();
        ArrayList<ProductsModel> groceries = new ArrayList<>();

        String data = getProductData(getString(R.string.vegetablesApiUrl), AUTH_TOKEN);
        JSONArray jsonArray1 = new JSONObject(data).getJSONObject("popularproduct").getJSONObject( "Fruits & Vegetables").getJSONArray("products");

        for (int i = 0; i < jsonArray1.length(); i++) {
            ProductsModel productsModel = getProductFromObject(jsonArray1.getJSONObject(i));
            vegetables.add(productsModel);
        }
        JSONArray jsonArray2 = new JSONObject(data).getJSONObject("popularproduct").getJSONObject( "Grocery").getJSONArray("products");

        for (int i = 0; i < jsonArray2.length(); i++) {
            ProductsModel productsModel = getProductFromObject(jsonArray2.getJSONObject(i));
            groceries.add(productsModel);
        }
        Map<String, ArrayList<ProductsModel>> mainData = new HashMap<>();

        mainData.put("vegetables", vegetables);
        mainData.put("groceries", groceries);

        return mainData;
    }

    public ProductsModel getProductFromObject(JSONObject jsonObject) throws JSONException {

        String img = jsonObject.getString("logo");
        String brandName = jsonObject.getString("bran_name");
        String itemName = jsonObject.getString("name");
        String itemType = jsonObject.getString("product_type");

        int itemPrice = jsonObject.getInt("price");
        boolean isStock = jsonObject.getJSONArray("qty_list").length() > 0;

        return new ProductsModel(img, brandName, itemName, itemType, itemPrice, isStock);
    }

    public static String getProductData(String url, String token) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("Authtoken", token)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}