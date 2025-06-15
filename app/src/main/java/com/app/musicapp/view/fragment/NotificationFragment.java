package com.app.musicapp.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.app.musicapp.R;
import com.app.musicapp.adapter.NotificationAdapter;
import com.app.musicapp.api.ApiClient;
import com.app.musicapp.model.NotificationViewModel;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.NotificationResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NotificationFragment extends Fragment {

    NotificationAdapter notificationAdapter;
    List<NotificationResponse> notifications;
    ListView lvNotification;
    NotificationViewModel viewModel;
    public NotificationFragment() {

    }
    public NotificationFragment(NotificationViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    private void loadDetail(List<String> ids){
        ApiClient.getNotificationApiService().getNotifications(ids).enqueue(new Callback<ApiResponse<List<NotificationResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<NotificationResponse>>> call, Response<ApiResponse<List<NotificationResponse>>> response) {
                Log.d("NotificationFragment", "Response: " + response.toString());
                if(!response.isSuccessful())return;
                notifications.clear();
                notifications.addAll(response.body().getData());
                notificationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ApiResponse<List<NotificationResponse>>> call, Throwable t) {

            }
        });
    }
    private void loadNotification(){
        ApiClient.getNotificationApiService().getAll().enqueue(new Callback<ApiResponse<List<String>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<String>>> call, Response<ApiResponse<List<String>>> response) {
                Log.d("NotificationFragment", "Response: " + response.toString());
                if(!response.isSuccessful()) return;
                List<String> ids = response.body().getData();
                loadDetail(ids);
            }

            @Override
            public void onFailure(Call<ApiResponse<List<String>>> call, Throwable t) {

            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        ImageView ivBack = view.findViewById(R.id.image_back);
        ivBack.setOnClickListener(v->{
            if(getActivity()==null)return;
            getActivity().getSupportFragmentManager().popBackStack();
        });
        lvNotification = view.findViewById(R.id.lv_notification);
        notifications = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(notifications,getContext());
        lvNotification.setAdapter(notificationAdapter);
        loadNotification();
        viewModel.getNotification().observe(requireActivity(), message -> {
            notifications.add(0, message);
            notificationAdapter.notifyDataSetChanged();
        });
        return view;

    }
}