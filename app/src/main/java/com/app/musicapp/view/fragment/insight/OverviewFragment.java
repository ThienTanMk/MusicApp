package com.app.musicapp.view.fragment.insight;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.DatePickerDialog;
import android.widget.Button;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.app.musicapp.R;
import com.app.musicapp.api.ApiClient;
import com.app.musicapp.helper.SharedPreferencesManager;
import com.app.musicapp.model.DailyPlay;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.PlayResponse;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import java.util.Calendar;
import com.app.musicapp.model.DailyLike;
import com.app.musicapp.model.DailyComment;
import com.app.musicapp.model.response.LikeResponse;
import com.app.musicapp.model.response.CommentStatisticResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.app.musicapp.adapter.UserStatisticAdapter;
import com.app.musicapp.model.UserStatistic;
import com.app.musicapp.model.response.ProfileWithCountFollowResponse;
import com.app.musicapp.model.TopTrack;
import com.app.musicapp.adapter.TopTrackAdapter;

public class OverviewFragment extends Fragment {
    private BarChart barChart;
    private Button btnPlays, btnLikes, btnComments, btnPickDate;
    private String currentType = "plays";
    private LocalDate fromDate, toDate;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private RecyclerView rvUserStatistic;
    private UserStatisticAdapter userStatisticAdapter;
    private final List<UserStatisticAdapter.Item> userStatisticItems = new ArrayList<>();
    private RecyclerView rvTopTracks;
    private TopTrackAdapter topTrackAdapter;
    private TextView textTimeUserStatistic, textTimeTopTracks;
    private final List<TopTrack> topTrackList = new java.util.ArrayList<>();
    private String role = "user";

    public static OverviewFragment newInstance(String role) {
        OverviewFragment fragment = new OverviewFragment();
        Bundle args = new Bundle();
        args.putString("role", role);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            role = getArguments().getString("role", "user");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        barChart = view.findViewById(R.id.barChart);
        btnPlays = view.findViewById(R.id.btnPlays);
        btnLikes = view.findViewById(R.id.btnLikes);
        textTimeUserStatistic = view.findViewById(R.id.text_time_user_statistic);
        textTimeTopTracks = view.findViewById(R.id.text_time_top_tracks);
        btnComments = view.findViewById(R.id.btnComments);
        btnPickDate = view.findViewById(R.id.btnPickDate);
        rvUserStatistic = view.findViewById(R.id.rv_user_statistic);
        rvUserStatistic.setLayoutManager(new LinearLayoutManager(getContext()));
        userStatisticAdapter = new UserStatisticAdapter(getContext(), userStatisticItems, UserStatisticAdapter.Type.PLAY);
        rvUserStatistic.setAdapter(userStatisticAdapter);
        rvTopTracks = view.findViewById(R.id.rv_top_tracks);
        rvTopTracks.setLayoutManager(new LinearLayoutManager(getContext()));
        topTrackAdapter = new TopTrackAdapter(getContext(), topTrackList);
        rvTopTracks.setAdapter(topTrackAdapter);
        setupBarChart();
        setupButtons();
        // Mặc định lấy 7 ngày gần nhất
        toDate = LocalDate.now();
        fromDate = toDate.minusDays(6);
        updateTextTime("Last 7 days");
        fetchStatistic();
        fetchTopTracks();
        return view;
    }

    private void setupBarChart() {
        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setPinchZoom(false);
        barChart.setScaleEnabled(false);
        barChart.getAxisRight().setEnabled(false);
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
    }

    private void setupButtons() {
        btnPlays.setOnClickListener(v -> {
            currentType = "plays";
            setActiveButton(btnPlays);
            fetchStatistic();
        });
        btnLikes.setOnClickListener(v -> {
            currentType = "likes";
            setActiveButton(btnLikes);
            fetchStatistic();
        });
        btnComments.setOnClickListener(v -> {
            currentType = "comments";
            setActiveButton(btnComments);
            fetchStatistic();
        });
        btnPickDate.setOnClickListener(v -> showQuickDateMenu());
    }

