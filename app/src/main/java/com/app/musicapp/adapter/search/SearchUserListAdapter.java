package com.app.musicapp.adapter.search;

import android.content.Context;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.musicapp.R;
import com.app.musicapp.api.ApiClient;
import com.app.musicapp.helper.UrlHelper;
import com.app.musicapp.model.request.AddFollowRequest;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.ProfileWithCountFollowResponse;
import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.util.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchUserListAdapter extends ArrayAdapter<ProfileWithCountFollowResponse> {
    private final List<ProfileWithCountFollowResponse> users;
    private Button btnFollowing;
    private TextView tvUserDetails;
    private final OnUserClickListener listener;
    private final HashMap<String, Boolean> followStates; // Quản lý trạng thái follow
    public interface OnUserClickListener {
        void onUserClick(ProfileWithCountFollowResponse user);
    }

    public SearchUserListAdapter(@NonNull Context context, List<ProfileWithCountFollowResponse> users, OnUserClickListener listener) {
        super(context, 0, users);
        this.users = users != null ? users : new ArrayList<>();
        this.listener = listener;
        this.followStates = new HashMap<>(); // Khởi tạo HashMap để lưu trạng thái follow
        initializeFollowStates(); // Khởi tạo trạng thái mặc định cho tất cả user
    }
    private void initializeFollowStates() {
        for (ProfileWithCountFollowResponse user : users) {
            followStates.put(user.getUserId(), false);
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_following, parent, false);
        }
        ProfileWithCountFollowResponse user = users.get(position);
        TextView tvUserName = convertView.findViewById(R.id.tv_user_name);
        tvUserDetails = convertView.findViewById(R.id.tv_user_details);
        ImageView ivUserImage = convertView.findViewById(R.id.iv_user_image);
        btnFollowing = convertView.findViewById(R.id.btn_following);

        // Đặt dữ liệu
        tvUserName.setText(user.getDisplayName() != null ? user.getDisplayName() : "Unknown");
        tvUserDetails.setText(user.getFollowerCount() + " Followers");

        Glide.with(getContext())
                .load(user.getAvatar() != null && !user.getAvatar().isEmpty() ? UrlHelper.getCoverImageUrl(user.getAvatar()) : R.drawable.logo)
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(ivUserImage);
        String userId = user.getUserId();
        boolean isFollowing = followStates.getOrDefault(userId, false);
        btnFollowing.setText(isFollowing ? "Following" : "Follow");

        // Sự kiện click vào nút Following
        btnFollowing.setOnClickListener(v -> {
            boolean newFollowState = !isFollowing;
            followStates.put(userId, newFollowState);

            if (newFollowState) {
                followUser(userId);
            } else {
                unfollowUser(userId);
            }
        });
        convertView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onUserClick(user);
            }
        });

        return convertView;
    }

    private void followUser(String userId) {
        AddFollowRequest request = new AddFollowRequest(userId);
        ApiClient.getUserService().follow(request).enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                if (response.isSuccessful()) {
                    followStates.put(userId, true);
                    btnFollowing.setText("Following");

                    for (ProfileWithCountFollowResponse user : users) {
                        if (user.getUserId().equals(userId)) {
                            user.setFollowerCount(user.getFollowerCount() + 1);
                            tvUserDetails.setText(formatFollowersCount(user.getFollowerCount()) + " Followers");
                            break;
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Không thể follow người dùng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi khi follow: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void unfollowUser(String userId) {
        ApiClient.getUserService().unfollow(userId).enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                if (response.isSuccessful()) {
                    followStates.put(userId, false);
                    btnFollowing.setText("Follow");

                    for (ProfileWithCountFollowResponse user : users) {
                        if (user.getUserId().equals(userId)) {
                            user.setFollowerCount(Math.max(0, user.getFollowerCount() - 1));
                            tvUserDetails.setText(formatFollowersCount(user.getFollowerCount()) + " Followers");
                            break;
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Không thể bỏ theo dõi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi khi bỏ theo dõi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String formatFollowersCount(int count) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        if (count >= 1000000) {
            return numberFormat.format(count / 1000000) + "M";
        } else if (count >= 1000) {
            return numberFormat.format(count / 1000) + "K";
        }
        return String.valueOf(count);
    }
}
