package com.app.musicapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.app.musicapp.view.fragment.videfragment.AlbumsTabFragment;
import com.app.musicapp.view.fragment.videfragment.AllTabFragment;
import com.app.musicapp.view.fragment.videfragment.PlaylistsTabFragment;
import com.app.musicapp.view.fragment.videfragment.TrendingTabFragment;

public class VibePagerAdapter extends FragmentStateAdapter {
    public VibePagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new AllTabFragment();
            case 1: return new TrendingTabFragment();
            case 2: return new PlaylistsTabFragment();
            case 3: return new AlbumsTabFragment();
            default: return new AllTabFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