    private void setActiveButton(Button activeBtn) {
        btnPlays.setBackgroundTintList(getResources().getColorStateList(R.color.surface));
        btnLikes.setBackgroundTintList(getResources().getColorStateList(R.color.surface));
        btnComments.setBackgroundTintList(getResources().getColorStateList(R.color.surface));
        btnPlays.setTextColor(getResources().getColor(R.color.white));
        btnLikes.setTextColor(getResources().getColor(R.color.white));
        btnComments.setTextColor(getResources().getColor(R.color.white));
        activeBtn.setBackgroundTintList(getResources().getColorStateList(R.color.white));
        activeBtn.setTextColor(getResources().getColor(R.color.black));
    }

    private void showQuickDateMenu() {
        PopupMenu popupMenu = new PopupMenu(getContext(), btnPickDate);
        popupMenu.getMenu().add(0, 1, 0, "Today");
        popupMenu.getMenu().add(0, 2, 1, "Last 7 days");
        popupMenu.getMenu().add(0, 3, 2, "Last 30 days");
        popupMenu.getMenu().add(0, 4, 3, "Last 6 months");
        popupMenu.setOnMenuItemClickListener(item -> {
            LocalDate now = LocalDate.now();
            switch (item.getItemId()) {
                case 1:
                    fromDate = now;
                    toDate = now;
                    updateTextTime("Today");
                    btnPickDate.setText("Today");
                    break;
                case 2:
                    fromDate = now.minusDays(6);
                    toDate = now;
                    updateTextTime("Last 7 days");
                    btnPickDate.setText("Last 7 days");
                    break;
                case 3:
                    fromDate = now.minusDays(29);
                    toDate = now;
                    updateTextTime("Last 30 days");
                    btnPickDate.setText("Last 30 days");
                    break;
                case 4:
                    fromDate = now.minusMonths(6).plusDays(1);
                    toDate = now;
                    updateTextTime("Last 6 months");
                    btnPickDate.setText("Last 6 months");
                    break;
            }
            fetchStatistic();
            return true;
        });
        popupMenu.show();
    }

    private void fetchStatistic() {
        switch (currentType) {
            case "plays":
                fetchPlayStatistic();
                break;
            case "likes":
                fetchLikeStatistic();
                break;
            case "comments":
                fetchCommentStatistic();
                break;
        }
        fetchTopTracks();
    }

