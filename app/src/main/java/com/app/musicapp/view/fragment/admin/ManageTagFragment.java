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
import com.app.musicapp.api.ApiClient;
import com.app.musicapp.model.request.TagRequest;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.TagResponse;

import java.util.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageTagFragment extends Fragment {
    private EditText edTag;
    private ImageButton btnAddTag;
    private ListView listViewTag;

    private ManageListAdapter<TagResponse> tagAdapter;
    private List<TagResponse> tagList = new ArrayList<>();
    public ManageTagFragment() {
        // Required empty public constructor
    }
    public static ManageTagFragment newInstance() {
        return new ManageTagFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manage_tag, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edTag = view.findViewById(R.id.ed_tag);
        btnAddTag = view.findViewById(R.id.btn_add_tag);
        listViewTag = view.findViewById(R.id.list_tag);

        // Nút back
        view.findViewById(R.id.image_back).setOnClickListener(v -> back());

        // Nút add
        btnAddTag.setOnClickListener(v -> addTag());

        // Adapter dùng chung
        tagAdapter = new ManageListAdapter<>(
                getContext(),
                tagList,
                ApiClient.getAdminApiService(),
                false, // FALSE: Đây là TAG
                this::loadTags
        );
        listViewTag.setAdapter(tagAdapter);

        // Load tag ban đầu
        loadTags();
    }

    private void back() {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    private void addTag() {
        String tagName = edTag.getText().toString().trim();
        if (tagName.isEmpty()) {
            Toast.makeText(getContext(), "Please enter tag name", Toast.LENGTH_SHORT).show();
            return;
        }

        TagRequest request = new TagRequest(tagName);
        ApiClient.getAdminApiService().createTag(request).enqueue(new Callback<ApiResponse<TagResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<TagResponse>> call, Response<ApiResponse<TagResponse>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Tag added", Toast.LENGTH_SHORT).show();
                    edTag.setText("");
                    loadTags();
                } else {
                    Toast.makeText(getContext(), "Failed to add tag", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<TagResponse>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadTags() {
        ApiClient.getTagService().getTags().enqueue(new Callback<ApiResponse<List<TagResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<TagResponse>>> call, Response<ApiResponse<List<TagResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tagList.clear();
                    tagList.addAll(response.body().getData());
                    tagAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<TagResponse>>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}