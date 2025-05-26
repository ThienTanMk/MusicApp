package com.app.musicapp.view.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import android.widget.TextView;

import com.app.musicapp.R;
import com.app.musicapp.adapter.SearchUserAdapter;
import com.app.musicapp.adapter.VibesAdapter;
import com.app.musicapp.model.GridView.Vibes;
import com.app.musicapp.model.ProfileWithCountFollowResponse;

import java.time.LocalDate;
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
        // Tạo danh sách Vibes
        vibeList = new ArrayList<>();
        vibeList.add(new Vibes(R.drawable.searchbg_hiphop_rap,"Hip Hop & Rap"));
        vibeList.add(new Vibes(R.drawable.searchbg_electronic,"Electronic"));
        vibeList.add(new Vibes(R.drawable.searchbg_ballad,"Ballad"));
        vibeList.add(new Vibes(R.drawable.searchbg_study,"Study"));
        vibeList.add(new Vibes(R.drawable.searchbg_pop,"Pop"));
        vibeList.add(new Vibes(R.drawable.searchbg_chill,"Chill"));
        vibeList.add(new Vibes(R.drawable.searchbg_indie,"Indie"));
        vibeList.add(new Vibes(R.drawable.searchbg_rock,"Rock"));

        // Khởi tạo và gán adapter cho GridView
        vibesAdapter = new VibesAdapter(getContext(), vibeList);
        gridViewVibes.setAdapter(vibesAdapter);

        recyclerViewSearchUser = view.findViewById(R.id.recyclerViewSearchUser);
        recyclerViewSearchUser.setLayoutManager(new LinearLayoutManager(getContext()));
        searchUserAdapter = new SearchUserAdapter(searchResults, this::navigateToUserProfile);
        recyclerViewSearchUser.setAdapter(searchUserAdapter);
        EditText searchEditText = view.findViewById(R.id.searchEditText);

        allUsers.add(new ProfileWithCountFollowResponse(
                "user123",
                "Tran",
                "Viet Quang",
                "tranvietquang3110",
                LocalDate.now(),
                true,
                "email@example.com",
                "login",
                "login",
                "user123",
                100,
                500
        ));

        allUsers.add(new ProfileWithCountFollowResponse(
                "user456",
                "Nguyen",
                "Van A",
                "nguyenvana",
                LocalDate.now(),
                false,
                "a@example.com",
                "logo",
                "logo",
                "user456",
                50,
                80
        ));

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    performUserSearch(s.toString());
                    recyclerViewSearchUser.setVisibility(View.VISIBLE);
                    gridViewVibes.setVisibility(View.GONE);
                    textViewVibes.setVisibility(View.GONE);
                } else {
                    recyclerViewSearchUser.setVisibility(View.GONE);
                    gridViewVibes.setVisibility(View.VISIBLE);
                    textViewVibes.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
        gridViewVibes.setOnItemClickListener((adapterView, view1, i, l) -> {
            Vibes selectedVibe = vibeList.get(i);
            VibeDetailFragment fragment = VibeDetailFragment.newInstance(selectedVibe.getTitle(), selectedVibe.getImageResId());
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();

            // Ẩn ViewPager và hiển thị fragment_container
            View viewPager = requireActivity().findViewById(R.id.view_pager);
            if (viewPager != null) viewPager.setVisibility(View.GONE);
            View fragmentContainer = requireActivity().findViewById(R.id.fragment_container);
            if (fragmentContainer != null) fragmentContainer.setVisibility(View.VISIBLE);
        });
        return view;
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
        try {
            View container = requireActivity().findViewById(R.id.fragment_container);
            View viewPager = requireActivity().findViewById(R.id.view_pager);
            if (container == null) {
                Log.e("SearchPageFragment", "fragment_container not found in layout");
                return;
            } else {
                Log.d("SearchPageFragment", "fragment_container found: " + container.getClass().getSimpleName());
                container.setVisibility(View.VISIBLE);
                if (viewPager != null) {
                    viewPager.setVisibility(View.GONE);
                }
            }
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
            Log.d("SearchPageFragment", "Transaction committed successfully");
        } catch (IllegalStateException e) {
            Log.e("SearchPageFragment", "Failed to commit transaction: " + e.getMessage());
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
    private void performUserSearch(String query) {
        List<ProfileWithCountFollowResponse> filteredUsers = new ArrayList<>();

        for (ProfileWithCountFollowResponse user : allUsers) {
            if (user.getDisplayName().toLowerCase().contains(query.toLowerCase())) {
                filteredUsers.add(user);
            }
        }
        searchResults.clear();
        searchResults.addAll(filteredUsers);
        searchUserAdapter.updateData(filteredUsers);
        recyclerViewSearchUser.setVisibility(!filteredUsers.isEmpty() ? View.VISIBLE : View.GONE);
    }
}