    private void fetchPlayStatistic() {
        if ("admin".equals(role)) {
            ApiClient.getStatisticApiService().getAllPlays(
                    fromDate.format(formatter),
                    toDate.format(formatter)
            ).enqueue(new Callback<ApiResponse<PlayResponse>>() {
                @Override
                public void onResponse(Call<ApiResponse<PlayResponse>> call, Response<ApiResponse<PlayResponse>> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                        PlayResponse playResponse = response.body().getData();
                        List<DailyPlay> dailyPlays = playResponse.getDailyPlays();
                        List<UserStatistic> topListeners = playResponse.getTopListenerIds();
                        showBarChartPlays(dailyPlays, topListeners);
                    }
                }
                @Override
                public void onFailure(Call<ApiResponse<PlayResponse>> call, Throwable t) {}
            });
        } else {
            ApiClient.getStatisticApiService().getPlays(
                    fromDate.format(formatter),
                    toDate.format(formatter)
            ).enqueue(new Callback<ApiResponse<PlayResponse>>() {
                @Override
                public void onResponse(Call<ApiResponse<PlayResponse>> call, Response<ApiResponse<PlayResponse>> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                        PlayResponse playResponse = response.body().getData();
                        List<DailyPlay> dailyPlays = playResponse.getDailyPlays();
                        List<UserStatistic> topListeners = playResponse.getTopListenerIds();
                        showBarChartPlays(dailyPlays, topListeners);
                    }
                }
                @Override
                public void onFailure(Call<ApiResponse<PlayResponse>> call, Throwable t) {}
            });
        }
    }

    private void fetchLikeStatistic() {
        if ("admin".equals(role)) {
            ApiClient.getStatisticApiService().getAllLiked(
                    fromDate.format(formatter),
                    toDate.format(formatter)
            ).enqueue(new Callback<ApiResponse<LikeResponse>>() {
                @Override
                public void onResponse(Call<ApiResponse<LikeResponse>> call, Response<ApiResponse<LikeResponse>> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                        LikeResponse likeResponse = response.body().getData();
                        List<DailyLike> dailyLikes = likeResponse.getDailyLikes();
                        List<UserStatistic> whoLiked = likeResponse.getWhoLiked();
                        showBarChartLikes(dailyLikes, whoLiked);
                    }
                }
                @Override
                public void onFailure(Call<ApiResponse<LikeResponse>> call, Throwable t) {}
            });
        } else {
            ApiClient.getStatisticApiService().getLiked(
                    fromDate.format(formatter),
                    toDate.format(formatter)
            ).enqueue(new Callback<ApiResponse<LikeResponse>>() {
                @Override
                public void onResponse(Call<ApiResponse<LikeResponse>> call, Response<ApiResponse<LikeResponse>> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                        LikeResponse likeResponse = response.body().getData();
                        List<DailyLike> dailyLikes = likeResponse.getDailyLikes();
                        List<UserStatistic> whoLiked = likeResponse.getWhoLiked();
                        showBarChartLikes(dailyLikes, whoLiked);
                    }
                }
                @Override
                public void onFailure(Call<ApiResponse<LikeResponse>> call, Throwable t) {}
            });
        }
    }

    private void fetchCommentStatistic() {
        if ("admin".equals(role)) {
            ApiClient.getStatisticApiService().getAllComments(
                    fromDate.format(formatter),
                    toDate.format(formatter)
            ).enqueue(new Callback<ApiResponse<CommentStatisticResponse>>() {
                @Override
                public void onResponse(Call<ApiResponse<CommentStatisticResponse>> call, Response<ApiResponse<CommentStatisticResponse>> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                        CommentStatisticResponse commentResponse = response.body().getData();
                        List<DailyComment> dailyComments = commentResponse.getDailyComments();
                        List<UserStatistic> whoCommented = commentResponse.getWhoCommented();
                        showBarChartComments(dailyComments, whoCommented);
                    }
                }
                @Override
                public void onFailure(Call<ApiResponse<CommentStatisticResponse>> call, Throwable t) {}
            });
        } else {
            ApiClient.getStatisticApiService().getCommentStatistic(
                    fromDate.format(formatter),
                    toDate.format(formatter)
            ).enqueue(new Callback<ApiResponse<CommentStatisticResponse>>() {
                @Override
                public void onResponse(Call<ApiResponse<CommentStatisticResponse>> call, Response<ApiResponse<CommentStatisticResponse>> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                        CommentStatisticResponse commentResponse = response.body().getData();
                        List<DailyComment> dailyComments = commentResponse.getDailyComments();
                        List<UserStatistic> whoCommented = commentResponse.getWhoCommented();
                        showBarChartComments(dailyComments, whoCommented);
                    }
                }
                @Override
                public void onFailure(Call<ApiResponse<CommentStatisticResponse>> call, Throwable t) {}
            });
        }
    }

    private void showBarChartPlays(List<DailyPlay> dailyPlays, List<UserStatistic> topListeners) {
        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        DateTimeFormatter labelFormatter = DateTimeFormatter.ofPattern("MMM dd");
        for (int i = 0; i < dailyPlays.size(); i++) {
            DailyPlay dp = dailyPlays.get(i);
            entries.add(new BarEntry(i, dp.getPlayCount() != null ? dp.getPlayCount() : 0));
            labels.add(dp.getDate() != null ? dp.getDate().format(labelFormatter) : "");
        }
        updateBarChart(entries, labels, "#FE5300");
        if (currentType.equals("plays")) {
            showUserStatistic(topListeners, UserStatisticAdapter.Type.PLAY);
        }
    }

    private void showBarChartLikes(List<DailyLike> dailyLikes, List<UserStatistic> whoLiked) {
        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        DateTimeFormatter labelFormatter = DateTimeFormatter.ofPattern("MMM dd");
        for (int i = 0; i < dailyLikes.size(); i++) {
            DailyLike dl = dailyLikes.get(i);
            entries.add(new BarEntry(i, dl.getLikedCount() != null ? dl.getLikedCount() : 0));
            labels.add(dl.getDate() != null ? dl.getDate().format(labelFormatter) : "");
        }
        updateBarChart(entries, labels, "#FF5722");
        if (currentType.equals("likes")) {
            showUserStatistic(whoLiked, UserStatisticAdapter.Type.LIKE);
        }
    }

    private void showBarChartComments(List<DailyComment> dailyComments, List<UserStatistic> whoCommented) {
        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        DateTimeFormatter labelFormatter = DateTimeFormatter.ofPattern("MMM dd");
        for (int i = 0; i < dailyComments.size(); i++) {
            DailyComment dc = dailyComments.get(i);
            entries.add(new BarEntry(i, dc.getCommentCount() != null ? dc.getCommentCount() : 0));
            labels.add(dc.getDate() != null ? dc.getDate().format(labelFormatter) : "");
        }
        updateBarChart(entries, labels, "#929292");
        if (currentType.equals("comments")) {
            showUserStatistic(whoCommented, UserStatisticAdapter.Type.COMMENT);
        }
    }

    private void updateBarChart(List<BarEntry> entries, List<String> labels, String colorHex) {
        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setColor(Color.parseColor(colorHex));
        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.5f);
        barData.setValueTextColor(Color.WHITE);
        barData.setValueTextSize(12f);
        barChart.setData(barData);
        int grayColor = android.graphics.Color.parseColor("#929292"); // color/gray
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setLabelRotationAngle(-45f);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(labels.size());
        xAxis.setTextColor(grayColor);
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setTextColor(grayColor);
        barChart.invalidate();
    }

    private void showUserStatistic(List<UserStatistic> userStats, UserStatisticAdapter.Type type) {
        userStatisticItems.clear();
        if (userStats == null || userStats.isEmpty()) {
            userStatisticAdapter.notifyDataSetChanged();
            return;
        }
        // Lấy profile từng user rồi add vào list
        for (UserStatistic stat : userStats) {
            ApiClient.getUserProfileApiService().getUserProfile(stat.getUserId())
                    .enqueue(new retrofit2.Callback<com.app.musicapp.model.response.ApiResponse<ProfileWithCountFollowResponse>>() {
                        @Override
                        public void onResponse(retrofit2.Call<com.app.musicapp.model.response.ApiResponse<ProfileWithCountFollowResponse>> call, retrofit2.Response<com.app.musicapp.model.response.ApiResponse<ProfileWithCountFollowResponse>> response) {
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                                userStatisticItems.add(new UserStatisticAdapter.Item(response.body().getData(), stat.getCount()));
                                userStatisticAdapter.notifyDataSetChanged();
                            }
                        }
                        @Override
                        public void onFailure(retrofit2.Call<com.app.musicapp.model.response.ApiResponse<ProfileWithCountFollowResponse>> call, Throwable t) {}
                    });
        }
        userStatisticAdapter = new UserStatisticAdapter(getContext(), userStatisticItems, type);
        rvUserStatistic.setAdapter(userStatisticAdapter);
    }

    private void fetchTopTracks() {
        if ("admin".equals(role)) {
            ApiClient.getStatisticApiService().getAllTopTracks(
                    fromDate.format(formatter),
                    toDate.format(formatter)
            ).enqueue(new retrofit2.Callback<com.app.musicapp.model.response.ApiResponse<List<TopTrack>>>() {
                @Override
                public void onResponse(retrofit2.Call<com.app.musicapp.model.response.ApiResponse<List<TopTrack>>> call, retrofit2.Response<com.app.musicapp.model.response.ApiResponse<List<TopTrack>>> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                        topTrackList.clear();
                        topTrackList.addAll(response.body().getData());
                        topTrackAdapter.notifyDataSetChanged();
                    }
                }
                @Override
                public void onFailure(retrofit2.Call<com.app.musicapp.model.response.ApiResponse<List<TopTrack>>> call, Throwable t) {}
            });
        } else {
            String userId = SharedPreferencesManager.getInstance(requireContext()).getUserId();
            ApiClient.getStatisticApiService().getTopTracks(userId,
                    fromDate.format(formatter),
                    toDate.format(formatter)
            ).enqueue(new retrofit2.Callback<com.app.musicapp.model.response.ApiResponse<List<TopTrack>>>() {
                @Override
                public void onResponse(retrofit2.Call<com.app.musicapp.model.response.ApiResponse<List<TopTrack>>> call, retrofit2.Response<com.app.musicapp.model.response.ApiResponse<List<TopTrack>>> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                        topTrackList.clear();
                        topTrackList.addAll(response.body().getData());
                        topTrackAdapter.notifyDataSetChanged();
                    }
                }
                @Override
                public void onFailure(retrofit2.Call<com.app.musicapp.model.response.ApiResponse<List<TopTrack>>> call, Throwable t) {}
            });
        }
    }
    private void updateTextTime(String textTime) {
        this.textTimeUserStatistic.setText(textTime);
        this.textTimeTopTracks.setText(textTime);

    }
}