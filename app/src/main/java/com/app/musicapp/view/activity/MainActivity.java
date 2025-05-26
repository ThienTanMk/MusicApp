package com.app.musicapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.app.musicapp.R;
import com.app.musicapp.adapter.HomePageAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    ViewPager mViewpager;
    BottomNavigationView mBottomNavigationView;
    private List<Fragment> fragments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        mViewpager = findViewById(R.id.view_pager);
        mBottomNavigationView = findViewById(R.id.bottom_navigation);

        HomePageAdapter viewpagerAdater = new HomePageAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewpager.setAdapter(viewpagerAdater);
        mViewpager.setCurrentItem(0);
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mBottomNavigationView.getMenu().findItem(R.id.home).setChecked(true);
                        break;
                    case 1:
                        mBottomNavigationView.getMenu().findItem(R.id.search).setChecked(true);
                        break;
                    case 2:
                        mBottomNavigationView.getMenu().findItem(R.id.library).setChecked(true);
                        break;

                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Kiểm tra nếu fragment_container đang hiển thị
                View fragmentContainer = findViewById(R.id.fragment_container);
                if (fragmentContainer.getVisibility() == View.VISIBLE) {
                    // Hiển thị lại ViewPager và ẩn fragment_container
                    mViewpager.setVisibility(View.VISIBLE);
                    fragmentContainer.setVisibility(View.GONE);
                    // Xóa fragment trong fragment_container
                    getSupportFragmentManager().popBackStack();
                }

                switch (item.getItemId()) {
                    case R.id.home:
                        mViewpager.setCurrentItem(0);
                        break;
                    case R.id.search:
                        mViewpager.setCurrentItem(1);
                        break;
                    case R.id.library:
                        mViewpager.setCurrentItem(2);
                        break;
                }
                return true;
            }
        });
    }
    // Xử lý khi người dùng bấm nút Back
    @Override
    public void onBackPressed() {
        // Kiểm tra nếu fragment_container đang hiển thị
        View fragmentContainer = findViewById(R.id.fragment_container);
        if (fragmentContainer != null && fragmentContainer.getVisibility() == View.VISIBLE) {
            // Hiển thị lại ViewPager và BottomNavigationView
            mViewpager.setVisibility(View.VISIBLE);
            fragmentContainer.setVisibility(View.GONE);

            // Xóa fragment trong fragment_container
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
    public void setViewPagerPosition(int position) {
        if (mViewpager != null) {
            mViewpager.setCurrentItem(position);
        }
    }
}