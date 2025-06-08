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
import android.widget.Toast;

import com.app.musicapp.R;
import com.app.musicapp.adapter.SearchUserAdapter;
import com.app.musicapp.adapter.VibesAdapter;
import com.app.musicapp.api.ApiClient;
import com.app.musicapp.api.SearchApiService;
import com.app.musicapp.model.AlbumResponse;
import com.app.musicapp.model.response.ApiResponse;
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

import retrofit2.*;

public class SearchPageFragment extends Fragment {
    private GridView gridViewVibes;
    private List<Vibes> vibeList;
    private VibesAdapter vibesAdapter;
    private RecyclerView recyclerViewSearchUser;
    private SearchUserAdapter searchUserAdapter;
    private List<ProfileWithCountFollowResponse> searchResults = new ArrayList<>();
    private SearchApiService searchApiService;
    private EditText searchEditText;
    private ImageButton searchButton;
    private TextView textViewVibes;

    public SearchPageFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Khởi tạo Retrofit service
        searchApiService = ApiClient.getClient().create(SearchApiService.class);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_page, container, false);

        // Khởi tạo GridView
        gridViewVibes = view.findViewById(R.id.gridViewVibes);
        textViewVibes = view.findViewById(R.id.textViewVibes);
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

        // Khởi tạo RecyclerView
        recyclerViewSearchUser = view.findViewById(R.id.recyclerViewSearchUser);
        recyclerViewSearchUser.setLayoutManager(new LinearLayoutManager(getContext()));
        searchUserAdapter = new SearchUserAdapter(searchResults, this::navigateToUserProfile);
        recyclerViewSearchUser.setAdapter(searchUserAdapter);

        // Ánh xạ EditText và ImageButton
        searchEditText = view.findViewById(R.id.searchEditText);
        searchButton = view.findViewById(R.id.searchButton);

        // Logic tự động tìm kiếm khi gõ
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

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
                    searchUsers(query);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
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
    private void searchUsers(String query) {
        searchApiService.searchUser(query).enqueue(new Callback<ApiResponse<List<String>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<String>>> call, Response<ApiResponse<List<String>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                    List<String> userIds = response.body().getData();
                    if (!userIds.isEmpty()) {
                        fetchUserDetails(userIds);
                    } else {
                        searchResults.clear();
                        searchUserAdapter.notifyDataSetChanged();
                        recyclerViewSearchUser.setVisibility(View.VISIBLE);
                        gridViewVibes.setVisibility(View.GONE);
                        textViewVibes.setVisibility(View.GONE);
                    }
                } else {
                    searchResults.clear();
                    searchUserAdapter.notifyDataSetChanged();
                    recyclerViewSearchUser.setVisibility(View.VISIBLE);
                    gridViewVibes.setVisibility(View.GONE);
                    textViewVibes.setVisibility(View.GONE);
                    String errorMsg = response.body() != null ? response.body().getMessage() : "Không tìm thấy người dùng";
                    Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<String>>> call, Throwable t) {
                searchResults.clear();
                searchUserAdapter.notifyDataSetChanged();
                recyclerViewSearchUser.setVisibility(View.VISIBLE);
                gridViewVibes.setVisibility(View.GONE);
                textViewVibes.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Lỗi khi tìm kiếm người dùng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUserDetails(List<String> userIds) {
        if (searchApiService == null) {
            Log.e("SearchPageFragment", "searchApiService is null in fetchUserDetails");
            Toast.makeText(getContext(), "Lỗi kết nối API", Toast.LENGTH_SHORT).show();
            return;
        }
        searchApiService.getUserProfilesByIds(userIds).enqueue(new Callback<ApiResponse<List<ProfileWithCountFollowResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<ProfileWithCountFollowResponse>>> call, Response<ApiResponse<List<ProfileWithCountFollowResponse>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                    searchResults.clear();
                    searchResults.addAll(response.body().getData());
                    searchUserAdapter.notifyDataSetChanged();
                    recyclerViewSearchUser.setVisibility(View.VISIBLE);
                    gridViewVibes.setVisibility(View.GONE);
                    textViewVibes.setVisibility(View.GONE);
                } else {
                    searchResults.clear();
                    searchUserAdapter.notifyDataSetChanged();
                    recyclerViewSearchUser.setVisibility(View.VISIBLE);
                    gridViewVibes.setVisibility(View.GONE);
                    textViewVibes.setVisibility(View.GONE);
                    String errorMsg = response.body() != null ? response.body().getMessage() : "Không lấy được chi tiết người dùng";
                    Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<ProfileWithCountFollowResponse>>> call, Throwable t) {
                searchResults.clear();
                searchUserAdapter.notifyDataSetChanged();
                recyclerViewSearchUser.setVisibility(View.VISIBLE);
                gridViewVibes.setVisibility(View.GONE);
                textViewVibes.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Lỗi khi lấy chi tiết người dùng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
    private void performSearch(String query) {
        // Chuyển sang SearchResultFragment với từ khóa tìm kiếm
        SearchResultFragment fragment = SearchResultFragment.newInstance(query);
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
        hideKeyboard();
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