package com.app.musicapp.view.fragment.profile;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.musicapp.R;
import com.app.musicapp.api.ApiClient;
import com.app.musicapp.model.request.ProfileUpdateRequest;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.UploadAvatarResponse;
import com.app.musicapp.model.response.UploadCoverResponse;
import com.app.musicapp.model.response.UserProfileResponse;
import com.app.musicapp.helper.FileUtil;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileFragment extends Fragment {
    private Button btnSave;
    private ImageButton ivBack;
    private ImageView imgAvatar, ivCover;
    private TextInputEditText etDisplayName;
    private TextInputLayout displayNameLayout;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private String currentCoverUrl, currentAvatarUrl, currentDisplayName;
    private Uri selectedImageUri;
    private boolean hasChanges = false;
    private boolean isAvatarSelected = false;
    private ProgressBar progressBar;

    public EditProfileFragment() {
        // Required empty public constructor
    }
    public static EditProfileFragment newInstance(String coverUrl, String avatarUrl, String displayName) {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();
        args.putString("coverUrl", coverUrl);
        args.putString("avatarUrl", avatarUrl);
        args.putString("displayName", displayName);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentCoverUrl = getArguments().getString("coverUrl");
            currentAvatarUrl = getArguments().getString("avatarUrl");
            currentDisplayName = getArguments().getString("displayName");
        }
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == requireActivity().RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null && isAdded()) {
                            hasChanges = true;
                            if (isAvatarSelected) {
                                Glide.with(this)
                                        .load(selectedImageUri)
                                        .placeholder(R.drawable.logo)
                                        .error(R.drawable.logo)
                                        .into(imgAvatar);
                                changeAvatar(selectedImageUri);
                            } else {
                                Glide.with(this)
                                        .load(selectedImageUri)
                                        .placeholder(R.drawable.background_music_uploads)
                                        .error(R.drawable.logo)
                                        .into(ivCover);
                                changeCover(selectedImageUri);
                            }
                        }
                    }
                }
        );
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        etDisplayName = view.findViewById(R.id.etDisplayName);
        displayNameLayout = (TextInputLayout) etDisplayName.getParent().getParent();
        btnSave = view.findViewById(R.id.btnSave);
        ivBack = view.findViewById(R.id.btnBack);
        imgAvatar = view.findViewById(R.id.img_avatar);
        ivCover = view.findViewById(R.id.img_background);
        progressBar = view.findViewById(R.id.progressBar);

        if (currentDisplayName != null) {
            etDisplayName.setText(currentDisplayName);
        }
        if (currentAvatarUrl != null) {
            Glide.with(this).load(currentAvatarUrl)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(imgAvatar);
        }
        if (currentCoverUrl != null) {
            Glide.with(this).load(currentCoverUrl)
                    .placeholder(R.drawable.background_music_uploads)
                    .error(R.drawable.logo)
                    .into(ivCover);
        }
        etDisplayName.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(android.text.Editable s) {
                hasChanges = !s.toString().equals(currentDisplayName);
            }
        });

        ivBack.setOnClickListener(v -> {
            if (hasChanges) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Hủy thay đổi")
                        .setMessage("Bạn có thay đổi chưa lưu. Bạn có muốn hủy không?")
                        .setPositiveButton("Hủy", (dialog, which) -> requireActivity().getSupportFragmentManager().popBackStack())
                        .setNegativeButton("Tiếp tục chỉnh sửa", null)
                        .show();
            } else {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        btnSave.setOnClickListener(v -> {
            String newDisplayName = etDisplayName.getText().toString().trim();

            if (newDisplayName.isEmpty()) {
                displayNameLayout.setError("Tên hiển thị không được để trống");
                return;
            }
            if (newDisplayName.length() > 50) {
                displayNameLayout.setError("Tên hiển thị không được vượt quá 50 ký tự");
                return;
            }
            displayNameLayout.setError(null);

            ProfileUpdateRequest request = new ProfileUpdateRequest(newDisplayName);

            new AlertDialog.Builder(requireContext())
                    .setTitle("Xác nhận lưu")
                    .setMessage("Bạn có muốn lưu các thay đổi này không?")
                    .setPositiveButton("Lưu", (dialog, which) -> saveProfile(request))
                    .setNegativeButton("Hủy", null)
                    .show();
        });

        imgAvatar.setOnClickListener(v -> {
            isAvatarSelected = true;
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        ivCover.setOnClickListener(v -> {
            isAvatarSelected = false;
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        return view;
    }
    private void saveProfile(ProfileUpdateRequest request) {
        if (!isAdded()) return;
        progressBar.setVisibility(View.VISIBLE); // Hiển thị ProgressBar
        Call<ApiResponse<UserProfileResponse>> call = ApiClient.getUserProfileApiService().updateUserProfile(request);
        call.enqueue(new Callback<ApiResponse<UserProfileResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<UserProfileResponse>> call, @NonNull Response<ApiResponse<UserProfileResponse>> response) {
                if (!isAdded()) return;
                progressBar.setVisibility(View.GONE);
                Log.d("EditProfileFragment", "saveProfile response code: " + response.code() + ", body: " + (response.body() != null ? response.body().toString() : "null"));
                if (response.isSuccessful()) {
                    currentDisplayName = request.getDisplayName();
                    hasChanges = false;
//                    changeAvatar(selectedImageUri);
//                    changeCover(selectedImageUri);
                    Toast.makeText(getContext(), "Cập nhật hồ sơ thành công", Toast.LENGTH_SHORT).show();
                    if (requireActivity() != null) {
                        requireActivity().getSupportFragmentManager().popBackStack();
                    }
                } else {
                    Log.e("EditProfileFragment", "saveProfile failed, response: " + (response.body() != null ? response.body().toString() : "null"));
                    Toast.makeText(getContext(), "Không thể cập nhật hồ sơ, mã lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<UserProfileResponse>> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                progressBar.setVisibility(View.GONE);
                Log.e("EditProfileFragment", "saveProfile error: " + t.getMessage());
                Toast.makeText(getContext(), "Lỗi cập nhật hồ sơ: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void changeCover(Uri imageUri) {
        try {
            MultipartBody.Part imagePart = null;
            if (imageUri != null) {
                File imageFile = FileUtil.getFile(getContext(), imageUri);
                RequestBody imageRequestBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
                imagePart = MultipartBody.Part.createFormData("cover", imageFile.getName(), imageRequestBody);
            }

            Call<ApiResponse<UploadCoverResponse>> call = ApiClient.getUserProfileApiService().uploadCover(imagePart);
            call.enqueue(new Callback<ApiResponse<UploadCoverResponse>>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse<UploadCoverResponse>> call, @NonNull Response<ApiResponse<UploadCoverResponse>> response) {
                    if (!isAdded()) return;
                    Log.d("EditProfileFragment", "uploadCover response code: " + response.code() + ", body: " + (response.body() != null ? response.body().toString() : "null"));
                    if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                        UploadCoverResponse uploadResponse = response.body().getData();
                        currentCoverUrl = uploadResponse.getCoverName();
                        hasChanges = true;
                        Toast.makeText(getContext(), "Cập nhật ảnh bìa thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("EditProfileFragment", "uploadCover failed, response: " + (response.body() != null ? response.body().toString() : "null"));
                        Toast.makeText(getContext(), "Không thể cập nhật ảnh bìa, mã lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse<UploadCoverResponse>> call, @NonNull Throwable t) {
                    if (!isAdded()) return;
                    Log.e("EditProfileFragment", "uploadCover error: " + t.getMessage());
                    Toast.makeText(getContext(), "Lỗi cập nhật ảnh bìa: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            if (isAdded()) {
                Log.e("EditProfileFragment", "Error processing cover: " + e.getMessage());
                Toast.makeText(getContext(), "Lỗi xử lý ảnh bìa: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void changeAvatar(Uri imageUri) {
        try {
            MultipartBody.Part imagePart = null;
            if (imageUri != null) {
                File imageFile = FileUtil.getFile(getContext(), imageUri);
                RequestBody imageRequestBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
                imagePart = MultipartBody.Part.createFormData("avatar", imageFile.getName(), imageRequestBody);
            }

            Call<ApiResponse<UploadAvatarResponse>> call = ApiClient.getUserProfileApiService().uploadAvatar(imagePart);
            call.enqueue(new Callback<ApiResponse<UploadAvatarResponse>>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse<UploadAvatarResponse>> call, @NonNull Response<ApiResponse<UploadAvatarResponse>> response) {
                    if (!isAdded()) return;
                    Log.d("EditProfileFragment", "uploadAvatar response code: " + response.code() + ", body: " + (response.body() != null ? response.body().toString() : "null"));
                    if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                        UploadAvatarResponse uploadResponse = response.body().getData();
                        currentAvatarUrl = uploadResponse.getAvatarName();
                        hasChanges = true;
                        Toast.makeText(getContext(), "Cập nhật ảnh đại diện thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("EditProfileFragment", "uploadAvatar failed, response: " + (response.body() != null ? response.body().toString() : "null"));
                        Toast.makeText(getContext(), "Không thể cập nhật ảnh đại diện, mã lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(@NonNull Call<ApiResponse<UploadAvatarResponse>> call, @NonNull Throwable t) {
                    if (!isAdded()) return;
                    Log.e("EditProfileFragment", "uploadAvatar error: " + t.getMessage());
                    Toast.makeText(getContext(), "Lỗi cập nhật ảnh đại diện: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            if (isAdded()) {
                Log.e("EditProfileFragment", "Error processing avatar: " + e.getMessage());
                Toast.makeText(getContext(), "Lỗi xử lý ảnh đại diện: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}