package com.app.musicapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.app.musicapp.model.Album;
import com.app.musicapp.model.ProfileWithCountFollowResponse;
import com.app.musicapp.model.Track;
import com.app.musicapp.view.fragment.searchresult.SearchAlbumTabFragment;
import com.app.musicapp.view.fragment.searchresult.SearchPlaylistTabFragment;
import com.app.musicapp.view.fragment.searchresult.SearchTrackTabFragment;
import com.app.musicapp.view.fragment.searchresult.SearchUserTabFragment;

import java.util.*;

public class SearchResultPagerAdapter extends FragmentStateAdapter {
    private List<Track> trackResults;
    private List<ProfileWithCountFollowResponse> userResults;
    private List<Object> playlistResults;
    private List<Album> albumResults;

    public SearchResultPagerAdapter(@NonNull Fragment fragment, List<Track> trackResults,
                                    List<ProfileWithCountFollowResponse> userResults,
                                    List<Object> playlistResults, List<Album> albumResults) {
        super(fragment);
        this.trackResults = trackResults;
        this.userResults = userResults;
        this.playlistResults = playlistResults;
        this.albumResults = albumResults;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: // Song
                return SearchTrackTabFragment.newInstance(trackResults);
            case 1: // User
                return SearchUserTabFragment.newInstance(userResults);
            case 2: // Playlist
                return SearchPlaylistTabFragment.newInstance(playlistResults);
            case 3: // Album
                return SearchAlbumTabFragment.newInstance(albumResults);
            default:
                return new Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4; // 4 tabs: Song, User, Playlist, Album
    }

    public void updateData(List<Track> tracks, List<ProfileWithCountFollowResponse> users,
                           List<Object> playlists, List<Album> albums) {
        this.trackResults = tracks;
        this.userResults = users;
        this.playlistResults = playlists;
        this.albumResults = albums;
        notifyDataSetChanged();
    }
}
