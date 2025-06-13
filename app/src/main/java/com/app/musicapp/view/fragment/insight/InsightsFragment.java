package com.app.musicapp.view.fragment.insight;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.app.musicapp.R;
import com.app.musicapp.adapter.InsightPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class InsightsFragment extends Fragment {
    private ImageButton btnBack;
    public static InsightsFragment newInstance(String role) {
        InsightsFragment fragment = new InsightsFragment();
        Bundle args = new Bundle();
        args.putString("role", role);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_insights, container, false);
        String role = getArguments() != null ? getArguments().getString("role", "user") : "user";
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        ViewPager2 viewPager = view.findViewById(R.id.viewPager);
        InsightPagerAdapter adapter = new InsightPagerAdapter(requireActivity(), role);
        viewPager.setAdapter(adapter);
        btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> getActivity().getSupportFragmentManager().popBackStack());
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0: tab.setText("Overview"); break;
                case 1: tab.setText("Tracks"); break;
                case 2: tab.setText("Audience"); break;
            }
        }).attach();
        return view;
    }

}