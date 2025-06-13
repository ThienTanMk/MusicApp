package com.app.musicapp.view.fragment.track;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.musicapp.R;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.app.musicapp.api.ApiClient;
import com.app.musicapp.helper.SharedPreferencesManager;
import com.app.musicapp.helper.UrlHelper;
import com.app.musicapp.model.request.TrackRequest;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.GenreResponse;
import com.app.musicapp.model.response.TrackResponse;
import com.app.musicapp.helper.FileUtil;
import com.bumptech.glide.Glide;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.text.InputType;
import android.view.Gravity;




public class UploadTrackFragment extends Fragment {
    private static final int MAX_TITLE_LENGTH = 100;

    private ImageView ivCoverImage;
    private EditText etTitle;
    private TextView tvTitleCount;
    private TextView tvGenre;
    private TextView tvDescription;
    private SwitchMaterial switchPublic;
    private Button btnSave;
    private ImageButton btnBack;
    private ImageButton btnCast;

    private Uri selectedImageUri;
    private Uri selectedAudioUri;
    private String selectedGenre;
    private String description;

    private boolean isEdit = false;
    private TrackResponse trackToEdit;

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    Glide.with(this)
                            .load(selectedImageUri)
                            .centerCrop()
                            .into(ivCoverImage);
                }
            }
    );

    private final ActivityResultLauncher<Intent> pickAudio = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    selectedAudioUri = result.getData().getData();
                    Toast.makeText(getContext(), "Audio track selected", Toast.LENGTH_SHORT).show();
                }
            }
    );

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isEdit = getArguments().getBoolean("isEdit", false);
            if (isEdit) {
                trackToEdit = (TrackResponse) getArguments().getSerializable("track");
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_upload_track, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setupListeners(view);

        if (isEdit && trackToEdit != null) {
            fillTrackData();
        }
    }

    private void initViews(View view) {
        ivCoverImage = view.findViewById(R.id.ivCoverImage);
        etTitle = view.findViewById(R.id.etTitle);
        tvTitleCount = view.findViewById(R.id.tvTitleCount);
        tvGenre = view.findViewById(R.id.tvGenre);
        tvDescription = view.findViewById(R.id.tvDescription);
        switchPublic = view.findViewById(R.id.switchPublic);
        btnSave = view.findViewById(R.id.btnSave);
        btnBack = view.findViewById(R.id.btnBack);
        btnCast = view.findViewById(R.id.btnCast);
    }

    private void setupListeners(View view) {
        view.findViewById(R.id.coverImageContainer).setOnClickListener(v -> openImagePicker());

        etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvTitleCount.setText(s.length() + "/" + MAX_TITLE_LENGTH);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        view.findViewById(R.id.genreSelector).setOnClickListener(v -> openGenreSelector());
        view.findViewById(R.id.descriptionSelector).setOnClickListener(v -> openDescriptionEditor());

        btnBack.setOnClickListener(v -> requireActivity().onBackPressed());
        btnSave.setOnClickListener(v -> uploadTrack());
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImage.launch(intent);
    }

    private void openAudioPicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        pickAudio.launch(intent);
    }

    private void openGenreSelector() {
        ApiClient.getGenreService()
                .getGenres()
                .enqueue(new Callback<ApiResponse<List<GenreResponse>>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<List<GenreResponse>>> call, Response<ApiResponse<List<GenreResponse>>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<GenreResponse> genres = response.body().getData();
                            showGenreDialog(genres);
                        } else {
                            Toast.makeText(getContext(), "Failed to load genres", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<List<GenreResponse>>> call, Throwable t) {
                        Toast.makeText(getContext(), "Error loading genres: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showGenreDialog(List<GenreResponse> genres) {
        String[] genreNames = genres.stream()
                .map(GenreResponse::getName)
                .toArray(String[]::new);

        new AlertDialog.Builder(requireContext())
                .setTitle("Select Genre")
                .setItems(genreNames, (dialog, which) -> {
                    GenreResponse selectedGenreObj = genres.get(which);
                    selectedGenre = selectedGenreObj.getId();
                    tvGenre.setText(selectedGenreObj.getName());
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void openDescriptionEditor() {
        EditText input = new EditText(requireContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        input.setMinLines(3);
        input.setMaxLines(5);
        input.setGravity(Gravity.TOP | Gravity.START);
        input.setText(description != null ? description : "");

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setTitle("Edit Description")
                .setView(input)
                .setPositiveButton("Save", (dialog, which) -> {
                    description = input.getText().toString().trim();
                    tvDescription.setText(description.isEmpty() ? "Add a description" : description);
                })
                .setNegativeButton("Cancel", null);

        builder.show();
    }

    private void fillTrackData() {
        // Fill title
        etTitle.setText(trackToEdit.getTitle());
        tvTitleCount.setText(trackToEdit.getTitle().length() + "/" + MAX_TITLE_LENGTH);

        // Fill description
        description = trackToEdit.getDescription();
        tvDescription.setText(description != null ? description : "Add a description");

        // Fill genre with null check
        if (trackToEdit.getGenre() != null) {
            selectedGenre = trackToEdit.getGenre().getId();
            tvGenre.setText(trackToEdit.getGenre().getName());
        } else {
            selectedGenre = null;
            tvGenre.setText("Select genre");
        }

        // Fill privacy
        switchPublic.setChecked(trackToEdit.getPrivacy().equalsIgnoreCase("PUBLIC"));

        // Load cover image if exists
        if (trackToEdit.getCoverImageName() != null) {
            Glide.with(this)
                    .load(UrlHelper.getCoverImageUrl(trackToEdit.getCoverImageName()))
                    .centerCrop()
                    .into(ivCoverImage);
        }

        // Update UI for edit mode
        btnSave.setText("Update Track");
    }

    private void uploadTrack() {
        if (isEdit) {
            updateTrack();
            return;
        }

        if (selectedAudioUri == null) {
            openAudioPicker();
            return;
        }

        String title = etTitle.getText().toString().trim();
        if (title.isEmpty()) {
            Toast.makeText(getContext(), "Please enter a title", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedGenre == null || selectedGenre.isEmpty()) {
            Toast.makeText(getContext(), "Please select a genre", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Create track request JSON
            TrackRequest trackRequest = new TrackRequest();
            trackRequest.setTitle(title);
            trackRequest.setDescription(description);
            trackRequest.setGenreId(selectedGenre);
            trackRequest.setTagIds(new ArrayList<>(){});
            trackRequest.setCountPlay(0);
            trackRequest.setUserId(SharedPreferencesManager.getInstance(requireContext()).getUserId());
            trackRequest.setPrivacy(switchPublic.isChecked() ? "PUBLIC" : "PRIVATE");

            // Convert track request to RequestBody
            Gson gson = new Gson();
            String trackJson = gson.toJson(trackRequest);
            RequestBody trackRequestBody = RequestBody.create(MediaType.parse("application/json"), trackJson);

            // Create MultipartBody.Part for audio file
            File audioFile = FileUtil.getFile(getContext(), selectedAudioUri);
            RequestBody audioRequestBody = RequestBody.create(MediaType.parse("audio/*"), audioFile);
            MultipartBody.Part audioPart = MultipartBody.Part.createFormData("track_audio", audioFile.getName(), audioRequestBody);

            // Create MultipartBody.Part for image file if exists
            MultipartBody.Part imagePart = null;
            if (selectedImageUri != null) {
                File imageFile = FileUtil.getFile(getContext(), selectedImageUri);
                RequestBody imageRequestBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
                imagePart = MultipartBody.Part.createFormData("cover_image", imageFile.getName(), imageRequestBody);
            }

            // Call API
            ApiClient.getTrackApiService()
                    .uploadTrack(imagePart, audioPart, trackRequestBody)
                    .enqueue(new Callback<ApiResponse<TrackResponse>>() {

                        @Override
                        public void onResponse(Call<ApiResponse<TrackResponse>> call, Response<ApiResponse<TrackResponse>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                requireActivity().getSupportFragmentManager().popBackStack();
                            } else {
                                Toast.makeText(getContext(), "Upload failed", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse<TrackResponse>> call, Throwable t) {
                            Toast.makeText(getContext(), "Upload failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateTrack() {
        String title = etTitle.getText().toString().trim();
        if (title.isEmpty()) {
            Toast.makeText(getContext(), "Please enter a title", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Create track request
            TrackRequest trackRequest = new TrackRequest();
            trackRequest.setTitle(title);
            trackRequest.setDescription(description);
            trackRequest.setGenreId(selectedGenre);
            trackRequest.setTagIds(new ArrayList<>(){});
            trackRequest.setPrivacy(switchPublic.isChecked() ? "PUBLIC" : "PRIVATE");

            // Convert to RequestBody
            Gson gson = new Gson();
            String trackJson = gson.toJson(trackRequest);
            RequestBody metadataBody = RequestBody.create(MediaType.parse("application/json"), trackJson);

            // Create image part if new image selected
            MultipartBody.Part imagePart = null;
            if (selectedImageUri != null) {
                File imageFile = FileUtil.getFile(getContext(), selectedImageUri);
                RequestBody imageRequestBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
                imagePart = MultipartBody.Part.createFormData("cover_image", imageFile.getName(), imageRequestBody);
            }

            // Create audio part if new audio selected
            MultipartBody.Part audioPart = null;
            if (selectedAudioUri != null) {
                File audioFile = FileUtil.getFile(getContext(), selectedAudioUri);
                RequestBody audioRequestBody = RequestBody.create(MediaType.parse("audio/*"), audioFile);
                audioPart = MultipartBody.Part.createFormData("track_audio", audioFile.getName(), audioRequestBody);
            }

            // Call update API
            ApiClient.getTrackApiService()
                    .updateTrack(trackToEdit.getId(), metadataBody, imagePart, audioPart)
                    .enqueue(new Callback<ApiResponse<TrackResponse>>() {
                        @Override
                        public void onResponse(Call<ApiResponse<TrackResponse>> call, Response<ApiResponse<TrackResponse>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                Toast.makeText(getContext(), "Track updated successfully", Toast.LENGTH_SHORT).show();
                                requireActivity().getSupportFragmentManager().popBackStack();
                            } else {
                                Toast.makeText(getContext(), "Failed to update track", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse<TrackResponse>> call, Throwable t) {
                            Toast.makeText(getContext(), "Error updating track: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}