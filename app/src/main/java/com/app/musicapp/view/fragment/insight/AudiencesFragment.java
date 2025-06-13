package com.app.musicapp.view.fragment.insight;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.app.musicapp.R;
import com.app.musicapp.adapter.UserStatisticAdapter;
import com.app.musicapp.api.ApiClient;
import com.app.musicapp.model.UserStatistic;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.PlayResponse;
import com.app.musicapp.model.response.ProfileWithCountFollowResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AudiencesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AudiencesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button btnPickDate;
    private TextView textTimeTopListeners;
    private RecyclerView rvTopListeners;
    private UserStatisticAdapter userStatisticAdapter;
    private final List<UserStatisticAdapter.Item> userStatisticItems = new ArrayList<>();
    private LocalDate fromDate, toDate;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private String role = "user";

    public AudiencesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AudiencesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AudiencesFragment newInstance(String role) {
        AudiencesFragment fragment = new AudiencesFragment();
        Bundle args = new Bundle();
        args.putString("role", role);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            role = getArguments().getString("role", "user");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audiences, container, false);
        btnPickDate = view.findViewById(R.id.btnPickDate);
        textTimeTopListeners = view.findViewById(R.id.text_time_top_listeners);
        rvTopListeners = view.findViewById(R.id.rv_top_listeners);
        rvTopListeners.setLayoutManager(new LinearLayoutManager(getContext()));
        userStatisticAdapter = new UserStatisticAdapter(getContext(), userStatisticItems, UserStatisticAdapter.Type.PLAY);
        rvTopListeners.setAdapter(userStatisticAdapter);
        // Mặc định lấy 7 ngày gần nhất
        toDate = LocalDate.now();
        fromDate = toDate.minusDays(6);
        updateTextTime("Last 7 days");
        fetchTopListeners();
        btnPickDate.setOnClickListener(v -> showQuickDateMenu());
        return view;
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
            fetchTopListeners();
            return true;
        });
        popupMenu.show();
    }

    private void updateTextTime(String textTime) {
        this.textTimeTopListeners.setText(textTime);
    }

    private void fetchTopListeners() {
        if ("admin".equals(role)) {
            ApiClient.getStatisticApiService().getAllPlays(
                    fromDate.format(formatter),
                    toDate.format(formatter)
            ).enqueue(new retrofit2.Callback<ApiResponse<PlayResponse>>() {
                @Override
                public void onResponse(retrofit2.Call<ApiResponse<PlayResponse>> call, retrofit2.Response<ApiResponse<PlayResponse>> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                        List<UserStatistic> topListeners = response.body().getData().getTopListenerIds();
                        userStatisticItems.clear();
                        if (topListeners != null) {
                            for (UserStatistic stat : topListeners) {
                                fetchUserProfileAndAdd(stat);
                            }
                        }
                        userStatisticAdapter.notifyDataSetChanged();
                    }
                }
                @Override
                public void onFailure(retrofit2.Call<ApiResponse<PlayResponse>> call, Throwable t) {}
            });
        } else {
            ApiClient.getStatisticApiService().getPlays(
                    fromDate.format(formatter),
                    toDate.format(formatter)
            ).enqueue(new retrofit2.Callback<ApiResponse<PlayResponse>>() {
                @Override
                public void onResponse(retrofit2.Call<ApiResponse<PlayResponse>> call, retrofit2.Response<ApiResponse<PlayResponse>> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                        List<UserStatistic> topListeners = response.body().getData().getTopListenerIds();
                        userStatisticItems.clear();
                        if (topListeners != null) {
                            for (UserStatistic stat : topListeners) {
                                fetchUserProfileAndAdd(stat);
                            }
                        }
                        userStatisticAdapter.notifyDataSetChanged();
                    }
                }
                @Override
                public void onFailure(retrofit2.Call<ApiResponse<PlayResponse>> call, Throwable t) {}
            });
        }
    }

    private void fetchUserProfileAndAdd(UserStatistic stat) {
        ApiClient.getUserProfileApiService().getUserProfile(stat.getUserId())
                .enqueue(new retrofit2.Callback<ApiResponse<ProfileWithCountFollowResponse>>() {
                    @Override
                    public void onResponse(retrofit2.Call<ApiResponse<ProfileWithCountFollowResponse>> call, retrofit2.Response<ApiResponse<ProfileWithCountFollowResponse>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                            userStatisticItems.add(new UserStatisticAdapter.Item(response.body().getData(), stat.getCount()));
                            userStatisticAdapter.notifyDataSetChanged();
                        }
                    }
                    @Override
                    public void onFailure(retrofit2.Call<ApiResponse<ProfileWithCountFollowResponse>> call, Throwable t) {}
                });
    }
}