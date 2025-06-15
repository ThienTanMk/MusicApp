package com.app.musicapp.view.fragment.admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.app.musicapp.R;
import com.app.musicapp.adapter.ManageListAdapter;
import com.app.musicapp.api.AdminApiService;
import com.app.musicapp.api.ApiClient;
import com.app.musicapp.model.request.GenreRequest;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.GenreResponse;

import java.util.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageGenreFragment extends Fragment {
    private EditText edGenre;
    private ImageButton btnAddGenre;
    private ListView listViewGenre;

    private ManageListAdapter<GenreResponse> genreAdapter;
    private List<GenreResponse> genreList = new ArrayList<>();

    public static ManageGenreFragment newInstance() {
        return new ManageGenreFragment();
    }
    public ManageGenreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manage_genre, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edGenre = view.findViewById(R.id.ed_genre);
        btnAddGenre = view.findViewById(R.id.btn_add_genre);
        listViewGenre = view.findViewById(R.id.list_genre);
        view.findViewById(R.id.image_back).setOnClickListener(v -> back());

        btnAddGenre.setOnClickListener(v -> addGenre());

        genreAdapter = new ManageListAdapter<>(
                getContext(),
                genreList,
                ApiClient.getAdminApiService(),
                true,
                this::loadGenres
        );
        listViewGenre.setAdapter(genreAdapter);

        loadGenres();
    }
    private void back() {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    private void addGenre() {
        String genreName = edGenre.getText().toString().trim();
        if (genreName.isEmpty()) {
            Toast.makeText(getContext(), "Please enter genre name", Toast.LENGTH_SHORT).show();
            return;
        }

        GenreRequest request = new GenreRequest(genreName);
        ApiClient.getAdminApiService().createGenre(request).enqueue(new Callback<ApiResponse<GenreResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<GenreResponse>> call, Response<ApiResponse<GenreResponse>> response) {
                if (response.isSuccessful()) {
                    edGenre.setText("");
                    loadGenres();
                } else {
                    Toast.makeText(getContext(), "Failed to add genre", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<GenreResponse>> call, Throwable t) {
                try {
                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                }
            }
        });
    }

    private void loadGenres() {
        ApiClient.getGenreService().getGenres().enqueue(new Callback<ApiResponse<List<GenreResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<GenreResponse>>> call, Response<ApiResponse<List<GenreResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    genreList.clear();
                    genreList.addAll(response.body().getData());
                    genreAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<GenreResponse>>> call, Throwable t) {
                try {
                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                }
            }
        });
    }
}