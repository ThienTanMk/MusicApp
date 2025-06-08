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
import com.app.musicapp.api.ApiClient;
import com.app.musicapp.api.SearchApiService;
import com.app.musicapp.model.AlbumResponse;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.LikedPlaylistResponse;
import com.app.musicapp.model.response.PlaylistResponse;
import com.app.musicapp.model.response.TrackResponse;
import com.app.musicapp.model.response.ProfileWithCountFollowResponse;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchResultFragment extends Fragment {
    private static final String ARG_QUERY = "query";

    private EditText searchEditText;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private ImageView ivBack;
    private SearchApiService searchApiService;

    private List<TrackResponse> trackResponseResults = new ArrayList<>();
    private List<ProfileWithCountFollowResponse> userResults = new ArrayList<>();
    private List<PlaylistResponse> playlistResults = new ArrayList<>(); // Thay List<Object> bằng List<PlaylistResponse>
    private List<AlbumResponse> albumResponseResults = new ArrayList<>();

    public SearchResultFragment() {
        // Required empty public constructor
    }
//    public static SearchResultFragment newInstance(List<TrackResponse> trackResponses, List<ProfileWithCountFollowResponse> users,
//                                                   List<Object> playlists, List<AlbumResponse> albumResponses, String query) {
//        SearchResultFragment fragment = new SearchResultFragment();
//        Bundle args = new Bundle();
//        args.putSerializable(ARG_TRACKS, new ArrayList<>(trackResponses));
//        args.putSerializable(ARG_USERS, new ArrayList<>(users));
//        args.putSerializable(ARG_PLAYLISTS, new ArrayList<>(playlists));
//        args.putSerializable(ARG_ALBUMS, new ArrayList<>(albumResponses));
//        args.putString(ARG_QUERY, query);
//        fragment.setArguments(args);
//        return fragment;
//    }
    public static SearchResultFragment newInstance(String query) {
        SearchResultFragment fragment = new SearchResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUERY, query);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            trackResponseResults = (List<TrackResponse>) getArguments().getSerializable(ARG_TRACKS);
//            userResults = (List<ProfileWithCountFollowResponse>) getArguments().getSerializable(ARG_USERS);
//            playlistResults = (List<Object>) getArguments().getSerializable(ARG_PLAYLISTS);
//            albumResponseResults = (List<AlbumResponse>) getArguments().getSerializable(ARG_ALBUMS);
//        }
        searchApiService = ApiClient.getClient().create(SearchApiService.class);
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
                case 0: tab.setText("Track"); break;
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
//        searchEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                String newQuery = s.toString();
//                performSearch(newQuery);
//                SearchResultPagerAdapter pagerAdapter = (SearchResultPagerAdapter) viewPager.getAdapter();
//                if (pagerAdapter != null) {
//                    pagerAdapter.updateData(trackResponseResults, userResults, playlistResults, albumResponseResults);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {}
//        });
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newQuery = s.toString().trim();
                if (!newQuery.isEmpty()) {
                    performSearch(newQuery);
                } else {
                    // Xóa dữ liệu nếu query rỗng
                    trackResponseResults.clear();
                    userResults.clear();
                    playlistResults.clear();
                    albumResponseResults.clear();
                    updatePagerAdapter();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        // Gọi tìm kiếm ban đầu nếu có query
        if (!query.isEmpty()) {
            performSearch(query);
        }
        return view;
    }
    private void performSearch(String query) {
        searchTracks(query);
        searchUsers(query);
        searchPlaylists(query);
        searchAlbums(query);
    }
    private void searchTracks(String query) {
        searchApiService.searchTrack(query).enqueue(new Callback<ApiResponse<List<String>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<String>>> call, Response<ApiResponse<List<String>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                    List<String> trackIds = response.body().getData();
                    if (!trackIds.isEmpty()) {
                        fetchTrackDetails(trackIds);
                    } else {
                        trackResponseResults.clear();
                        updatePagerAdapter();
                    }
                } else {
                    trackResponseResults.clear();
                    updatePagerAdapter();
                    String errorMsg = response.body() != null ? response.body().getMessage() : "Không tìm thấy bài hát";
                    Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<String>>> call, Throwable t) {
                trackResponseResults.clear();
                updatePagerAdapter();
                Toast.makeText(getContext(), "Lỗi khi tìm kiếm bài hát: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void fetchTrackDetails(List<String> trackIds) {
        searchApiService.getTracksByIds(trackIds).enqueue(new Callback<ApiResponse<List<TrackResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<TrackResponse>>> call, Response<ApiResponse<List<TrackResponse>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                    trackResponseResults.clear();
                    trackResponseResults.addAll(response.body().getData());
                    updatePagerAdapter();
                } else {
                    trackResponseResults.clear();
                    updatePagerAdapter();
                    String errorMsg = response.body() != null ? response.body().getMessage() : "Không lấy được chi tiết bài hát";
                    Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<TrackResponse>>> call, Throwable t) {
                trackResponseResults.clear();
                updatePagerAdapter();
                Toast.makeText(getContext(), "Lỗi khi lấy chi tiết bài hát: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchUsers(String query) {
        searchApiService.searchUser(query).enqueue(new Callback<ApiResponse<List<String>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<String>>> call, Response<ApiResponse<List<String>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                    List<String> userIds = response.body().getData();
                    if (!userIds.isEmpty()) {
                        fetchUserDetails(userIds);
                    } else {
                        userResults.clear();
                        updatePagerAdapter();
                    }
                } else {
                    userResults.clear();
                    updatePagerAdapter();
                    String errorMsg = response.body() != null ? response.body().getMessage() : "Không tìm thấy người dùng";
                    Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<String>>> call, Throwable t) {
                userResults.clear();
                updatePagerAdapter();
                Toast.makeText(getContext(), "Lỗi khi tìm kiếm người dùng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUserDetails(List<String> userIds) {
        searchApiService.getUserProfilesByIds(userIds).enqueue(new Callback<ApiResponse<List<ProfileWithCountFollowResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<ProfileWithCountFollowResponse>>> call, Response<ApiResponse<List<ProfileWithCountFollowResponse>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                    userResults.clear();
                    userResults.addAll(response.body().getData());
                    updatePagerAdapter();
                } else {
                    userResults.clear();
                    updatePagerAdapter();
                    String errorMsg = response.body() != null ? response.body().getMessage() : "Không lấy được chi tiết người dùng";
                    Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<ProfileWithCountFollowResponse>>> call, Throwable t) {
                userResults.clear();
                updatePagerAdapter();
                Toast.makeText(getContext(), "Lỗi khi lấy chi tiết người dùng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchPlaylists(String query) {
        searchApiService.searchPlaylist(query).enqueue(new Callback<ApiResponse<List<String>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<String>>> call, Response<ApiResponse<List<String>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                    List<String> playlistIds = response.body().getData();
                    if (!playlistIds.isEmpty()) {
                        fetchPlaylistDetails(playlistIds);
                    } else {
                        playlistResults.clear();
                        updatePagerAdapter();
                    }
                } else {
                    playlistResults.clear();
                    updatePagerAdapter();
                    String errorMsg = response.body() != null ? response.body().getMessage() : "Không tìm thấy playlist";
                    Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<String>>> call, Throwable t) {
                playlistResults.clear();
                updatePagerAdapter();
                Toast.makeText(getContext(), "Lỗi khi tìm kiếm playlist: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchPlaylistDetails(List<String> playlistIds) {
        searchApiService.getPlaylistsByIds(playlistIds).enqueue(new Callback<ApiResponse<List<PlaylistResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<PlaylistResponse>>> call, Response<ApiResponse<List<PlaylistResponse>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                    playlistResults.clear();
                    playlistResults.addAll(response.body().getData());
                    updatePagerAdapter();
                } else {
                    playlistResults.clear();
                    updatePagerAdapter();
                    String errorMsg = response.body() != null ? response.body().getMessage() : "Không lấy được chi tiết playlist";
                    Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<PlaylistResponse>>> call, Throwable t) {
                playlistResults.clear();
                updatePagerAdapter();
                Toast.makeText(getContext(), "Lỗi khi lấy chi tiết playlist: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchAlbums(String query) {
        searchApiService.searchAlbum(query).enqueue(new Callback<ApiResponse<List<String>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<String>>> call, Response<ApiResponse<List<String>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                    List<String> albumIds = response.body().getData();
                    if (!albumIds.isEmpty()) {
                        fetchAlbumDetails(albumIds);
                    } else {
                        albumResponseResults.clear();
                        updatePagerAdapter();
                    }
                } else {
                    albumResponseResults.clear();
                    updatePagerAdapter();
                    String errorMsg = response.body() != null ? response.body().getMessage() : "Không tìm thấy album";
                    Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<String>>> call, Throwable t) {
                albumResponseResults.clear();
                updatePagerAdapter();
                Toast.makeText(getContext(), "Lỗi khi tìm kiếm album: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchAlbumDetails(List<String> albumIds) {
        searchApiService.getAlbumsByIds(albumIds).enqueue(new Callback<ApiResponse<List<AlbumResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<AlbumResponse>>> call, Response<ApiResponse<List<AlbumResponse>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                    albumResponseResults.clear();
                    albumResponseResults.addAll(response.body().getData());
                    updatePagerAdapter();
                } else {
                    albumResponseResults.clear();
                    updatePagerAdapter();
                    String errorMsg = response.body() != null ? response.body().getMessage() : "Không lấy được chi tiết album";
                    Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<AlbumResponse>>> call, Throwable t) {
                albumResponseResults.clear();
                updatePagerAdapter();
                Toast.makeText(getContext(), "Lỗi khi lấy chi tiết album: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatePagerAdapter() {
        SearchResultPagerAdapter pagerAdapter = (SearchResultPagerAdapter) viewPager.getAdapter();
        if (pagerAdapter != null) {
            pagerAdapter.updateData(trackResponseResults, userResults, playlistResults, albumResponseResults);
            pagerAdapter.notifyDataSetChanged();
        }
    }
}