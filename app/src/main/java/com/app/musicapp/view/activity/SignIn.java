package com.app.musicapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.musicapp.R;
import com.app.musicapp.api.ApiClient;
import com.app.musicapp.helper.SharedPreferencesManager;
import com.app.musicapp.model.request.LoginRequest;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.LoginResponse;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignIn extends AppCompatActivity {
    private TextInputEditText usernameEditText;
    private TextInputEditText passwordEditText;
    private MaterialButton loginButton;
    private View progressBar;
    private SharedPreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Initialize views
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.progressBar);
        
        // Initialize SharedPreferencesManager
        preferencesManager = SharedPreferencesManager.getInstance(this);

        loginButton.setOnClickListener(v -> performLogin());
    }

    private String extractUserIdFromToken(String token) {
        try {
            // Split the token into parts
            String[] parts = token.split("\\.");
            if (parts.length < 2) return null;

            // Get the payload part (second part)
            String payload = parts[1];
            
            // Decode the payload
            byte[] decodedBytes = Base64.decode(payload, Base64.URL_SAFE);
            String decodedPayload = new String(decodedBytes, StandardCharsets.UTF_8);
            
            // Parse the JSON
            JSONObject jsonPayload = new JSONObject(decodedPayload);
            
            // Extract the sub claim (user_id)
            return jsonPayload.getString("sub");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void performLogin() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Validate input
        if (username.isEmpty()) {
            usernameEditText.setError("Username is required");
            return;
        }
        if (password.isEmpty()) {
            passwordEditText.setError("Password is required");
            return;
        }

        // Show loading indicator
        progressBar.setVisibility(View.VISIBLE);
        loginButton.setEnabled(false);

        // Create login request
        LoginRequest loginRequest = new LoginRequest(username, password);

        // Make API call
        ApiClient.getApiService().login(loginRequest).enqueue(new Callback<ApiResponse<LoginResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<LoginResponse>> call, Response<ApiResponse<LoginResponse>> response) {
                progressBar.setVisibility(View.GONE);
                loginButton.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<LoginResponse> apiResponse = response.body();
                    if (apiResponse.getCode() == 1000) {
                        // Login successful
                        LoginResponse loginData = apiResponse.getData();
                        if (loginData != null && loginData.getToken() != null) {
                            String token = loginData.getToken();
                            // Save token
                            preferencesManager.saveToken(token);
                            
                            // Extract and save user_id from token
                            String userId = extractUserIdFromToken(token);
                            if (userId != null) {
                                preferencesManager.saveUserId(userId);
                                // Navigate to MainActivity
                                Toast.makeText(SignIn.this, "Login successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignIn.this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(SignIn.this, "Failed to extract user information", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(SignIn.this, "Invalid response from server", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        // Login failed with error message
                        Toast.makeText(SignIn.this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Handle error
                    Toast.makeText(SignIn.this, "Login failed. Please try again.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<LoginResponse>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                loginButton.setEnabled(true);
                Toast.makeText(SignIn.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}