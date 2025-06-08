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

import java.time.LocalDateTime;
import java.util.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FollowerFragment extends Fragment {
    private ListView listViewFollower;
    private ImageView ivBack;
    private FollowingAdapter followerAdapter;
    private List<ProfileWithCountFollowResponse> followerList;

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

        getFollowers();


        listViewFollower.setOnItemClickListener((parent, view1, position, id) -> {
            ProfileWithCountFollowResponse user = followerList.get(position);
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
    private void navigateToUserProfile(ProfileWithCountFollowResponse user) {
        //tam
        UserProfileFragment fragment = UserProfileFragment.newInstance(user,"following");
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void getFollowers(){
        String userId = SharedPreferencesManager.getInstance(getContext()).getUserId();
        ApiClient.getUserService().getFollowers(userId,0,1000).enqueue(new Callback<ApiResponse<PageFollowResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<PageFollowResponse>> call, Response<ApiResponse<PageFollowResponse>> response) {
                Log.i("follower",response.body().getData().getContent().get(0).getDisplayName());
                if(response.isSuccessful()&&response.body().getData()!=null){
                    followerList.clear();
                    followerList.addAll(response.body().getData().getContent());
                    followerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<PageFollowResponse>> call, Throwable t) {

            }
        });
    }
}