package com.app.musicapp.view.fragment.videfragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.musicapp.R;
import com.app.musicapp.adapter.album.AlbumInVibeAdapter;
import com.app.musicapp.adapter.playlist.PlaylistInVibeAdapter;
import com.app.musicapp.adapter.track.TrackInVibeAdapter;
import com.app.musicapp.model.response.AlbumResponse;
import com.app.musicapp.model.response.PlaylistResponse;
import com.app.musicapp.model.response.TrackResponse;
import com.app.musicapp.view.fragment.album.AlbumPageFragment;
import com.app.musicapp.view.fragment.playlist.PlaylistPageFragment;
import com.app.musicapp.view.fragment.searchpage.VibeDetailFragment;

import java.util.ArrayList;
import java.util.List;

public class AllTabFragment extends Fragment {
    private static final String TAG = "AllTabFragment";
    private RecyclerView recyclerViewTrending;
    private RecyclerView recyclerViewPlaylists;
    private RecyclerView recyclerViewAlbums;
    private AlbumInVibeAdapter albumInVibeAdapter;
    private PlaylistInVibeAdapter playlistInVibeAdapter;
    private TrackInVibeAdapter trackInVibeAdapter;
    private VibeDetailFragment.VibeTabNavigator tabNavigator;

    public AllTabFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_tab, container, false);

        // Initialize RecyclerViews
        recyclerViewTrending = view.findViewById(R.id.recyclerViewTrending);
        recyclerViewTrending.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        recyclerViewPlaylists = view.findViewById(R.id.recyclerViewPlaylists);
        recyclerViewPlaylists.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerViewPlaylists.setHasFixedSize(true);
        recyclerViewPlaylists.setNestedScrollingEnabled(false);

        recyclerViewAlbums = view.findViewById(R.id.recyclerViewAlbums);
        recyclerViewAlbums.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerViewAlbums.setHasFixedSize(true);
        recyclerViewAlbums.setNestedScrollingEnabled(false);

        // lay data tu VibeDetailFragment
        VibeDetailFragment parent = (VibeDetailFragment) getParentFragment();
        Log.d(TAG, "Parent fragment: " + (parent != null ? "Found" : "Null"));
        List<TrackResponse> tracks = parent != null ? parent.getTracks() : new ArrayList<>();
        List<PlaylistResponse> playlists = parent != null ? parent.getPlaylists() : new ArrayList<>();
        List<AlbumResponse> albums = parent != null ? parent.getAlbums() : new ArrayList<>();
        Log.d(TAG, "Data sizes: tracks=" + tracks.size() + ", playlists=" + playlists.size() + ", albums=" + albums.size());

        trackInVibeAdapter = new TrackInVibeAdapter(this, tracks.subList(0, Math.min(4, tracks.size())));
        albumInVibeAdapter = new AlbumInVibeAdapter(getContext(), albums.subList(0, Math.min(4, albums.size())));
        playlistInVibeAdapter = new PlaylistInVibeAdapter(getContext(), playlists.subList(0, Math.min(4, playlists.size())));
        setSizePlaylist(playlists);

        albumInVibeAdapter.setOnAlbumClickListener(album -> {
            Log.d(TAG, "Album clicked: " + album.getAlbumTitle());
            AlbumPageFragment fragment = AlbumPageFragment.newInstance(album);
            if (getActivity() != null) {
                View fragmentContainer = getActivity().findViewById(R.id.fragment_container);
                if (fragmentContainer != null) {
                    Log.d(TAG, "Navigating to fragment: " + fragment.getClass().getSimpleName());
                    fragmentContainer.setVisibility(View.VISIBLE);
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .addToBackStack(null)
                            .commit();
                } else {
                    Log.e(TAG, "FragmentContainer not found in main layout");
                }
            } else {
                Log.e(TAG, "Activity is null, cannot navigate to fragment");
            }
        });

        playlistInVibeAdapter.setOnPlaylistClickListener(playlist -> {
            Log.d(TAG, "Playlist clicked: " + playlist.getTitle());
            PlaylistPageFragment fragment = PlaylistPageFragment.newInstance(playlist);
            if (getActivity() != null) {
                View fragmentContainer = getActivity().findViewById(R.id.fragment_container);
                if (fragmentContainer != null) {
                    Log.d(TAG, "Navigating to fragment: " + fragment.getClass().getSimpleName());
                    fragmentContainer.setVisibility(View.VISIBLE);
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .addToBackStack(null)
                            .commit();
                } else {
                    Log.e(TAG, "FragmentContainer not found in main layout");
                }
            } else {
                Log.e(TAG, "Activity is null, cannot navigate to fragment");
            }
        });

        recyclerViewTrending.setAdapter(trackInVibeAdapter);
        recyclerViewPlaylists.setAdapter(playlistInVibeAdapter);
        recyclerViewAlbums.setAdapter(albumInVibeAdapter);

        TextView seeAllTrending = view.findViewById(R.id.seeAllTrending);
        seeAllTrending.setOnClickListener(v -> navigateToTab(1)); // Tab Trending

        TextView seeAllPlaylists = view.findViewById(R.id.seeAllPlaylists);
        seeAllPlaylists.setOnClickListener(v -> navigateToTab(2)); // Tab Playlists

        TextView seeAllAlbums = view.findViewById(R.id.seeAllAlbums);
        seeAllAlbums.setOnClickListener(v -> navigateToTab(3)); // Tab Albums

        // Set up tabNavigator
        if (parent != null) {
            parent.setTabNavigator(position -> {
                Log.d(TAG, "Navigating to tab position: " + position);
                parent.navigateToTab(position);
            });
            tabNavigator = parent::navigateToTab;
        } else {
            Log.e(TAG, "Parent fragment is not an instance of VibeDetailFragment");
        }

        return view;
    }
    public void updateData(List<TrackResponse> newTracks,
                           List<PlaylistResponse> newPlaylists,
                           List<AlbumResponse> newAlbums) {
        Log.d(TAG, "Updating data: tracks=" + newTracks.size() + ", playlists=" + newPlaylists.size() + ", albums=" + newAlbums.size());
        if (recyclerViewTrending != null && trackInVibeAdapter != null) {
            List<TrackResponse> limitedTracks = newTracks.subList(0, Math.min(4, newTracks.size()));
            trackInVibeAdapter.updateData(limitedTracks);
        }
        if (recyclerViewPlaylists != null && playlistInVibeAdapter != null) {
            List<PlaylistResponse> limitedPlaylists = newPlaylists.subList(0, Math.min(4, newPlaylists.size()));
            playlistInVibeAdapter.updateData(limitedPlaylists);
            setSizePlaylist(limitedPlaylists);
        }
        if (recyclerViewAlbums != null && albumInVibeAdapter != null) {
            List<AlbumResponse> limitedAlbums = newAlbums.subList(0, Math.min(4, newAlbums.size()));
            albumInVibeAdapter.updateData(limitedAlbums);
        }
    }
    private void setSizePlaylist(List<PlaylistResponse> playlists) {
        if (recyclerViewPlaylists == null) return;

        ViewGroup.LayoutParams params = recyclerViewPlaylists.getLayoutParams();
        if (playlists != null && playlists.size() == 2) {
            // Set height = 210dp nếu có đúng 2 item
            params.height = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 210, getResources().getDisplayMetrics());
        } else {
            // Set lại chiều cao mặc định như trong XML
            params.height = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 420, getResources().getDisplayMetrics());
        }
        recyclerViewPlaylists.setLayoutParams(params);
    }

    private void navigateToTab(int position) {
        if (tabNavigator != null) {
            Log.d(TAG, "Navigating to tab position: " + position);
            tabNavigator.navigateToTab(position);
        } else {
            Log.e(TAG, "tabNavigator is null, cannot navigate to tab");
        }
    }
}