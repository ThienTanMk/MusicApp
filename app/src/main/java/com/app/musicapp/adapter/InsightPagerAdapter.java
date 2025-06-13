package com.app.musicapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.app.musicapp.view.fragment.insight.AudiencesFragment;
import com.app.musicapp.view.fragment.insight.OverviewFragment;
import com.app.musicapp.view.fragment.insight.TracksFragment;

public class InsightPagerAdapter extends FragmentStateAdapter {
    private final String role;
    public InsightPagerAdapter(@NonNull FragmentActivity fragmentActivity, String role) {
        super(fragmentActivity);
        this.role = role;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return com.app.musicapp.view.fragment.insight.OverviewFragment.newInstance(role);
            case 1: return com.app.musicapp.view.fragment.insight.TracksFragment.newInstance(role);
            case 2: return com.app.musicapp.view.fragment.insight.AudiencesFragment.newInstance(role);
            default: return com.app.musicapp.view.fragment.insight.OverviewFragment.newInstance(role);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}