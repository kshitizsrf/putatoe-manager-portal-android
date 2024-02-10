package com.practice.android.putatoe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AdminPortalLoginActivity extends AppCompatActivity {
    ConstraintLayout mainScreen;
    EditText userIdEditText, passwdEditText;
    CheckBox rememberMeCheckBox;
    Button submit;
    ImageView alertImageView;
    TextView alertTextView;
    public static final MediaType JSON = MediaType.get("application/json");
    String managerApiUrl;
    public static String AdminToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_portal_login);

        mainScreen = findViewById(R.id.managerPortalScreen);
        userIdEditText = findViewById(R.id.userIdEditText);
        passwdEditText = findViewById(R.id.passwordEditText);
        rememberMeCheckBox = findViewById(R.id.rememberMeCheckBox);
        submit = findViewById(R.id.signInButton);
        alertImageView = findViewById(R.id.alertImageView);
        alertTextView = findViewById(R.id.alertTextView);

        managerApiUrl = getString(R.string.managerApiUrl);

        mainScreen.setOnClickListener(v -> {
            if (alertImageView.getVisibility() == View.VISIBLE) {
                alertImageView.setVisibility(View.INVISIBLE);
                alertTextView.setVisibility(View.INVISIBLE);
                Log.d("ALERT VISIBILITY", "alert is invisible");
            }
        });

        submit.setOnClickListener(v -> {
            String userID = String.valueOf(userIdEditText.getText());
            String password = String.valueOf(passwdEditText.getText());

            if (userID.isEmpty()) {
                alertTextView.setText(R.string.enter_userid);
                showAlert();
                return;
            }

            new Thread(() -> {
                try {
                    AdminToken = sendLoginRequest(userID, password);
                }
                catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                runOnUiThread(() -> {
                    if (AdminToken == null) {
                        alertTextView.setText(R.string.invalid_field);
                        showAlert();
                    }
                    else {
                        startActivity(new Intent(AdminPortalLoginActivity.this, OrdersManager.class));
                    }
                });
            }).start();
        });
    }

    private void showAlert() {
        alertImageView.setVisibility(View.VISIBLE);
        alertTextView.setVisibility(View.VISIBLE);
        Log.d("ALERT VISIBILITY", "alert is visible");
    }

    public String sendLoginRequest(String userID, String password) throws IOException, JSONException {

        String requestData = "{\"user_id\": " + userID + ",\"password\": \"" + password + "\"}";
        String responseData = getLoginResponse(managerApiUrl, requestData);
        String token = null;

        try {
            token = new JSONObject(responseData).getString("token");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("Login Request", "Request: " + requestData);
        Log.d("Login Response", "Response: " + responseData);

        return token;
    }

    public String getLoginResponse(String url, String json) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}