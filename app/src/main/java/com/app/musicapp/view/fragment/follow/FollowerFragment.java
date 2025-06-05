package com.app.musicapp.view.fragment.follow;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.app.musicapp.R;
import com.app.musicapp.adapter.FollowingAdapter;
import com.app.musicapp.model.FollowingUser;
import com.app.musicapp.view.fragment.UserProfileFragment;

import java.time.LocalDateTime;
import java.util.*;


public class FollowerFragment extends Fragment {
    private ListView listViewFollower;
    private ImageView ivBack;
    private FollowingAdapter followerAdapter;
    private List<FollowingUser> followerList;

    public FollowerFragment() {
        // Required empty public constructor
    }

    public static FollowerFragment newInstance() {
        return new FollowerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        followerList = new ArrayList<>();
        mockFollowerData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_follower, container, false);

        // Ánh xạ views
        listViewFollower = view.findViewById(R.id.listViewFollower);
        ivBack = view.findViewById(R.id.iv_back);

        // Khởi tạo adapter cho danh sách follower
        followerAdapter = new FollowingAdapter(getContext(), followerList);
        listViewFollower.setAdapter(followerAdapter);
        listViewFollower.setOnItemClickListener((parent, view1, position, id) -> {
            FollowingUser user = followerList.get(position);
            navigateToUserProfile(user);
        });
        // Xử lý nút Quay lại
        ivBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }
    private void navigateToUserProfile(FollowingUser user) {
        UserProfileFragment fragment = UserProfileFragment.newInstance(user,"following");
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
    private void mockFollowerData() {
        if (followerList == null) {
            followerList = new ArrayList<>();
        }
        followerList.add(new FollowingUser(
                "1",
                "tranvietquang3110",
                "Unknown",
                1,
                "https://cdn11.dienmaycholon.vn/filewebdmclnew/public/userupload/files/Image%20FP_2024/avatar-cute-3.jpg",
                LocalDateTime.now(),
                true
        ));
        followerList.add(new FollowingUser(
                "2",
                "Follower2",
                "Unknown",
                5000,
                "https://toigingiuvedep.vn/wp-content/uploads/2022/01/hinh-avatar-cute-nu.jpg",
                LocalDateTime.now(),
                true
        ));
    }
}