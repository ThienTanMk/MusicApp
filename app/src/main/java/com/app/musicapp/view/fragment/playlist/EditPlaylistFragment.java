package com.app.musicapp.view.fragment.playlist;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.app.musicapp.R;
import com.app.musicapp.api.ApiClient;
import com.app.musicapp.helper.UrlHelper;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.PlaylistResponse;
import com.app.musicapp.model.request.UpdatePlaylistInfoRequest;
import com.app.musicapp.model.response.TagResponse;
import com.app.musicapp.model.response.TrackResponse;
import com.app.musicapp.helper.FileUtil;
import com.app.musicapp.helper.LocalDateTimeAdapter;
import com.bumptech.glide.Glide;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.time.LocalDateTime;

public class EditPlaylistFragment extends Fragment {
    private PlaylistResponse playlist;
    private ImageView ivCoverImage;
    private EditText etTitle, etDescription;
    private TextView tvTitleCount, tvTags;
    private SwitchMaterial switchPublic;
    private Uri selectedImageUri;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private List<TagResponse> allTags = new ArrayList<>();
    private List<TagResponse> selectedTags = new ArrayList<>();

    public static EditPlaylistFragment newInstance(PlaylistResponse playlist) {
        EditPlaylistFragment fragment = new EditPlaylistFragment();
        Bundle args = new Bundle();
        args.putSerializable("playlist", playlist);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            playlist = (PlaylistResponse) getArguments().getSerializable("playlist");
            if (playlist.getPlaylistTags() != null) {
                selectedTags.addAll(playlist.getPlaylistTags());
            }
        }

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        Glide.with(this)
                                .load(selectedImageUri)
                                .placeholder(R.drawable.logo)
                                .error(R.drawable.logo)
                                .into(ivCoverImage);
                    }
                }
        );

        loadTags();
    }

    private void loadTags() {
        ApiClient.getTagService().getTags()
                .enqueue(new Callback<ApiResponse<List<TagResponse>>>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse<List<TagResponse>>> call,
                                        @NonNull Response<ApiResponse<List<TagResponse>>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            allTags.clear();
                            allTags.addAll(response.body().getData());
                            updateTagsText();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse<List<TagResponse>>> call,
                                        @NonNull Throwable t) {
                        Toast.makeText(requireContext(), "Failed to load tags", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateTagsText() {
        if (selectedTags.isEmpty()) {
            tvTags.setText("Select tags");
        } else {
            String tagNames = selectedTags.stream()
                    .map(TagResponse::getName)
                    .collect(Collectors.joining(", "));
            tvTags.setText(tagNames);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_playlist, container, false);

        initializeViews(view);
        setupListeners();
        populateData();

        return view;
    }

    private void initializeViews(View view) {
        ImageButton btnBack = view.findViewById(R.id.btnBack);
        Button btnSave = view.findViewById(R.id.btnSave);
        ivCoverImage = view.findViewById(R.id.ivCoverImage);
        etTitle = view.findViewById(R.id.etTitle);
        etDescription = view.findViewById(R.id.etDescription);
        tvTitleCount = view.findViewById(R.id.tvTitleCount);
        tvTags = view.findViewById(R.id.tvTags);
        switchPublic = view.findViewById(R.id.switchPublic);

        btnBack.setOnClickListener(v -> requireActivity().onBackPressed());
        btnSave.setOnClickListener(v -> savePlaylist());

        if (playlist != null) {
            etTitle.setText(playlist.getTitle());
            tvTitleCount.setText(playlist.getTitle().length() + "/80");
            etDescription.setText(playlist.getDescription());
            switchPublic.setChecked("public".equalsIgnoreCase(playlist.getPrivacy()));

            if (playlist.getImagePath() != null) {
                Glide.with(this)
                        .load(UrlHelper.getCoverImageUrl(playlist.getImagePath()))
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.logo)
                        .into(ivCoverImage);
            }
        }
    }

    private void setupListeners() {
        etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvTitleCount.setText(s.length() + "/80");
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        ivCoverImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        tvTags.setOnClickListener(v -> showTagsDialog());
    }

    private void populateData() {
        if (playlist != null) {
            etTitle.setText(playlist.getTitle());
            tvTitleCount.setText(playlist.getTitle().length() + "/80");
            etDescription.setText(playlist.getDescription());
            switchPublic.setChecked("public".equalsIgnoreCase(playlist.getPrivacy()));

            if (playlist.getImagePath() != null) {
                Glide.with(this)
                        .load(UrlHelper.getCoverImageUrl(playlist.getImagePath()))
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.logo)
                        .into(ivCoverImage);
            }
        }
    }

    private void showDescriptionDialog() {
        final EditText editText = new EditText(requireContext());
        editText.setHint("Enter description");
        editText.setText(playlist.getDescription());
        editText.setMinLines(3);
        editText.setMaxLines(5);
        editText.setGravity(Gravity.TOP | Gravity.START);

        // Add padding to EditText
        int padding = (int) (16 * requireContext().getResources().getDisplayMetrics().density);
        editText.setPadding(padding, padding, padding, padding);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Edit Description")
               .setView(editText)
               .setPositiveButton("Save", (dialog, which) -> {
                   String newDescription = editText.getText().toString().trim();
                   playlist.setDescription(newDescription);
                   etDescription.setText(newDescription.isEmpty() ? "Description" : newDescription);
               })
               .setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showTagsDialog() {
        if (allTags.isEmpty()) {
            Toast.makeText(requireContext(), "No tags available", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] tagNames = allTags.stream()
                .map(TagResponse::getName)
                .toArray(String[]::new);
        boolean[] checkedItems = new boolean[allTags.size()];

        // Set initial checked state based on selectedTags
        for (int i = 0; i < allTags.size(); i++) {
            TagResponse tag = allTags.get(i);
            checkedItems[i] = selectedTags.stream()
                    .anyMatch(selectedTag -> selectedTag.getId().equals(tag.getId()));
        }

        new AlertDialog.Builder(requireContext())
                .setTitle("Select Tags")
                .setMultiChoiceItems(tagNames, checkedItems, (dialog, which, isChecked) -> {
                    TagResponse tag = allTags.get(which);
                    if (isChecked) {
                        if (!selectedTags.contains(tag)) {
                            selectedTags.add(tag);
                        }
                    } else {
                        selectedTags.removeIf(t -> t.getId().equals(tag.getId()));
                    }
                })
                .setPositiveButton("OK", (dialog, which) -> updateTagsText())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void savePlaylist() {
        if (etTitle.getText().toString().trim().isEmpty()) {
            Toast.makeText(requireContext(), "Please enter a title", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            UpdatePlaylistInfoRequest request = new UpdatePlaylistInfoRequest(
                etTitle.getText().toString().trim(),
                switchPublic.isChecked() ? "PUBLIC" : "PRIVATE",
                playlist.getPlaylistTracks() != null ? playlist.getPlaylistTracks().stream().map(TrackResponse::getId).collect(Collectors.toList()) : new ArrayList<>(),
                playlist.getId(),
                playlist.getReleaseDate(),
                etDescription.getText().toString().trim(),
                playlist.getGenre() != null ? playlist.getGenre().getId() : null,
                selectedTags.stream().map(TagResponse::getId).collect(Collectors.toList())
            );
            
            Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
            String jsonRequest = gson.toJson(request);
            RequestBody playlistBody = RequestBody.create(MediaType.parse("application/json"), jsonRequest);

            // Create image part if image is selected
            MultipartBody.Part imagePart = null;
            if (selectedImageUri != null) {
                File imageFile = FileUtil.getFile(requireContext(), selectedImageUri);
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
                imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), requestFile);
            }

            ApiClient.getPlaylistService().updatePlaylistInfoV1(imagePart, playlistBody)
                    .enqueue(new Callback<ApiResponse<PlaylistResponse>>() {
                        @Override
                        public void onResponse(@NonNull Call<ApiResponse<PlaylistResponse>> call,
                                            @NonNull Response<ApiResponse<PlaylistResponse>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                Toast.makeText(requireContext(), "Playlist updated successfully", Toast.LENGTH_SHORT).show();
                                requireActivity().onBackPressed();
                            } else {
                                try {
                                    String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                                    Log.e("EditPlaylistFragment", "Error response: " + errorBody);
                                    Toast.makeText(requireContext(), "Failed to update playlist: " + errorBody, Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Log.e("EditPlaylistFragment", "Error reading error body", e);
                                    Toast.makeText(requireContext(), "Failed to update playlist", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ApiResponse<PlaylistResponse>> call, @NonNull Throwable t) {
                            Log.e("EditPlaylistFragment", "Network error", t);
                            Toast.makeText(requireContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (Exception e) {
            Log.e("EditPlaylistFragment", "Error updating playlist", e);
            Toast.makeText(requireContext(), "Error updating playlist: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
} 