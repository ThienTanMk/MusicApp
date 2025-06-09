package com.app.musicapp.view.fragment.album;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.app.musicapp.R;
import com.app.musicapp.adapter.album.AlbumAdapter;
import com.app.musicapp.api.ApiClient;
import com.app.musicapp.helper.SharedPreferencesManager;
import com.app.musicapp.model.response.AlbumResponse;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.interfaces.OnLikeChangeListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlbumsFragment extends Fragment implements OnLikeChangeListener {

    private ImageView ivBack;
    private EditText etSearch;
    private ListView listViewAlbums;
    private AlbumAdapter albumAdapter;
    private List<AlbumResponse> albumList;
    private List<AlbumResponse> filteredAlbumList;
    private ProgressBar progressBar;

    public AlbumsFragment() {
        // Required empty public constructor
    }

    public static AlbumsFragment newInstance() {
        return new AlbumsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_albums, container, false);

        // Ánh xạ views
        listViewAlbums = view.findViewById(R.id.listViewAlbums);
        etSearch = view.findViewById(R.id.et_search);
        ivBack = view.findViewById(R.id.iv_back);
        progressBar = view.findViewById(R.id.progressBar);

        // Khởi tạo danh sách album
        albumList = new ArrayList<>();
        filteredAlbumList = new ArrayList<>();

        // Thiết lập adapter
        albumAdapter = new AlbumAdapter(getContext(), filteredAlbumList);
        albumAdapter.setOnLikeChangeListener(this);
        listViewAlbums.setAdapter(albumAdapter);

        // Load albums from API
        loadAlbums();

        // Xử lý nút Quay lại
        ivBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                View mainView = requireActivity().findViewById(R.id.main);
                View viewPager = mainView.findViewById(R.id.view_pager);
                View fragmentContainer = mainView.findViewById(R.id.fragment_container);

                viewPager.setVisibility(View.VISIBLE);
                fragmentContainer.setVisibility(View.GONE);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // Xử lý sự kiện click trên ListView
        listViewAlbums.setOnItemClickListener((parent, view1, position, id) -> {
            AlbumResponse album = filteredAlbumList.get(position);
            AlbumOptionsBottomSheet bottomSheet = AlbumOptionsBottomSheet.newInstance(album);
            bottomSheet.setOnLikeChangeListener(this);
            AlbumPageFragment fragment = AlbumPageFragment.newInstance(album);
            if (getActivity() != null) {
                // Ẩn ViewPager và hiển thị fragment_container
                View mainView = requireActivity().findViewById(R.id.main);
                View viewPager = mainView.findViewById(R.id.view_pager);
                View fragmentContainer = mainView.findViewById(R.id.fragment_container);

                viewPager.setVisibility(View.GONE);
                fragmentContainer.setVisibility(View.VISIBLE);

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterAlbums(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    private void loadAlbums() {
        progressBar.setVisibility(View.VISIBLE);
        String userId = SharedPreferencesManager.getInstance(requireContext()).getUserId();
        
        if (userId == null) {
            Toast.makeText(requireContext(), "Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
            return;
        }
        
        ApiClient.getAlbumService().getCreatedAndLikedAlbums(userId).enqueue(new Callback<ApiResponse<List<AlbumResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<AlbumResponse>>> call, Response<ApiResponse<List<AlbumResponse>>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    albumList.clear();
                    albumList.addAll(response.body().getData());
                    filteredAlbumList.clear();
                    filteredAlbumList.addAll(albumList);
                    albumAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Không thể tải danh sách album", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<AlbumResponse>>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterAlbums(String query) {
        filteredAlbumList.clear();
        if (query.isEmpty()) {
            filteredAlbumList.addAll(albumList);
        } else {
            filteredAlbumList.addAll(albumList.stream()
                    .filter(album -> album.getAlbumTitle().toLowerCase().contains(query.toLowerCase()) ||
                            album.getMainArtists().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList()));
        }
        albumAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLikeChanged(String albumId, boolean isLiked) {
        // Update both lists
        for (AlbumResponse album : albumList) {
            if (album.getId().equals(albumId)) {
                album.setIsLiked(isLiked);
            }
        }
        for (AlbumResponse album : filteredAlbumList) {
            if (album.getId().equals(albumId)) {
                album.setIsLiked(isLiked);
            }
        }
        // Notify adapter to refresh UI
        albumAdapter.notifyDataSetChanged();
    }
}