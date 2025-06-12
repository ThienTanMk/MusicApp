package com.app.musicapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.*;

import com.app.musicapp.R;
import com.app.musicapp.api.ApiClient;
import com.app.musicapp.helper.UrlHelper;
import com.app.musicapp.model.request.AddFollowRequest;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.ProfileWithCountFollowResponse;
import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowingAdapter extends BaseAdapter {
    private Context context;
    private List<ProfileWithCountFollowResponse> followingList;

    public FollowingAdapter(Context context, List<ProfileWithCountFollowResponse> followingList) {
        this.context = context;
        this.followingList = followingList;
    }

    @Override
    public int getCount() {
        return followingList.size();
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
            ProfileWithCountFollowResponse user = followingList.get(i);
            tvUserName.setText(user.getDisplayName() != null ? user.getDisplayName() : "Unknown Name");
            tvUserDetails.setText(formatFollowersCount(user.getFollowerCount()) + " Followers");
            btnFollowing.setText( user.isFollowing()? "Following":"Follow");
            btnFollowing.setOnClickListener(v -> {
                String followStatus = btnFollowing.getText().toString();
                if(followStatus.equals("Follow")){
                    AddFollowRequest addFollowRequest = new AddFollowRequest(user.getUserId());
                    ApiClient.getUserService().follow(addFollowRequest).enqueue(new Callback<ApiResponse<Object>>() {
                        @Override
                        public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                            if(response.isSuccessful()){
                                btnFollowing.setText("Following");
                                user.setFollowerCount(user.getFollowerCount()+1);
                                tvUserDetails.setText(formatFollowersCount(user.getFollowerCount()) + " Followers");
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {

                        }
                    });
                }else{
                    ApiClient.getUserService().unfollow(user.getUserId()).enqueue(new Callback<ApiResponse<Object>>() {
                        @Override
                        public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                            if(response.isSuccessful())
                                user.setFollowerCount(user.getFollowerCount()-1);
                                btnFollowing.setText("Follow");
                                tvUserDetails.setText(formatFollowersCount(user.getFollowerCount()) + " Followers");
                        }

                        @Override
                        public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {

                        }
                    });
                }
            });

            try {
                Glide.with(context)
                        .load(user.getAvatar()!=null?UrlHelper.getAvatarImageUrl(user.getAvatar()): R.drawable.logo)
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
