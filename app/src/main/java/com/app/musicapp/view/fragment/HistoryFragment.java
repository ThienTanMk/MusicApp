package com.app.musicapp.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.app.musicapp.R;
import com.app.musicapp.adapter.track.TrackAdapter;
import com.app.musicapp.api.ApiClient;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.TrackResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HistoryFragment extends Fragment {

    private TrackAdapter trackAdapter;
    ImageView backBtn, playBtn, clearAllHistoryBtn;
    private ListView historyListView;
    private ArrayList<TrackResponse> histories;

    public HistoryFragment() {
        // Required empty public constructor
    }

    private void getHistory(){
        ApiClient.getHistoryApiService().getAllHistory().enqueue(new Callback<ApiResponse<List<TrackResponse>>>() {

            @Override
            public void onResponse(Call<ApiResponse<List<TrackResponse>>> call, Response<ApiResponse<List<TrackResponse>>> response) {
                if(response.isSuccessful()&&response.body()!=null){
                    histories.clear();
                    histories.addAll(response.body().getData());
                    trackAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<TrackResponse>>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        histories = new ArrayList<>();
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        historyListView = view.findViewById(R.id.list_view_history);
        backBtn = view.findViewById(R.id.iv_back);
        playBtn = view.findViewById(R.id.image_play_btn);
        clearAllHistoryBtn = view.findViewById(R.id.image_clear_all_history_btn);

        backBtn.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        clearAllHistoryBtn.setOnClickListener(v -> {
           ApiClient.getHistoryApiService().deleteAllHistory().enqueue(new Callback<ApiResponse<String>>() {
               @Override
               public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                   if(response.isSuccessful()){
                       histories.clear();
                       trackAdapter.notifyDataSetChanged();
                   }
               }

               @Override
               public void onFailure(Call<ApiResponse<String>> call, Throwable t) {

               }
           });
        });

        trackAdapter = new TrackAdapter(this, histories);
        historyListView.setAdapter(trackAdapter);
        getHistory();
        return view;
    }
}