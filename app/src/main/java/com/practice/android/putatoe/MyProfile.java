package com.practice.android.putatoe;

import static com.practice.android.putatoe.MainActivity.JSON;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyProfile extends AppCompatActivity {
    TextView myProfileButton, logOutButton, saveChangesButton;
    EditText firstNameEditText, lastNameEditText, stateEditText, districtEditText, userIdEditText, usernameEditText;
    ArrayList<String> profileDetails = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        myProfileButton = findViewById(R.id.myProfileButton);
        logOutButton = findViewById(R.id.logOutButton);
        saveChangesButton = findViewById(R.id.saveChangesButton);

        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        stateEditText = findViewById(R.id.stateEditText);
        districtEditText = findViewById(R.id.districtEditText);
        userIdEditText = findViewById(R.id.userIdEditText);
        usernameEditText = findViewById(R.id.usernameEditText);

        myProfileButton.setOnClickListener(v -> recreate());

        logOutButton.setOnClickListener(v -> {
            final Intent intent = new Intent(this, AdminPortalLoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        saveChangesButton.setOnClickListener(v -> {
            boolean isFirstName = !(firstNameEditText.getText().toString().equals(profileDetails.get(0))),
                    isLastName = !(lastNameEditText.getText().toString().equals(profileDetails.get(1)));
            if (isFirstName || isLastName) {
                String payload = "{\"name\":\"" + firstNameEditText.getText() + " " + lastNameEditText.getText() + "\"}";
                new Thread(() -> {
                    try {
                        sendProfileUpdateRequest(payload);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(() -> {
                        Toast.makeText(MyProfile.this, "Details updated successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }).start();
            }
        });

        new Thread(() -> {
            try {
                Log.d("getProfileDetails: ", "before getting: " + profileDetails);
                getProfileDetails();
                Log.d("getProfileDetails: ", "after getting: " + profileDetails);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            runOnUiThread(() -> {
                Log.d("UI THREAD", "profile details: " + profileDetails);
                firstNameEditText.setText(profileDetails.get(0));
                lastNameEditText.setText(profileDetails.get(1));
                stateEditText.setText(profileDetails.get(2));
                districtEditText.setText(profileDetails.get(3));
                userIdEditText.setText(profileDetails.get(4));
                usernameEditText.setText(profileDetails.get(5));
            });
        }).start();

    }
    public void getProfileDetails() throws JSONException {
        try {
            Log.d("getProfileDetails: ", "initialize token: " + AdminPortalLoginActivity.AdminToken);
            String responseData = getProfileData(getString(R.string.managerProfileApiUrl), OrdersManager.AUTH_TOKEN);
            Log.d("getProfileDetails: ", "details of manager profile: " + responseData);

            JSONObject jsonObject = new JSONObject(responseData);

            profileDetails.add(jsonObject.getString("name").split(" ")[0]);
            profileDetails.add(jsonObject.getString("name").split(" ")[1]);
            profileDetails.add(jsonObject.getString("state"));
            profileDetails.add(jsonObject.getString("district"));
            profileDetails.add(jsonObject.getString("id"));
            profileDetails.add(jsonObject.getString("username"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String getProfileData(String url, String token) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .header("Authtoken", token)
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
    public void sendProfileUpdateRequest(String payload) throws JSONException {
        try {
            Log.d("sendProfileUpdateRequest: ", "payload: " + payload);
            String responseData = OrdersManager.getOrdersManagerResponse(getString(R.string.updateProfileApiUrl), payload);

            Log.d("sendProfileUpdateRequest: ", "responseData: " + responseData);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String sendUpdateRequest(String url, String json) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        RequestBody body = RequestBody.create(json, JSON);
        Log.d("sendUpdateRequest: ", "authtoken: " + AdminPortalLoginActivity.AdminToken);
        Request request = new Request.Builder()
                .url(url)
                .header("Authtoken", AdminPortalLoginActivity.AdminToken)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}
