package com.app.musicapp.view.fragment.follow;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.app.musicapp.R;
import com.app.musicapp.adapter.FollowingAdapter;
import com.app.musicapp.api.ApiClient;
import com.app.musicapp.helper.SharedPreferencesManager;
import com.app.musicapp.model.FollowingUser;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.PageFollowResponse;
import com.app.musicapp.model.response.ProfileWithCountFollowResponse;
import com.app.musicapp.view.fragment.UserProfileFragment;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowingFragment extends Fragment {
    private ListView listViewFollowing;
    private ImageView ivBack;
    private ImageView ivFollower;
    private FollowingAdapter followingAdapter;
    private List<ProfileWithCountFollowResponse> followingList;

    public FollowingFragment() {
        // Required empty public constructor
    }

    public static FollowingFragment newInstance() {
        return new FollowingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        followingList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_following, container, false);
        // Ánh xạ views
        listViewFollowing = view.findViewById(R.id.listViewFollowing);
        ivBack = view.findViewById(R.id.iv_back);
        ivFollower = view.findViewById(R.id.iv_follower);

        // Khởi tạo adapter sau khi có dữ liệu
        followingAdapter = new FollowingAdapter(getContext(), followingList);
        listViewFollowing.setAdapter(followingAdapter);

        getFollowingData();


        listViewFollowing.setOnItemClickListener((parent, view1, position, id) -> {
            ProfileWithCountFollowResponse user = followingList.get(position);
            navigateToUserProfile(user);
        });
        // Xử lý nút Quay lại
        ivBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                View mainView = requireActivity().findViewById(R.id.main);
                View viewPager = mainView.findViewById(R.id.view_pager);
                View fragmentContainer = mainView.findViewById(R.id.fragment_container);

                if (viewPager != null && fragmentContainer != null) {
                    viewPager.setVisibility(View.VISIBLE);
                    fragmentContainer.setVisibility(View.GONE);
                }
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        ivFollower.setOnClickListener(v -> {
            if (getActivity() != null) {
                FollowerFragment followerFragment = FollowerFragment.newInstance();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, followerFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }
    private void navigateToUserProfile(ProfileWithCountFollowResponse user) {
        UserProfileFragment fragment = UserProfileFragment.newInstance(user, "following");
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void getFollowingData() {
        String userId = SharedPreferencesManager.getInstance(getContext()).getUserId();
        ApiClient.getUserService().getFollowings(userId,0,1000).enqueue(new Callback<ApiResponse<PageFollowResponse>>() {

            @Override
            public void onResponse(Call<ApiResponse<PageFollowResponse>> call, Response<ApiResponse<PageFollowResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    followingList.clear();
                    followingList.addAll(response.body().getData().getContent());
                    followingAdapter.notifyDataSetChanged();
                } else {
                    try {
                        if (response.errorBody() != null) {
                            Log.e("API Error", "Error: " + response.errorBody().string());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<PageFollowResponse>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}