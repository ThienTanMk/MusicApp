package com.app.musicapp.adapter.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.musicapp.R;
import com.app.musicapp.helper.UrlHelper;
import com.app.musicapp.model.response.ProfileWithCountFollowResponse;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.UserViewHolder>{
    private List<ProfileWithCountFollowResponse> users;
    private final OnUserClickListener listener;

    public interface OnUserClickListener {
        void onUserClick(ProfileWithCountFollowResponse user);
    }

    public SearchUserAdapter(List<ProfileWithCountFollowResponse> users, OnUserClickListener listener) {
        this.users = users != null ? users : new ArrayList<>();
        this.listener = listener;
    }

    public void updateData(List<ProfileWithCountFollowResponse> newUsers) {
        this.users = newUsers != null ? newUsers : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_search, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        if (users != null && position >= 0 && position < users.size()) {
            ProfileWithCountFollowResponse user = users.get(position);
            holder.bind(user);
            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onUserClick(user);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return users != null ? users.size() : 0;
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername;
        ImageView ivUserAvatar;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivUserAvatar = itemView.findViewById(R.id.iv_user_avatar);
        }
        void bind(ProfileWithCountFollowResponse user) {
            if (user != null) {
                tvUsername.setText(user.getDisplayName() != null ? user.getDisplayName() : "Unknown");
                Glide.with(itemView.getContext())
                        .load(user.getAvatar() != null && !user.getAvatar().isEmpty() ? UrlHelper.getCoverImageUrl(user.getAvatar()) : R.drawable.logo)
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.logo)
                        .into(ivUserAvatar);
            }
        }
    }
}
