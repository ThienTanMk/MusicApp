package com.app.musicapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.app.musicapp.view.fragment.HomePageFragment;
import com.app.musicapp.view.fragment.LibraryPageFragment;
import com.app.musicapp.view.fragment.SearchPageFragment;

public class HomePageAdapter extends FragmentStatePagerAdapter {
    public HomePageAdapter(@NonNull FragmentManager fm, int behavior){
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
               return new HomePageFragment();//homepage
            case 1:
                return new SearchPageFragment();//searchpage
            case 2:
               return new LibraryPageFragment();//librarypage
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
