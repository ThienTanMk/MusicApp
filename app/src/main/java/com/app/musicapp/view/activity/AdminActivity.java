package com.app.musicapp.view.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.app.musicapp.R;
import com.app.musicapp.view.fragment.admin.ManageGenreFragment;
import com.app.musicapp.view.fragment.admin.ManageTagFragment;

public class AdminActivity extends AppCompatActivity {
    private ImageView imageBack;
    private TextView textStatistic, textManageGenre, textManageTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);

        // ÁNH XẠ VIEW
        imageBack = findViewById(R.id.image_back);
        textStatistic = findViewById(R.id.text_statistic);
        textManageGenre = findViewById(R.id.text_manage_genre);
        textManageTag = findViewById(R.id.text_manage_tag);

        // XỬ LÝ NÚT BACK
        imageBack.setOnClickListener(v -> onBackPressed());

        // XỬ LÝ MỞ FRAGMENT KHI CLICK TEXT
        textManageGenre.setOnClickListener(v -> openFragment(new ManageGenreFragment()));
        textManageTag.setOnClickListener(v -> openFragment(new ManageTagFragment()));
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}