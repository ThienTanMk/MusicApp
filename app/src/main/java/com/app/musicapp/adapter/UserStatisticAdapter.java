package com.app.musicapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.app.musicapp.R;
import com.app.musicapp.model.response.ProfileWithCountFollowResponse;
import com.bumptech.glide.Glide;
import java.util.List;

public class UserStatisticAdapter extends RecyclerView.Adapter<UserStatisticAdapter.UserStatisticViewHolder> {
    public enum Type { PLAY, LIKE, COMMENT }
    private final List<Item> items;
    private final Type type;
    private final Context context;

    public static class Item {
        public final ProfileWithCountFollowResponse user;
        public final int count;
        public Item(ProfileWithCountFollowResponse user, int count) {
            this.user = user;
            this.count = count;
        }
    }

    public UserStatisticAdapter(Context context, List<Item> items, Type type) {
        this.context = context;
        this.items = items;
        this.type = type;
    }

    @NonNull
    @Override
    public UserStatisticViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_statistic, parent, false);
        return new UserStatisticViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserStatisticViewHolder holder, int position) {
        Item item = items.get(position);
        ProfileWithCountFollowResponse user = item.user;
        holder.tvDisplayName.setText(user.getDisplayName());
        holder.tvFollowers.setText(user.getFollowerCount() + " followers");
        // Set icon và text count
        switch (type) {
            case PLAY:
                holder.imgTypeIcon.setImageResource(R.drawable.ic_play);
                holder.tvCount.setText(item.count + " plays");
                break;
            case LIKE:
                holder.imgTypeIcon.setImageResource(R.drawable.ic_favorite);
                holder.tvCount.setText(item.count + " like");
                break;
            case COMMENT:
                holder.imgTypeIcon.setImageResource(R.drawable.comment_icon);
                holder.tvCount.setText(item.count + " comments");
                break;
        }
        // Avatar
        if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
            Glide.with(context)
                .load(com.app.musicapp.helper.UrlHelper.getAvatarImageUrl(user.getAvatar()))
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_person)
                .into(holder.imgAvatar);
        } else {
            holder.imgAvatar.setImageResource(R.drawable.ic_person);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class UserStatisticViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar, imgTypeIcon, imgFollowersIcon;
        TextView tvDisplayName, tvCount, tvFollowers;
        UserStatisticViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.img_avatar);
            imgTypeIcon = itemView.findViewById(R.id.img_type_icon);
            imgFollowersIcon = itemView.findViewById(R.id.img_followers_icon);
            tvDisplayName = itemView.findViewById(R.id.tv_display_name);
            tvCount = itemView.findViewById(R.id.tv_count);
            tvFollowers = itemView.findViewById(R.id.tv_followers);
        }
    }
} 