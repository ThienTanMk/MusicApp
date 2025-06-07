package com.app.musicapp.adapter;

import android.content.Context;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.musicapp.R;
import com.app.musicapp.model.response.ProfileWithCountFollowResponse;
import com.bumptech.glide.Glide;

import java.util.*;

public class SearchUserListAdapter extends ArrayAdapter<ProfileWithCountFollowResponse> {
    private final List<ProfileWithCountFollowResponse> users;
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
        TextView tvUserFrom = convertView.findViewById(R.id.tv_user_from);
        TextView tvUserDetails = convertView.findViewById(R.id.tv_user_details);
        ImageView ivUserImage = convertView.findViewById(R.id.iv_user_image);
        Button btnFollowing = convertView.findViewById(R.id.btn_following);

        // Đặt dữ liệu
        tvUserName.setText(user.getDisplayName() != null ? user.getDisplayName() : "Unknown");
        tvUserFrom.setText("VietNam");
        tvUserDetails.setText(user.getFollowerCount() + " Followers");

        Glide.with(getContext())
                .load(user.getAvatar() != null && !user.getAvatar().isEmpty() ? user.getAvatar() : R.drawable.logo)
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
            btnFollowing.setText(newFollowState ? "Following" : "Follow");

            if (newFollowState) {
                // Gọi API để follow user
                followUser(userId);
            } else {
                // Gọi API để unfollow user
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
        // Gọi API hoặc xử lý logic follow
    }

    private void unfollowUser(String userId) {
        // Gọi API hoặc xử lý logic unfollow
    }
}
