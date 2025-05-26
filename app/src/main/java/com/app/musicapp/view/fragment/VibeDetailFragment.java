package com.app.musicapp.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.musicapp.R;
import com.app.musicapp.adapter.VibePagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class VibeDetailFragment extends Fragment {
    private static final String ARG_VIBE_NAME = "vibeName";
    private static final String ARG_VIBE_IMAGE = "vibeImage";

    public static VibeDetailFragment newInstance(String vibeName, int vibeImage) {
        VibeDetailFragment fragment = new VibeDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_VIBE_NAME, vibeName);
        args.putInt(ARG_VIBE_IMAGE, vibeImage);
        fragment.setArguments(args);
        return fragment;
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

        // Đặt tiêu đề và ảnh nền
        TextView vibeTitle = view.findViewById(R.id.vibeTitle);
        vibeTitle.setText(vibeName);

        ViewPager2 viewPager = view.findViewById(R.id.viewPager);
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);

        VibePagerAdapter adapter = new VibePagerAdapter(requireActivity());
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

        return view;
    }

    public void onBackPressed(View view) {
        requireActivity().onBackPressed();
    }
}