package com.app.musicapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.*;

import com.app.musicapp.R;
import com.app.musicapp.model.FollowingUser;
import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class FollowingAdapter extends BaseAdapter {
    private Context context;
    private List<FollowingUser> followingList;

    public FollowingAdapter(Context context, List<FollowingUser> followingList) {
        this.context = context;
        this.followingList = followingList != null ? followingList : new ArrayList<>();
    }

    @Override
    public int getCount() {
        int count = followingList.size();
        System.out.println("getCount: " + count);
        return count;
    }

    @Override
    public Object getItem(int i) {
        return followingList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_following, viewGroup, false);
        }

        // Ánh xạ các view
        ImageView ivUserImage = view.findViewById(R.id.iv_user_image);
        TextView tvUserName = view.findViewById(R.id.tv_user_name);
        TextView tvUserFrom = view.findViewById(R.id.tv_user_from);
        TextView tvUserDetails = view.findViewById(R.id.tv_user_details);
        Button btnFollowing = view.findViewById(R.id.btn_following);

        if (i >= 0 && i < followingList.size()) {
            FollowingUser user = followingList.get(i);

            // Gán dữ liệu vào view
            tvUserName.setText(user.getName() != null ? user.getName() : "Unknown Name");
            tvUserFrom.setText(user.getLocation() != null ? user.getLocation() : "Unknown");
            tvUserDetails.setText(formatFollowersCount(user.getFollowersCount()) + " Followers");
            btnFollowing.setText(user.isFollowing() ? "Following" : "Follow");
            btnFollowing.setOnClickListener(v -> {
                user.setFollowing(!user.isFollowing());
                btnFollowing.setText(user.isFollowing() ? "Following" : "Follow");
            });

            try {
                Glide.with(context)
                        .load(user.getAvatarUrl() != null && !user.getAvatarUrl().isEmpty() ? user.getAvatarUrl() : R.drawable.logo)
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.logo)
                        .into(ivUserImage);
            } catch (Exception e) {
                ivUserImage.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        } else {
            if (context != null) {
                Toast.makeText(context, "Invalid position: " + i, Toast.LENGTH_SHORT).show();
            }
        }

        return view;
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
