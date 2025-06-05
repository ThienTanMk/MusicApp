package com.app.musicapp.view.fragment.searchpage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.musicapp.R;
import com.app.musicapp.adapter.VibePagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class VibeDetailFragment extends Fragment {
    private static final String ARG_VIBE_NAME = "vibeName";
    private static final String ARG_VIBE_IMAGE = "vibeImage";
    private static final String TAG = "VibeDetailFragment";
    private VibeTabNavigator tabNavigator;
    private ViewPager2 viewPager;

    public static VibeDetailFragment newInstance(String vibeName, int vibeImage) {
        VibeDetailFragment fragment = new VibeDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_VIBE_NAME, vibeName);
        args.putInt(ARG_VIBE_IMAGE, vibeImage);
        fragment.setArguments(args);
        return fragment;
    }
    // Interface để giao tiếp
    public interface VibeTabNavigator {
        void navigateToTab(int position);
    }
    public VibeDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vibe_detail, container, false);

        String vibeName = getArguments().getString(ARG_VIBE_NAME);
        int vibeImage = getArguments().getInt(ARG_VIBE_IMAGE);

        TextView vibeTitle = view.findViewById(R.id.vibeTitle);
        vibeTitle.setText(vibeName);

        RelativeLayout rlBackground = view.findViewById(R.id.rlBackground);
        if (vibeImage != 0) {
            rlBackground.setBackgroundResource(vibeImage);
        } else {
            rlBackground.setBackgroundResource(R.drawable.default_background_vibe); // Ảnh mặc định nếu vibeImage không hợp lệ
        }

        ImageView ivBack = view.findViewById(R.id.ivBack);
        viewPager = view.findViewById(R.id.viewPager);
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);

        VibePagerAdapter adapter = new VibePagerAdapter(this); // Truyền Fragment, không phải Activity
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0: tab.setText("All"); break;
                        case 1: tab.setText("Trending"); break;
                        case 2: tab.setText("Playlists"); break;
                        case 3: tab.setText("Albums"); break;
                    }
                }).attach();

        ivBack.setOnClickListener(v -> requireActivity().onBackPressed());
        return view;
    }

    // Phương thức để AllTabFragment gọi
    public void setTabNavigator(VibeTabNavigator navigator) {
        this.tabNavigator = navigator;
        Log.d(TAG, "tabNavigator set: " + (navigator != null));
    }

    // Phương thức để điều hướng tab
    public void navigateToTab(int position) {
        if (viewPager != null) {
            Log.d(TAG, "Setting ViewPager2 to position: " + position);
            viewPager.setCurrentItem(position, true);
        } else {
            Log.e(TAG, "ViewPager2 is null, cannot navigate to tab");
        }
    }
}