package com.app.musicapp.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.app.musicapp.R;
import com.app.musicapp.adapter.FollowingAdapter;
import com.app.musicapp.model.FollowingUser;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FollowingFragment extends Fragment {
    private ListView listViewFollowing;
    private ImageView ivBack;
    private ImageView ivFollower;
    private FollowingAdapter followingAdapter;
    private List<FollowingUser> followingList;

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
        mockFollowingData();
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
        listViewFollowing.setOnItemClickListener((parent, view1, position, id) -> {
            FollowingUser user = followingList.get(position);
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
    private void navigateToUserProfile(FollowingUser user) {
        UserProfileFragment fragment = UserProfileFragment.newInstance(user, "following");
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
    private void mockFollowingData() {
        if (followingList == null) {
            followingList = new ArrayList<>();
        }
        followingList.add(new FollowingUser(
                "1",
                "MCK",
                "Unknown",
                241000,
                "https://cdn11.dienmaycholon.vn/filewebdmclnew/public/userupload/files/Image%20FP_2024/avatar-cute-3.jpg",
                LocalDateTime.now(),
                true
        ));
        followingList.add(new FollowingUser(
                "2",
                "User2",
                "Unknown",
                15000,
                "https://toigingiuvedep.vn/wp-content/uploads/2022/01/hinh-avatar-cute-nu.jpg",
                LocalDateTime.now(),
                true
        ));
    }
}