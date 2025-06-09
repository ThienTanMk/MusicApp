package com.app.musicapp.adapter.search;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.app.musicapp.model.response.AlbumResponse;
import com.app.musicapp.model.response.PlaylistResponse;
import com.app.musicapp.model.response.TrackResponse;
import com.app.musicapp.view.fragment.videfragment.AlbumsTabFragment;
import com.app.musicapp.view.fragment.videfragment.AllTabFragment;
import com.app.musicapp.view.fragment.videfragment.PlaylistsTabFragment;
import com.app.musicapp.view.fragment.videfragment.TrendingTabFragment;

import java.util.List;

public class VibePagerAdapter extends FragmentStateAdapter {
    private static final String TAG = "VibePagerAdapter";
    private final Fragment parentFragment;
    public VibePagerAdapter(Fragment parentFragment) {
        super(parentFragment);
        this.parentFragment = parentFragment;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new AllTabFragment();
                break;
            case 1:
                fragment = new TrendingTabFragment();
                break;
            case 2:
                fragment = new PlaylistsTabFragment();
                break;
            case 3:
                fragment = new AlbumsTabFragment();
                break;
            default:
                fragment = new AllTabFragment();
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
