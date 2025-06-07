package com.app.musicapp.view.fragment.searchpage;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.musicapp.R;
import com.app.musicapp.adapter.SearchUserAdapter;
import com.app.musicapp.adapter.VibesAdapter;
import com.app.musicapp.model.AlbumResponse;
import com.app.musicapp.model.response.GenreResponse;
import com.app.musicapp.model.GridView.Vibes;
import com.app.musicapp.model.response.LikedPlaylistResponse;
import com.app.musicapp.model.response.PlaylistResponse;
import com.app.musicapp.model.response.TrackResponse;
import com.app.musicapp.model.response.ProfileWithCountFollowResponse;
import com.app.musicapp.model.response.TagResponse;
import com.app.musicapp.view.fragment.searchresult.SearchResultFragment;
import com.app.musicapp.view.fragment.UserProfileFragment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SearchPageFragment extends Fragment {
    private GridView gridViewVibes;
    private List<Vibes> vibeList;
    private VibesAdapter vibesAdapter;
    private RecyclerView recyclerViewSearchUser;
    private SearchUserAdapter searchUserAdapter;
    private List<ProfileWithCountFollowResponse> searchResults = new ArrayList<>();
    private List<ProfileWithCountFollowResponse> allUsers = new ArrayList<>();
    private List<TrackResponse> allTrackResponses = new ArrayList<>();
    private List<Object> allPlaylists = new ArrayList<>();
    private List<AlbumResponse> allAlbumResponses = new ArrayList<>();

    public SearchPageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_page, container, false);

        // Khởi tạo GridView
        gridViewVibes = view.findViewById(R.id.gridViewVibes);
        TextView textViewVibes = view.findViewById(R.id.textViewVibes);
        vibeList = new ArrayList<>();
        vibeList.add(new Vibes(R.drawable.searchbg_hiphop_rap, "Hip Hop & Rap"));
        vibeList.add(new Vibes(R.drawable.searchbg_electronic, "Electronic"));
        vibeList.add(new Vibes(R.drawable.searchbg_ballad, "Ballad"));
        vibeList.add(new Vibes(R.drawable.searchbg_study, "Study"));
        vibeList.add(new Vibes(R.drawable.searchbg_pop, "Pop"));
        vibeList.add(new Vibes(R.drawable.searchbg_chill, "Chill"));
        vibeList.add(new Vibes(R.drawable.searchbg_indie, "Indie"));
        vibeList.add(new Vibes(R.drawable.searchbg_rock, "Rock"));

        vibesAdapter = new VibesAdapter(getContext(), vibeList);
        gridViewVibes.setAdapter(vibesAdapter);

        recyclerViewSearchUser = view.findViewById(R.id.recyclerViewSearchUser);
        recyclerViewSearchUser.setLayoutManager(new LinearLayoutManager(getContext()));
        searchUserAdapter = new SearchUserAdapter(searchResults, this::navigateToUserProfile);
        recyclerViewSearchUser.setAdapter(searchUserAdapter);
        EditText searchEditText = view.findViewById(R.id.searchEditText);
        ImageButton searchButton = view.findViewById(R.id.searchButton);

        initializeSampleData();

        // Logic tự động tìm kiếm khi gõ
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (query.isEmpty()) {
                    searchResults.clear();
                    searchUserAdapter.notifyDataSetChanged();
                    recyclerViewSearchUser.setVisibility(View.GONE);
                    gridViewVibes.setVisibility(View.VISIBLE);
                    textViewVibes.setVisibility(View.VISIBLE);
                    requireActivity().getSupportFragmentManager().popBackStack();
                    View viewPager = requireActivity().findViewById(R.id.view_pager);
                    if (viewPager != null) viewPager.setVisibility(View.VISIBLE);
                    View fragmentContainer = requireActivity().findViewById(R.id.fragment_container);
                    if (fragmentContainer != null) fragmentContainer.setVisibility(View.GONE);
                } else {
                    searchResults.clear();
                    for (ProfileWithCountFollowResponse user : allUsers) {
                        if (user.getDisplayName().toLowerCase().contains(query.toLowerCase())) {
                            searchResults.add(user);
                        }
                    }
                    searchUserAdapter.notifyDataSetChanged();
                    recyclerViewSearchUser.setVisibility(View.VISIBLE);
                    gridViewVibes.setVisibility(View.GONE);
                    textViewVibes.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        // Logic tìm kiếm khi nhấn nút
        searchButton.setOnClickListener(v -> {
            String query = searchEditText.getText().toString().trim();
            if (!query.isEmpty()) {
                hideKeyboard();
                performSearch(query);
                recyclerViewSearchUser.setVisibility(View.GONE);
                gridViewVibes.setVisibility(View.GONE);
                textViewVibes.setVisibility(View.GONE);
            }
        });

        gridViewVibes.setOnItemClickListener((adapterView, view1, i, l) -> {
            Vibes selectedVibe = vibeList.get(i);
            VibeDetailFragment fragment = VibeDetailFragment.newInstance(selectedVibe.getTitle(), selectedVibe.getImageResId());
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();

            View viewPager = requireActivity().findViewById(R.id.view_pager);
            if (viewPager != null) viewPager.setVisibility(View.GONE);
            View fragmentContainer = requireActivity().findViewById(R.id.fragment_container);
            if (fragmentContainer != null) fragmentContainer.setVisibility(View.VISIBLE);
        });

        return view;
    }
    private void hideKeyboard() {
        View currentFocus = requireActivity().getCurrentFocus();
        if (currentFocus != null) {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
                currentFocus.clearFocus();
            }
        }
    }

    private void initializeSampleData() {
        // Dữ liệu mẫu cho User
        allUsers.add(new ProfileWithCountFollowResponse(
                "user123", "Tran", "Viet Quang", "tranvietquang3110", LocalDate.now(),
                true, "email@example.com", "login", "login", "user123", 100, 500
        ));
        allUsers.add(new ProfileWithCountFollowResponse(
                "user456", "Nguyen", "Van A", "nguyenvana", LocalDate.now(),
                false, "a@example.com", "logo", "logo", "user456", 50, 80
        ));

        // Dữ liệu mẫu cho Track
        GenreResponse rockGenreResponse = new GenreResponse("1", "Rock", LocalDateTime.now());
        List<TagResponse> trackTags1 = new ArrayList<>();
        trackTags1.add(new TagResponse("1", "pop", LocalDateTime.now(), "user123"));
        List<TagResponse> trackTags2 = new ArrayList<>();
        trackTags2.add(new TagResponse("2", "rock", LocalDateTime.now(), "user123"));

        allTrackResponses.add(new TrackResponse(
                "1", "Song 1A", "A great pop song", "song1.mp3", "cover1.jpg",
                LocalDateTime.now(), "user123", "3:30", "public", 1000, rockGenreResponse, trackTags1
        ));
        allTrackResponses.add(new TrackResponse(
                "2", "Song 2A", "An awesome rock song", "song2.mp3", "cover2.jpg",
                LocalDateTime.now(), "user456", "4:00", "public", 500, rockGenreResponse, trackTags2
        ));

        // Dữ liệu mẫu cho Playlist
        List<TrackResponse> playlistTracks1 = new ArrayList<>();
        playlistTracks1.add(allTrackResponses.get(0));
        List<TagResponse> playlistTags1 = new ArrayList<>();
        playlistTags1.add(new TagResponse("3", "chill", LocalDateTime.now(), "user123"));

        List<TrackResponse> playlistTracks2 = new ArrayList<>();
        playlistTracks2.add(allTrackResponses.get(1));
        List<TagResponse> playlistTags2 = new ArrayList<>();
        playlistTags2.add(new TagResponse("4", "party", LocalDateTime.now(), "user456"));

        PlaylistResponse playlistResponse1 = new PlaylistResponse(
                "3", "Playlist 1", LocalDateTime.now(), "A chill playlist", "public",
                "user123", rockGenreResponse, "path/to/playlist1.jpg", LocalDateTime.now(), playlistTracks1, playlistTags1,false,null
        );
        PlaylistResponse playlistResponse2 = new PlaylistResponse(
                "4", "Playlist 2", LocalDateTime.now(), "A party playlist", "public",
                "user456", rockGenreResponse, "path/to/playlist2.jpg", LocalDateTime.now(), playlistTracks2, playlistTags2,false,null
        );

        allPlaylists.add(playlistResponse1);
        allPlaylists.add(new LikedPlaylistResponse("5", "user123", LocalDateTime.now(), playlistResponse2));

        // Dữ liệu mẫu cho Album
        List<TrackResponse> albumTracks1 = new ArrayList<>();
        albumTracks1.add(allTrackResponses.get(0));
        List<TagResponse> albumTags1 = new ArrayList<>();
        albumTags1.add(new TagResponse("5", "pop", LocalDateTime.now(), "user123"));

        List<TrackResponse> albumTracks2 = new ArrayList<>();
        albumTracks2.add(allTrackResponses.get(1));
        List<TagResponse> albumTags2 = new ArrayList<>();
        albumTags2.add(new TagResponse("6", "rock", LocalDateTime.now(), "user456"));

        allAlbumResponses.add(new AlbumResponse(
                "Album 1", "Artist 1", "1", "Single", albumTags1, "A pop album", "public",
                "http://album1.com", "path/to/album1.jpg", "user123", "5", LocalDateTime.now(), albumTracks1, rockGenreResponse
        ));
        allAlbumResponses.add(new AlbumResponse(
                "Album 2", "Artist 2", "1", "Album", albumTags2, "A rock album", "public",
                "http://album2.com", "path/to/album2.jpg", "user456", "6", LocalDateTime.now(), albumTracks2, rockGenreResponse
        ));
    }

    private void performSearch(String query) {
        List<TrackResponse> filteredTrackResponses = new ArrayList<>();
        List<ProfileWithCountFollowResponse> filteredUsers = new ArrayList<>();
        List<Object> filteredPlaylists = new ArrayList<>();
        List<AlbumResponse> filteredAlbumResponses = new ArrayList<>();

        for (TrackResponse trackResponse : allTrackResponses) {
            if (trackResponse.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredTrackResponses.add(trackResponse);
            }
        }
        for (ProfileWithCountFollowResponse user : allUsers) {
            if (user.getDisplayName().toLowerCase().contains(query.toLowerCase())) {
                filteredUsers.add(user);
            }
        }
        for (Object playlist : allPlaylists) {
            if (playlist instanceof PlaylistResponse && ((PlaylistResponse) playlist).getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredPlaylists.add(playlist);
            } else if (playlist instanceof LikedPlaylistResponse && ((LikedPlaylistResponse) playlist).getPlaylist().getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredPlaylists.add(playlist);
            }
        }
        for (AlbumResponse albumResponse : allAlbumResponses) {
            if (albumResponse.getAlbumTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredAlbumResponses.add(albumResponse);
            }
        }

        SearchResultFragment fragment = SearchResultFragment.newInstance(filteredTrackResponses, filteredUsers, filteredPlaylists, filteredAlbumResponses, query);
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();

        View viewPager = requireActivity().findViewById(R.id.view_pager);
        if (viewPager != null) viewPager.setVisibility(View.GONE);
        View fragmentContainer = requireActivity().findViewById(R.id.fragment_container);
        if (fragmentContainer != null) fragmentContainer.setVisibility(View.VISIBLE);
    }

    private void navigateToUserProfile(ProfileWithCountFollowResponse profile) {
        Log.d("SearchPageFragment", "Navigating to UserProfile for: " + profile.getDisplayName());
        View currentFocus = requireActivity().getCurrentFocus();
        if (currentFocus != null) {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
            }
            currentFocus.clearFocus();
        }
        UserProfileFragment fragment = UserProfileFragment.newInstance(profile, "search");
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();

        View container = requireActivity().findViewById(R.id.fragment_container);
        View viewPager = requireActivity().findViewById(R.id.view_pager);
        if (container != null) {
            container.setVisibility(View.VISIBLE);
            if (viewPager != null) {
                viewPager.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("SearchPageFragment", "onStart called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("SearchPageFragment", "onStop called");
    }
}