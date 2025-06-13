package com.app.musicapp.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.musicapp.R;
import com.app.musicapp.api.ApiClient;
import com.app.musicapp.helper.SharedPreferencesManager;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.RoleResponse;
import com.app.musicapp.model.response.UserResponse;
import com.app.musicapp.view.activity.AdminActivity;
import com.app.musicapp.view.activity.SignIn;
import com.app.musicapp.view.activity.SplashActivity;
import com.app.musicapp.view.fragment.playlist.PlaylistsFragment;
import com.app.musicapp.view.fragment.track.LikedTracksFragment;

import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingFragment extends Fragment {
    private TextView text_admin;
    public SettingFragment() {
        // Required empty public constructor
    }


    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.image_back).setOnClickListener(this::back);
        view.findViewById(R.id.text_your_tracks).setOnClickListener(this::tracks);
        view.findViewById(R.id.text_liked_tracks).setOnClickListener(this::likeTracks);
        view.findViewById(R.id.text_playlist).setOnClickListener(this::playList);
        text_admin.setOnClickListener(this::admin);
        view.findViewById(R.id.button_signout).setOnClickListener(this::signOut);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_setting, container, false);
        text_admin = view.findViewById(R.id.text_admin);
        getRole();
        return view;

    }

    public void back(View view) {
        if(getActivity()==null)return;
        getActivity().getSupportFragmentManager().popBackStack();
    }

    public void tracks(View view) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        PlaylistsFragment playlistsFragment = new PlaylistsFragment();
        transaction.replace(R.id.fragment_container, playlistsFragment);
        transaction.addToBackStack(null); // optional, nếu muốn quay lại
        transaction.commit();
    }

    public void likeTracks(View view) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        LikedTracksFragment likedTracksFragment = new LikedTracksFragment();
        transaction.replace(R.id.fragment_container, likedTracksFragment);
        transaction.addToBackStack(null); // optional, nếu muốn quay lại
        transaction.commit();
    }

    public void playList(View view) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        PlaylistsFragment uploadsFragment = PlaylistsFragment.newInstance();
        transaction.replace(R.id.fragment_container, uploadsFragment);
        transaction.addToBackStack(null); // optional, nếu muốn quay lại
        transaction.commit();
    }
    public void admin(View view){
        Intent intent = new Intent(getContext(), AdminActivity.class);
        startActivity(intent);
    }
    public void signOut(View view) {
        if(getContext()==null)return;
        SharedPreferencesManager.getInstance(getContext()).clearSession();
        Intent intent = new Intent(getContext(), SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        getContext().startActivity(intent);
    }
    private void getRole(){
        ApiClient.getIdentityService().getUserinfo().enqueue(new Callback<ApiResponse<UserResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserResponse>> call, Response<ApiResponse<UserResponse>> response) {
                if(response.isSuccessful() && response.body() != null){
                    Set<RoleResponse> roles = response.body().getData().getRoles();
                    Log.e("role", "onResponse: "+ roles );
                    boolean isAdmin = false;
                    for (RoleResponse role : roles) {
                        if ("ADMIN".equals(role.getName())) {
                            isAdmin = true;
                            break;
                        }
                    }

                    if (isAdmin) {
                        text_admin.setVisibility(View.VISIBLE);
                    } else {
                        text_admin.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UserResponse>> call, Throwable t) {
                text_admin.setVisibility(View.GONE);
            }
        });
    }
}