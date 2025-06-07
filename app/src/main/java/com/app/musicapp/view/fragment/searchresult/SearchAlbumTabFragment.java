package com.app.musicapp.view.fragment.searchresult;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.app.musicapp.R;
import com.app.musicapp.adapter.AlbumAdapter;
import com.app.musicapp.model.Album;
import com.app.musicapp.model.AlbumResponse;
import com.app.musicapp.view.fragment.album.AlbumPageFragment;

import java.util.*;

public class SearchAlbumTabFragment extends Fragment {

    private static final String ARG_ALBUMS = "albums";
    private List<AlbumResponse> albumResults;

    public static SearchAlbumTabFragment newInstance(List<Album> albums) {
        SearchAlbumTabFragment fragment = new SearchAlbumTabFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ALBUMS, new ArrayList<>(albums));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            albumResults = (List<AlbumResponse>) getArguments().getSerializable(ARG_ALBUMS);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_album_tab, container, false);

        ListView listView = view.findViewById(R.id.list_view_album_results);
        AlbumAdapter albumAdapter = new AlbumAdapter(getContext(), albumResults);
        listView.setAdapter(albumAdapter);

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            AlbumResponse selectedAlbum = albumResults.get(position);
            AlbumPageFragment albumFragment = AlbumPageFragment.newInstance(selectedAlbum);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, albumFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }
}