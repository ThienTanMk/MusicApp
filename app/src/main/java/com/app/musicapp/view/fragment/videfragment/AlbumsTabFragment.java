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
import com.app.musicapp.adapter.album.AlbumInVibeAdapter;
import com.app.musicapp.model.response.AlbumResponse;
import com.app.musicapp.view.fragment.album.AlbumPageFragment;
import com.app.musicapp.view.fragment.searchpage.VibeDetailFragment;

import java.util.ArrayList;
import java.util.List;

public class AlbumsTabFragment extends Fragment {
    private RecyclerView recyclerViewAlbums;
    private AlbumInVibeAdapter adapter;

    public AlbumsTabFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_albums_tab, container, false);

        recyclerViewAlbums = view.findViewById(R.id.recyclerViewAlbums);
        recyclerViewAlbums.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerViewAlbums.setHasFixedSize(true);
        recyclerViewAlbums.setNestedScrollingEnabled(false);

        // Lấy dữ liệu từ VibeDetailFragment
        VibeDetailFragment parent = (VibeDetailFragment) getParentFragment();
        List<AlbumResponse> albums = parent != null ? parent.getAlbums() : new ArrayList<>();

        List<AlbumResponse> copiedAlbums = new ArrayList<>(albums.subList(0, Math.min(10, albums.size())));
        adapter = new AlbumInVibeAdapter(getContext(), copiedAlbums);

        recyclerViewAlbums.setAdapter(adapter);

        adapter.setOnAlbumClickListener(album -> {
            AlbumPageFragment fragment = AlbumPageFragment.newInstance(album);

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
    public void updateData(List<AlbumResponse> newAlbums) {
        Log.d("AlbumsTabFragment", "Updating data: albums=" + newAlbums.size());
        if (recyclerViewAlbums != null && adapter != null) {
            List<AlbumResponse> limitedAlbums = new ArrayList<>(newAlbums.subList(0, Math.min(10, newAlbums.size())));
            adapter.updateData(limitedAlbums);
        }
    }
}