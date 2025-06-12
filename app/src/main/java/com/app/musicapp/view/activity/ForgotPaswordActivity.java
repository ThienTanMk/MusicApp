package com.app.musicapp.view.activity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.app.musicapp.R;
import com.app.musicapp.api.ApiClient;
import com.app.musicapp.model.request.ConfirmOtpRequest;
import com.app.musicapp.model.request.EmailRequest;
import com.app.musicapp.model.response.ApiResponse;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPaswordActivity extends AppCompatActivity {

    TextInputEditText edittextEmail, editTextOtp;
    Boolean isClicked = false;
    Button buttonContinue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_pasword);
        initView();

    }
    private void confirmView(){
        isClicked = false;
        editTextOtp.setVisibility(View.VISIBLE);
        edittextEmail.setEnabled(false);
        buttonContinue.setText("Confirm");
        buttonContinue.setOnClickListener(v->{
            if(isClicked)return;
            isClicked = true;
            String otp = editTextOtp.getText().toString();
            String email = edittextEmail.getText().toString();
            if(otp.isEmpty()){
                editTextOtp.setError("OTP is required");
                return;
            }
            if(otp.length()!=6){
                editTextOtp.setError("OTP is invalid");
                return;
            }
            ConfirmOtpRequest confirmOtpRequest = new ConfirmOtpRequest(email,otp);
            ApiClient.getUserService().confirmOtp(confirmOtpRequest).enqueue(new Callback<ApiResponse<Void>>() {
                @Override
                public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                    isClicked = false;
                    if(response.isSuccessful()){
                        Toast.makeText(ForgotPaswordActivity.this, "Confirm successfully. Check your email.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else{
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String message = jsonObject.getString("message");
                            int code = jsonObject.getInt("code");
                            if(code>100&&code<200)
                                editTextOtp.setError(message);
                        }
                        catch (Exception ex){
                            editTextOtp.setError("OTP is invalid");

                        }
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                    isClicked = false;

                }
            });
        });

    }
    private void initView(){
        edittextEmail = findViewById(R.id.edittext_email);
        editTextOtp = findViewById(R.id.edittext_otp);
        buttonContinue = findViewById(R.id.button_continue);
        editTextOtp.setVisibility(View.GONE);

        buttonContinue.setOnClickListener(v->{
            if(isClicked)return;
            isClicked = true;
           String email = edittextEmail.getText().toString();
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                edittextEmail.setError("Email is invalid");
                return;
            }
            EmailRequest emailRequest = new EmailRequest(email);
            ApiClient.getUserService().sendOtp(emailRequest).enqueue(new Callback<ApiResponse<Void>>() {
                @Override
                public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                    isClicked = false;
                    if(response.isSuccessful()){
                        confirmView();
                    }else{
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String message = jsonObject.getString("message");
                            int code = jsonObject.getInt("code");
                            if(code>100&&code<200)
                                edittextEmail.setError(message);
                        }
                        catch (Exception ex){
                            edittextEmail.setError("Email is invalid");

                        }
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                    isClicked = false;
                    edittextEmail.setError("Server error");
                }
            });
        });

    }
}