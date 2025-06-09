package com.app.musicapp.view.fragment.videfragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.musicapp.R;
import com.app.musicapp.adapter.playlist.PlaylistInVibeAdapter;
import com.app.musicapp.model.response.PlaylistResponse;
import com.app.musicapp.view.fragment.playlist.PlaylistPageFragment;
import com.app.musicapp.view.fragment.searchpage.VibeDetailFragment;

import java.util.ArrayList;
import java.util.List;

public class PlaylistsTabFragment extends Fragment {
    private RecyclerView recyclerViewPlaylists;
    private PlaylistInVibeAdapter adapter;

    public PlaylistsTabFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_playlists_tab, container, false);

        recyclerViewPlaylists = view.findViewById(R.id.recyclerViewPlaylists);
        recyclerViewPlaylists.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerViewPlaylists.setHasFixedSize(true);
        recyclerViewPlaylists.setNestedScrollingEnabled(false);

        // Lấy dữ liệu từ VibeDetailFragment
        VibeDetailFragment parent = (VibeDetailFragment) getParentFragment();
        List<PlaylistResponse> playlists = parent != null ? parent.getPlaylists() : new ArrayList<>();

        List<PlaylistResponse> copiedList = new ArrayList<>(playlists.subList(0, Math.min(10, playlists.size())));
        adapter = new PlaylistInVibeAdapter(getContext(), copiedList);

        recyclerViewPlaylists.setAdapter(adapter);
        adapter.setOnPlaylistClickListener(playlist -> {
            PlaylistPageFragment fragment = PlaylistPageFragment.newInstance(playlist);

            if (getActivity() != null) {
                View mainView = requireActivity().findViewById(R.id.main);
                View viewPager = mainView.findViewById(R.id.view_pager);
                View fragmentContainer = mainView.findViewById(R.id.fragment_container);

                viewPager.setVisibility(View.GONE);
                fragmentContainer.setVisibility(View.VISIBLE);

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }
    public void updateData(List<PlaylistResponse> newPlaylists) {
        Log.d("PlaylistsTabFragment", "Updating data: playlists=" + newPlaylists.size());
        if (recyclerViewPlaylists != null && adapter != null) {
            List<PlaylistResponse> limitedPlaylists = new ArrayList<>(newPlaylists.subList(0, Math.min(10, newPlaylists.size())));
            adapter.updateData(limitedPlaylists);
        }
    }
}