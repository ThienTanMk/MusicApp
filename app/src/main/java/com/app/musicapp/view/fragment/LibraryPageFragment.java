package com.app.musicapp.view.fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.musicapp.R;
import com.app.musicapp.adapter.LibraryListAdapter;
import com.app.musicapp.adapter.track.TrackAdapter;
import com.app.musicapp.api.ApiClient;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.ListView.LibraryList;
import com.app.musicapp.model.response.TrackResponse;
import com.app.musicapp.view.fragment.album.AlbumsFragment;
import com.app.musicapp.view.fragment.follow.FollowingFragment;
import com.app.musicapp.view.fragment.playlist.PlaylistsFragment;
import com.app.musicapp.view.fragment.track.LikedTracksFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LibraryPageFragment extends Fragment {

    private ListView listViewMenu;
    private ListView listViewHistory;
    private LibraryListAdapter menuAdapter;
    private TrackAdapter historyAdapter;
    private TextView seeAllTextView;
    private List<LibraryList> libraryLists;
    private List<TrackResponse> trackResponseList;
    public LibraryPageFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_library_page, container, false);

        seeAllTextView = view.findViewById(R.id.tv_see_all);
        // Khởi tạo ListView
        listViewMenu = view.findViewById(R.id.listViewMenu);

        // Tạo danh sách dữ liệu (dựa trên ảnh)
        libraryLists = new ArrayList<>();
        libraryLists.add(new LibraryList("Liked tracks"));
        libraryLists.add(new LibraryList("Playlists"));
        libraryLists.add(new LibraryList("Albums"));
        libraryLists.add(new LibraryList("Following"));
        libraryLists.add(new LibraryList("Your insights"));
        libraryLists.add(new LibraryList("Your uploads"));

        // Khởi tạo và gắn adapter vào ListView
        menuAdapter = new LibraryListAdapter(requireContext(), libraryLists);
        listViewMenu.setAdapter(menuAdapter);

        // Xử lý sự kiện click trên ListView
        listViewMenu.setOnItemClickListener((parent, v, position, id) -> {
            LibraryList selectedItem = libraryLists.get(position);
            String itemName = selectedItem.getName();

            // Ẩn ViewPager và BottomNavigationView, hiển thị fragment_container
            View mainView = requireActivity().findViewById(R.id.main);
            View viewPager = mainView.findViewById(R.id.view_pager);
            View bottomNavigation = mainView.findViewById(R.id.bottom_navigation);
            View fragmentContainer = mainView.findViewById(R.id.fragment_container);

            viewPager.setVisibility(View.GONE);
            fragmentContainer.setVisibility(View.VISIBLE);

            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();

            // Điều hướng đến Fragment tương ứng
            switch (itemName) {
                case "Liked tracks":
                    LikedTracksFragment likedTracksFragment = new LikedTracksFragment();
                    transaction.replace(R.id.fragment_container, likedTracksFragment);
                    break;
                case "Playlists":
                    PlaylistsFragment playlistsFragment = new PlaylistsFragment();
                    transaction.replace(R.id.fragment_container, playlistsFragment);
                    break;
                case "Albums":
                    AlbumsFragment albumsFragment = AlbumsFragment.newInstance();
                    transaction.replace(R.id.fragment_container, albumsFragment);
                    break;
                case "Following":
                    FollowingFragment followingFragment = FollowingFragment.newInstance();
                    transaction.replace(R.id.fragment_container, followingFragment);
                    break;
                case "Your uploads":
                    UploadsFragment uploadsFragment = UploadsFragment.newInstance();
                    transaction.replace(R.id.fragment_container, uploadsFragment);
                    break;
                default:
                    Toast.makeText(requireContext(), "Clicked: " + itemName, Toast.LENGTH_SHORT).show();
                    return;

            }

            transaction.addToBackStack(null);
            transaction.commit();
        });
        // Khởi tạo ListView cho Listening history
        listViewHistory = view.findViewById(R.id.listViewHistory);

        trackResponseList = new ArrayList<>();
        getHistory();
        // Khởi tạo và gắn adapter vào ListView history
        historyAdapter = new TrackAdapter(this, trackResponseList);
        listViewHistory.setAdapter(historyAdapter);

        seeAllTextView.setOnClickListener(v -> {
            FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new HistoryFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        return view;
    }

    private void getHistory(){
        ApiClient.getHistoryApiService().getAllHistory().enqueue(new Callback<ApiResponse<List<TrackResponse>>>() {

            @Override
            public void onResponse(Call<ApiResponse<List<TrackResponse>>> call, Response<ApiResponse<List<TrackResponse>>> response) {
                if(response.isSuccessful()&&response.body()!=null){
                    trackResponseList.clear();
                    trackResponseList.addAll(response.body().getData());
                    historyAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<TrackResponse>>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getHistory();
    }
}