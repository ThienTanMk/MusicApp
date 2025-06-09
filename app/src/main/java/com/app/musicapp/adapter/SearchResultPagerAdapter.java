package com.app.musicapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.app.musicapp.model.AlbumResponse;
import com.app.musicapp.model.response.PlaylistResponse;
import com.app.musicapp.model.response.TrackResponse;
import com.app.musicapp.model.response.ProfileWithCountFollowResponse;
import com.app.musicapp.view.fragment.searchresult.SearchAlbumTabFragment;
import com.app.musicapp.view.fragment.searchresult.SearchPlaylistTabFragment;
import com.app.musicapp.view.fragment.searchresult.SearchTrackTabFragment;
import com.app.musicapp.view.fragment.searchresult.SearchUserTabFragment;

import java.util.*;

public class SearchResultPagerAdapter extends FragmentStateAdapter {
    private List<TrackResponse> trackResponseResults;
    private List<ProfileWithCountFollowResponse> userResults;
    private List<PlaylistResponse> playlistResults;
    private List<AlbumResponse> albumResponseResults;

    public SearchResultPagerAdapter(Fragment fragment, List<TrackResponse> trackResponses,
                                    List<ProfileWithCountFollowResponse> users,
                                    List<PlaylistResponse> playlists, List<AlbumResponse> albums) {
        super(fragment);
        this.trackResponseResults = trackResponses;
        this.userResults = users;
        this.playlistResults = playlists;
        this.albumResponseResults = albums;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: // Song
                return SearchTrackTabFragment.newInstance(trackResponseResults);
            case 1: // User
                return SearchUserTabFragment.newInstance(userResults);
            case 2: // Playlist
                return SearchPlaylistTabFragment.newInstance(playlistResults);
            case 3: // Album
                return SearchAlbumTabFragment.newInstance(albumResponseResults);
            default:
                return new Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4; // 4 tabs: Song, User, Playlist, Album
    }

    public void updateData(List<TrackResponse> tracks, List<ProfileWithCountFollowResponse> users,
                           List<PlaylistResponse> playlists, List<AlbumResponse> albums) {
        this.trackResponseResults = tracks;
        this.userResults = users;
        this.playlistResults = playlists;
        this.albumResponseResults = albums;
    }
}
