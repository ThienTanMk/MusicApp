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
import com.app.musicapp.adapter.search.SearchUserAdapter;
import com.app.musicapp.adapter.search.VibesAdapter;
import com.app.musicapp.api.ApiClient;
import com.app.musicapp.api.SearchApiService;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.GridView.Vibes;
import com.app.musicapp.model.response.ProfileWithCountFollowResponse;
import com.app.musicapp.view.fragment.searchresult.SearchResultFragment;
import com.app.musicapp.view.fragment.profile.UserProfileFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.*;

public class SearchPageFragment extends Fragment {
    private GridView gridViewVibes;
    private List<Vibes> vibeList;
    private VibesAdapter vibesAdapter;
    private EditText searchEditText;
    private ImageButton searchButton;
    private TextView textViewVibes;

    public SearchPageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        vibeList.add(new Vibes(R.drawable.searchbg_hiphop_rap, "Hip Hop & Rap")); //id 1
        vibeList.add(new Vibes(R.drawable.searchbg_electronic, "Electronic")); // id 2
        vibeList.add(new Vibes(R.drawable.searchbg_ballad, "Ballad")); //id 3
        vibeList.add(new Vibes(R.drawable.searchbg_study, "Study")); //id 4
        vibeList.add(new Vibes(R.drawable.searchbg_pop, "Pop")); //id 5
        vibeList.add(new Vibes(R.drawable.searchbg_chill, "Chill")); //id 6
        vibeList.add(new Vibes(R.drawable.searchbg_indie, "Indie")); //id 7
        vibeList.add(new Vibes(R.drawable.searchbg_rock, "Rock")); //id 8

        vibesAdapter = new VibesAdapter(getContext(), vibeList);
        gridViewVibes.setAdapter(vibesAdapter);

        // Ánh xạ EditText và ImageButton
        searchEditText = view.findViewById(R.id.searchEditText);
        searchButton = view.findViewById(R.id.searchButton);

        // Logic tìm kiếm khi nhấn nút
        searchButton.setOnClickListener(v -> {
            String query = searchEditText.getText().toString().trim();
            if (!query.isEmpty()) {
                hideKeyboard();
                performSearch(query);
                searchEditText.setText("");
            } else {
                Toast.makeText(getContext(), "Vui lòng nhập từ khóa tìm kiếm", Toast.LENGTH_SHORT).show();
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