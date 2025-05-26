package com.app.musicapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.app.musicapp.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
//        // Kiểm tra trạng thái đăng nhập
//        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
//        boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);
//
//        if (isLoggedIn) {
//            // Nếu đã đăng nhập, chuyển thẳng đến MainActivity
//            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        }
    }
    public void signIn(View view) {
        Intent intent = new Intent(SplashActivity.this, SignIn.class);
        startActivity(intent);
    }
}