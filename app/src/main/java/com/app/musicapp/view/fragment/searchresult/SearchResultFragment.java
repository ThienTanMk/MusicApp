package com.app.musicapp.view.fragment.searchresult;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.app.musicapp.R;
import com.app.musicapp.adapter.SearchResultPagerAdapter;
import com.app.musicapp.model.AlbumResponse;
import com.app.musicapp.model.response.LikedPlaylistResponse;
import com.app.musicapp.model.response.PlaylistResponse;
import com.app.musicapp.model.response.TrackResponse;
import com.app.musicapp.model.response.ProfileWithCountFollowResponse;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.*;

public class SearchResultFragment extends Fragment {
    private static final String ARG_TRACKS = "tracks";
    private static final String ARG_USERS = "users";
    private static final String ARG_PLAYLISTS = "playlists";
    private static final String ARG_ALBUMS = "albums";
    private static final String ARG_QUERY = "query";

    private EditText searchEditText;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private ImageView ivBack;

    private List<TrackResponse> trackResponseResults = new ArrayList<>();
    private List<ProfileWithCountFollowResponse> userResults = new ArrayList<>();
    private List<Object> playlistResults = new ArrayList<>();
    private List<AlbumResponse> albumResponseResults = new ArrayList<>();

    public SearchResultFragment() {
        // Required empty public constructor
    }
    public static SearchResultFragment newInstance(List<TrackResponse> trackResponses, List<ProfileWithCountFollowResponse> users,
                                                   List<Object> playlists, List<AlbumResponse> albumResponses, String query) {
        SearchResultFragment fragment = new SearchResultFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TRACKS, new ArrayList<>(trackResponses));
        args.putSerializable(ARG_USERS, new ArrayList<>(users));
        args.putSerializable(ARG_PLAYLISTS, new ArrayList<>(playlists));
        args.putSerializable(ARG_ALBUMS, new ArrayList<>(albumResponses));
        args.putString(ARG_QUERY, query);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            trackResponseResults = (List<TrackResponse>) getArguments().getSerializable(ARG_TRACKS);
            userResults = (List<ProfileWithCountFollowResponse>) getArguments().getSerializable(ARG_USERS);
            playlistResults = (List<Object>) getArguments().getSerializable(ARG_PLAYLISTS);
            albumResponseResults = (List<AlbumResponse>) getArguments().getSerializable(ARG_ALBUMS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);

        // Ánh xạ view
        searchEditText = view.findViewById(R.id.searchEditText);
        ivBack = view.findViewById(R.id.iv_back);
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager_results);

        // Lấy query từ Bundle và đặt vào searchEditText
        String query = getArguments() != null ? getArguments().getString(ARG_QUERY, "") : "";
        searchEditText.setText(query);

        // Thiết lập ViewPager2 và TabLayout
        SearchResultPagerAdapter adapter = new SearchResultPagerAdapter(this, trackResponseResults, userResults, playlistResults, albumResponseResults);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0: tab.setText("Song"); break;
                case 1: tab.setText("User"); break;
                case 2: tab.setText("Playlist"); break;
                case 3: tab.setText("Album"); break;
            }
        }).attach();

        // Sự kiện nút Back
        ivBack.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
            View mainViewPager = requireActivity().findViewById(R.id.view_pager);
            View fragmentContainer = requireActivity().findViewById(R.id.fragment_container);
            if (mainViewPager != null && fragmentContainer != null) {
                mainViewPager.setVisibility(View.VISIBLE);
                fragmentContainer.setVisibility(View.GONE);
            }
            // Ẩn RecyclerView khi quay lại SearchPageFragment
            RecyclerView recyclerView = requireActivity().findViewById(R.id.recyclerViewSearchUser);
            if (recyclerView != null) {
                recyclerView.setVisibility(View.GONE);
            }
            GridView gridView = requireActivity().findViewById(R.id.gridViewVibes);
            TextView textView = requireActivity().findViewById(R.id.textViewVibes);
            if (gridView != null && textView != null) {
                gridView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
            }
        });
       // ivBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        // Sự kiện thay đổi từ khóa tìm kiếm
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newQuery = s.toString();
                performSearch(newQuery);
                SearchResultPagerAdapter pagerAdapter = (SearchResultPagerAdapter) viewPager.getAdapter();
                if (pagerAdapter != null) {
                    pagerAdapter.updateData(trackResponseResults, userResults, playlistResults, albumResponseResults);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return view;
    }
    private void performSearch(String query) {
        List<TrackResponse> tempTrackResponseResults = new ArrayList<>(trackResponseResults);
        List<ProfileWithCountFollowResponse> tempUserResults = new ArrayList<>(userResults);
        List<Object> tempPlaylistResults = new ArrayList<>(playlistResults);
        List<AlbumResponse> tempAlbumResponseResults = new ArrayList<>(albumResponseResults);

        trackResponseResults.clear();
        userResults.clear();
        playlistResults.clear();
        albumResponseResults.clear();

        if (query != null && !query.isEmpty()) {
            // Tìm kiếm Track
            for (TrackResponse trackResponse : tempTrackResponseResults) {
                if (trackResponse.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    trackResponseResults.add(trackResponse);
                }
            }

            // Tìm kiếm User
            for (ProfileWithCountFollowResponse user : tempUserResults) {
                if (user.getDisplayName().toLowerCase().contains(query.toLowerCase())) {
                    userResults.add(user);
                }
            }

            // Tìm kiếm Playlist
            for (Object playlist : tempPlaylistResults) {
                if (playlist instanceof PlaylistResponse && ((PlaylistResponse) playlist).getTitle().toLowerCase().contains(query.toLowerCase())) {
                    playlistResults.add(playlist);
                } else if (playlist instanceof LikedPlaylistResponse && ((LikedPlaylistResponse) playlist).getPlaylistResponse().getTitle().toLowerCase().contains(query.toLowerCase())) {
                    playlistResults.add(playlist);
                }
            }

            // Tìm kiếm Album
            for (AlbumResponse albumResponse : tempAlbumResponseResults) {
                if (albumResponse.getAlbumTitle().toLowerCase().contains(query.toLowerCase())) {
                    albumResponseResults.add(albumResponse);
                }
            }
        }
    }
}