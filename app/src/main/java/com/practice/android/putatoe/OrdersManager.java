package com.practice.android.putatoe;

import static com.practice.android.putatoe.MainActivity.JSON;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OrdersManager extends AppCompatActivity {
    TextView myProfileButton, logOutButton;
    EditText fromDateEditText, toDateEditText;
    TextView pendingOrders, allotedOrders, ongoingOrders, completedOrders, cancelledOrders, finalCancelledOrders;
    RecyclerView orderCardsRecyclerView;
    ArrayList<OrdersModel> ordersModels = new ArrayList<>();
    int clicked = 1;
    public static final String AUTH_TOKEN = "L4V2IU9IK65KRESGI57YOHPYEO274ZAWCQ49ZB0OOP6JZ9GGA1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_manager);

        myProfileButton = findViewById(R.id.myProfileButton);
        logOutButton = findViewById(R.id.logOutButton);

        fromDateEditText = findViewById(R.id.fromDateEditText);
        toDateEditText = findViewById(R.id.toDateEditText);

        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        Date oneWeekAgo = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String startDate = dateFormat.format(oneWeekAgo),
                endDate = dateFormat.format(today);

        fromDateEditText.setText(startDate);
        toDateEditText.setText(endDate);

        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        fromDateEditText.setOnClickListener(v -> new DatePickerDialog(
                OrdersManager.this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    // Set the selected date on the corresponding EditText
                    String inputDate = year + "-" + month + 1 + "-" + dayOfMonth;
                    fromDateEditText.setText(inputDate);
                },
                currentYear,
                currentMonth,
                currentDayOfMonth
        ).show());

        toDateEditText.setOnClickListener(v -> new DatePickerDialog(
                OrdersManager.this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    // Set the selected date on the corresponding EditText
                    String inputDate = year + "-" + month + 1 + "-" + dayOfMonth;
                    toDateEditText.setText(inputDate);
                },
                currentYear,
                currentMonth,
                currentDayOfMonth
        ).show());

        pendingOrders = findViewById(R.id.pendingOrdersButton);
        allotedOrders = findViewById(R.id.allotedOrdersButton);
        ongoingOrders = findViewById(R.id.ongoingOrdersButton);
        completedOrders = findViewById(R.id.completedOrdersButton);
        cancelledOrders = findViewById(R.id.cancelledOrdersButton);
        finalCancelledOrders = findViewById(R.id.finalCancelledOrdersButton);

        pendingOrders.setOnClickListener(v -> {
            if (clicked != 1) {

                clicked = 1;
                selectCategory(pendingOrders);
                new Thread(() -> {
                    try {
                        ordersModels.clear();
                        sendOrdersRequest(getString(R.string.pending_orders_), startDate, endDate);
                        runOnUiThread(() -> orderCardsRecyclerView.setAdapter(new RecyclerOrdersAdapter(getApplicationContext(), ordersModels)));
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        });
        allotedOrders.setOnClickListener(v -> {
            if (clicked != 2) {
                clicked = 2;
                selectCategory(allotedOrders);
                new Thread(() -> {
                    try {
                        ordersModels.clear();
                        sendOrdersRequest(getString(R.string.pending_to_serviceprovider_orders), startDate, endDate);
                        runOnUiThread(() -> orderCardsRecyclerView.setAdapter(new RecyclerOrdersAdapter(getApplicationContext(), ordersModels)));
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        });
        ongoingOrders.setOnClickListener(v -> {
            if (clicked != 3) {
                clicked = 3;
                selectCategory(ongoingOrders);
                new Thread(() -> {
                    try {
                        ordersModels.clear();
                        sendOrdersRequest(getString(R.string.ongoing_orders_), startDate, endDate);
                        runOnUiThread(() -> orderCardsRecyclerView.setAdapter(new RecyclerOrdersAdapter(getApplicationContext(), ordersModels)));
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        });
        completedOrders.setOnClickListener(v -> {
            if (clicked != 4) {
                clicked = 4;
                selectCategory(completedOrders);
                new Thread(() -> {
                    try {
                        ordersModels.clear();
                        sendOrdersRequest(getString(R.string.completed_orders_), startDate, endDate);
                        runOnUiThread(() -> orderCardsRecyclerView.setAdapter(new RecyclerOrdersAdapter(getApplicationContext(), ordersModels)));
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        });
        cancelledOrders.setOnClickListener(v -> {
            if (clicked != 5) {
                clicked = 5;
                selectCategory(cancelledOrders);
                new Thread(() -> {
                    try {
                        ordersModels.clear();
                        sendOrdersRequest(getString(R.string.cancelled_orders_), startDate, endDate);
                        runOnUiThread(() -> orderCardsRecyclerView.setAdapter(new RecyclerOrdersAdapter(getApplicationContext(), ordersModels)));
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        });
        finalCancelledOrders.setOnClickListener(v -> {
            if (clicked != 6) {
                clicked = 6;
                selectCategory(finalCancelledOrders);
                new Thread(() -> {
                    try {
                        ordersModels.clear();
                        sendOrdersRequest(getString(R.string.final_cancelled_orders_), startDate, endDate);
                        runOnUiThread(() -> orderCardsRecyclerView.setAdapter(new RecyclerOrdersAdapter(getApplicationContext(), ordersModels)));
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        });

        orderCardsRecyclerView = findViewById(R.id.orderCardsRecyclerView);
        orderCardsRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        myProfileButton.setOnClickListener(v -> startActivity(new Intent(this, MyProfile.class)));
        logOutButton.setOnClickListener(v -> finish());

        new Thread(() -> {
            try {
                sendOrdersRequest(getString(R.string.pending_orders_), startDate, endDate);
                runOnUiThread(() -> {
                    selectCategory(pendingOrders);
                    orderCardsRecyclerView.setAdapter(new RecyclerOrdersAdapter(getApplicationContext(), ordersModels));
                });
            }
            catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void clearCategory(TextView t) {
        t.setBackgroundResource(R.drawable.textview_orders_category);
        t.setTextColor(getColor(R.color.putatoe));
    }

    private void selectCategory(TextView t) {
        clearCategory(pendingOrders);
        clearCategory(allotedOrders);
        clearCategory(ongoingOrders);
        clearCategory(completedOrders);
        clearCategory(cancelledOrders);
        clearCategory(finalCancelledOrders);
        t.setBackgroundResource(R.drawable.textview_onclick);
        t.setTextColor(getColor(R.color.white));
    }

    public void sendOrdersRequest(String typeOfOrder, String startDate, String endDate) throws IOException, JSONException {
        String postBody = "{\"start_date\":\"" + startDate + " 00:00:00\",\"end_date\":\"" + endDate + " 23:59:59\"}";
        Log.d("sendOrdersRequest: ", "payload: " + postBody);

        String data = getOrdersManagerResponse(getString(R.string.ordersManagerApiUrl), postBody);

        Log.d("sendOrdersRequest: ", "data: " + data);
        JSONArray jsonArray = new JSONObject(data).getJSONArray(typeOfOrder);
        Log.d("sendOrdersRequest: ", "jsonArray: " + jsonArray);

        for (int i = 0; i < jsonArray.length(); i++) {
            ordersModels.add(getOrderCardModel(jsonArray.getJSONObject(i)));
        }
        Log.d("sendOrdersRequest: ", "ordersModels: " + ordersModels);
    }

    public OrdersModel getOrderCardModel(JSONObject jsonObject) throws JSONException {
        return new OrdersModel(
                jsonObject.getString("order_id"),
                jsonObject.getString("amt"),
                jsonObject.getString("name"),
                jsonObject.getString("phone"),
                jsonObject.getString("datetime")
        );
    }

    public static String getOrdersManagerResponse(String url, String json) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        RequestBody body = RequestBody.create(json, JSON);

        Log.d("getOrdersManagerResponse: ", "authtoken: " + AdminPortalLoginActivity.AdminToken);
        Request request = new Request.Builder()
                .url(url)
                .header("authtoken", AdminPortalLoginActivity.AdminToken)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}