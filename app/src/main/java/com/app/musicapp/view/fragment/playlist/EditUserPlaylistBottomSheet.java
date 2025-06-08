package com.app.musicapp.view.fragment.playlist;
import com.app.musicapp.R;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.app.musicapp.api.ApiClient;
import com.app.musicapp.model.request.UpdatePlaylistInfoRequest;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.PlaylistResponse;
import com.app.musicapp.model.response.TrackResponse;
import com.app.musicapp.view.activity.SignIn;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.*;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EditUserPlaylistBottomSheet extends BottomSheetDialogFragment {
//    private static final String ARG_PLAYLIST = "playlist";
//    private static final int STORAGE_PERMISSION_CODE = 100;
//    private PlaylistResponse playlistResponse;
//    private Uri selectedImageUri;
//    private ActivityResultLauncher<String> pickImageLauncher;
//    private ProgressBar progressBar;
//
//    public static EditUserPlaylistBottomSheet newInstance(PlaylistResponse playlistResponse) {
//        EditUserPlaylistBottomSheet fragment = new EditUserPlaylistBottomSheet();
//        Bundle args = new Bundle();
//        args.putSerializable(ARG_PLAYLIST, playlistResponse);
//        fragment.setArguments(args);
//        return fragment;
//    }
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            playlistResponse = (PlaylistResponse) getArguments().getSerializable(ARG_PLAYLIST);
//        }
//
//        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
//            if (uri != null) {
//                selectedImageUri = uri;
//                ImageView ivPlaylistCover = getView().findViewById(R.id.iv_playlist_cover);
//                if (ivPlaylistCover != null) {
//                    Glide.with(this).load(uri).into(ivPlaylistCover);
//                }
//            }
//        });
//    }
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.bottom_sheet_edit_user_playlist, container, false);
//
//        ImageButton btnClose = view.findViewById(R.id.btn_close);
//        Button btnSave = view.findViewById(R.id.btn_save);
//        ImageView ivPlaylistCover = view.findViewById(R.id.iv_playlist_cover);
//        ImageView ivCameraIcon = view.findViewById(R.id.iv_camera_icon);
//        EditText etPlaylistTitle = view.findViewById(R.id.et_playlist_title);
//        EditText etDescription = view.findViewById(R.id.et_description);
//        EditText etTags = view.findViewById(R.id.et_tags);
//        Switch switchMakePublic = view.findViewById(R.id.switch_make_public);
//        progressBar = view.findViewById(R.id.progress_bar);
//
//        // Khởi tạo dữ liệu
//        if (playlistResponse != null) {
//            Glide.with(this)
//                    .load(playlistResponse.getImagePath() != null ? playlistResponse.getImagePath() : R.drawable.logo)
//                    .placeholder(R.drawable.logo)
//                    .into(ivPlaylistCover);
//            etPlaylistTitle.setText(playlistResponse.getTitle() != null ? playlistResponse.getTitle() : "");
//            etDescription.setText(playlistResponse.getDescription() != null ? playlistResponse.getDescription() : "");
//            etTags.setText(playlistResponse.getTags() != null ? playlistResponse.getTags() : "");
//            switchMakePublic.setChecked(playlistResponse.getPrivacy() != null && playlistResponse.getPrivacy().equals("public"));
//        }
//
//        // Sự kiện đóng
//        btnClose.setOnClickListener(v -> dismiss());
//
//        // Sự kiện chọn ảnh
//        ivCameraIcon.setOnClickListener(v -> {
//            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
//                    != android.content.pm.PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(requireActivity(),
//                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
//            } else {
//                pickImageLauncher.launch("image/*");
//            }
//        });
//
//        // Sự kiện lưu
//        btnSave.setOnClickListener(v -> {
//            String title = etPlaylistTitle.getText().toString().trim();
//            if (title.isEmpty()) {
//                Toast.makeText(getContext(), "Tiêu đề không được để trống", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            String description = etDescription.getText().toString().trim();
//            String tags = etTags.getText().toString().trim();
//            String privacy = switchMakePublic.isChecked() ? "public" : "private";
//
//            updatePlaylist(title, description, tags, privacy);
//        });
//
//        return view;
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == STORAGE_PERMISSION_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
//                pickImageLauncher.launch("image/*");
//            } else {
//                Toast.makeText(getContext(), "Quyền truy cập bộ nhớ bị từ chối", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    private void updatePlaylist(String title, String description, String tags, String privacy) {
//        if (playlistResponse == null || playlistResponse.getId() == null) {
//            Log.e("EditUserPlaylistBottomSheet", "Invalid playlist or null ID");
//            Toast.makeText(getContext(), "Cannot update playlist", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        progressBar.setVisibility(View.VISIBLE);
//
//        // Chuyển tags thành tagIds
//        List<String> tagIds = tags.isEmpty() ? new ArrayList<>() : Arrays.asList(tags.split("\\s*,\\s*"));
//
//        // Lấy trackIds
//        List<String> trackIds = new ArrayList<>();
//        if (playlistResponse.getPlaylistTracks() != null) {
//            for (TrackResponse track : playlistResponse.getPlaylistTracks()) {
//                trackIds.add(track.getId());
//            }
//        }
//
//        // Tạo UpdatePlaylistInfoRequest
////        UpdatePlaylistInfoRequest updateRequest = new UpdatePlaylistInfoRequest(
////                playlistResponse.getId(),
////                title,
////                privacy,
////                trackIds,
////                LocalDateTime.now(),
////                description,
////                tagIds
////        );
//
//        // Tạo RequestBody
//        String json = new com.google.gson.Gson().toJson(updateRequest);
//        RequestBody playlistBody = RequestBody.create(MediaType.parse("application/json"), json);
//
//        // Tạo MultipartBody.Part cho ảnh
//        MultipartBody.Part imagePart = null;
//        if (selectedImageUri != null) {
//            try {
//                File file = new File(requireContext().getCacheDir(), "playlist_image.jpg");
//                copyUriToFile(selectedImageUri, file);
//                RequestBody imageBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
//                imagePart = MultipartBody.Part.createFormData("image", file.getName(), imageBody);
//            } catch (IOException e) {
//                Log.e("EditUserPlaylistBottomSheet", "Error preparing image: " + e.getMessage());
//                progressBar.setVisibility(View.GONE);
//                Toast.makeText(getContext(), "Error uploading image", Toast.LENGTH_SHORT).show();
//                return;
//            }
//        }
//
//        // Gọi API
//        Call<ApiResponse<PlaylistResponse>> call = ApiClient.getPlaylistService()
//                .updatePlaylistInfoV1(imagePart, playlistBody);
//        call.enqueue(new Callback<ApiResponse<PlaylistResponse>>() {
//            @Override
//            public void onResponse(@NonNull Call<ApiResponse<PlaylistResponse>> call, @NonNull Response<ApiResponse<PlaylistResponse>> response) {
//                progressBar.setVisibility(View.GONE);
//                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
//                    PlaylistResponse updatedPlaylist = response.body().getData();
//                    Toast.makeText(getContext(), "Playlist updated successfully", Toast.LENGTH_SHORT).show();
//
//                    // Gửi broadcast
//                    Intent intent = new Intent("PLAYLIST_UPDATED");
//                    intent.putExtra("playlist", updatedPlaylist);
//                    LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent);
//
//                    dismiss();
//                } else if (response.body() != null && response.body().getCode() == 1401) {
//                    Toast.makeText(getContext(), "Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(requireContext(), SignIn.class));
//                    requireActivity().finish();
//                } else {
//                    Log.e("EditUserPlaylistBottomSheet", "API error: " + (response.body() != null ? response.body().getMessage() : "Unknown"));
//                    Toast.makeText(getContext(), "Failed to update playlist", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<ApiResponse<PlaylistResponse>> call, @NonNull Throwable t) {
//                progressBar.setVisibility(View.GONE);
//                Log.e("EditUserPlaylistBottomSheet", "Network error: " + t.getMessage());
//                Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void copyUriToFile(Uri uri, File file) throws IOException {
//        try (InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);
//             FileOutputStream outputStream = new FileOutputStream(file)) {
//            byte[] buffer = new byte[1024];
//            int bytesRead;
//            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                outputStream.write(buffer, 0, bytesRead);
//            }
//        }
//    }
}
