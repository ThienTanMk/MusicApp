package com.app.musicapp.view.fragment.playlist;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.*;

import android.widget.*;

import com.app.musicapp.R;
import com.app.musicapp.adapter.playlist.PlaylistAdapter;
import com.app.musicapp.api.ApiClient;
import com.app.musicapp.helper.SharedPreferencesManager;
import com.app.musicapp.interfaces.OnLikeChangeListener;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.GenreResponse;
import com.app.musicapp.model.response.PlaylistResponse;
import com.app.musicapp.view.activity.SignIn;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.time.LocalDateTime;
import java.util.*;
import retrofit2.*;


public class PlaylistsFragment extends Fragment implements OnLikeChangeListener {
    private ListView listViewPlaylists;
    private PlaylistAdapter playlistAdapter;
    private ProgressBar progressBar;
    private List<PlaylistResponse> playlists = new ArrayList<>(); // Danh sách chứa cả Playlist và LikedPlaylist

    public PlaylistsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlists, container, false);

        listViewPlaylists = view.findViewById(R.id.listViewPlaylists);
        progressBar = view.findViewById(R.id.progressBar);

        playlistAdapter = new PlaylistAdapter(this, playlists);
        listViewPlaylists.setAdapter(playlistAdapter);

        loadPlaylists();

        // Xử lý sự kiện bấm nút Back
        ImageView ivBack = view.findViewById(R.id.iv_back);
        ivBack.setOnClickListener(v -> {
            // Hiển thị lại ViewPager
            View mainView = requireActivity().findViewById(R.id.main);
            View viewPager = mainView.findViewById(R.id.view_pager);
            View fragmentContainer = mainView.findViewById(R.id.fragment_container);

            viewPager.setVisibility(View.VISIBLE);
            fragmentContainer.setVisibility(View.GONE);

            // Quay lại fragment trước đó
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        // Xử lý sự kiện bấm nút Create playlist
        LinearLayout layoutCreatePlaylist = view.findViewById(R.id.layout_create_playlist);
        layoutCreatePlaylist.setOnClickListener(v -> {
            // Tạo dialog
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
            bottomSheetDialog.setContentView(R.layout.bottom_sheet_create_playlist);

            // Ánh xạ các view trong dialog
            ImageView ivClose = bottomSheetDialog.findViewById(R.id.iv_close);
            TextView tvSave = bottomSheetDialog.findViewById(R.id.tv_save);
            EditText etPlaylistTitle = bottomSheetDialog.findViewById(R.id.et_playlist_title);
            SwitchCompat switchMakePublic = bottomSheetDialog.findViewById(R.id.switch_make_public);

            // Xử lý nút Close
            ivClose.setOnClickListener(v1 -> bottomSheetDialog.dismiss());

            // Xử lý nút Save
            tvSave.setOnClickListener(v1 -> {
                String title = etPlaylistTitle.getText().toString().trim();
                if (title.isEmpty()) {
                    Toast.makeText(getContext(), "Tiêu đề Playlist không được trống", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Tạo playlist mới
                String privacy = switchMakePublic.isChecked() ? "public" : "private";
                PlaylistResponse newPlaylistResponse = new PlaylistResponse(
                        String.valueOf(playlists.size() + 1),
                        title,
                        LocalDateTime.now(),
                        "Created by user1",
                        privacy,
                        "user1",
                        new GenreResponse("1","Rock",LocalDateTime.now()),
                        null,
                        LocalDateTime.now(),
                        new ArrayList<>(),
                        new ArrayList<>(),false,null
                );

                // Thêm playlist mới vào danh sách
                playlists.add(0, newPlaylistResponse); // Thêm vào đầu danh sách
                playlistAdapter.notifyDataSetChanged();

                Toast.makeText(getContext(), "Playlist created: " + title, Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            });
            bottomSheetDialog.show();
        });

        return view;
    }

    private void loadPlaylists() {
        progressBar.setVisibility(View.VISIBLE);
        String userId = SharedPreferencesManager.getInstance(requireContext()).getUserId();

        if (userId == null) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(requireContext(), "Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(requireContext(), SignIn.class));
            requireActivity().finish();
            return;
        }
        Log.d("PlaylistsFragment", "Fetching playlists for userId: " + userId);
        ApiClient.getPlaylistService().getAllPlaylists().enqueue(new Callback<ApiResponse<List<PlaylistResponse>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<PlaylistResponse>>> call, @NonNull Response<ApiResponse<List<PlaylistResponse>>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    playlists.clear();
                    playlists.addAll(response.body().getData());
                    playlistAdapter.notifyDataSetChanged();
                } else if (response.body() != null && response.body().getCode() == 1401) {
                    Toast.makeText(requireContext(), "Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(requireContext(), SignIn.class));
                    requireActivity().finish();
                } else {
                    Toast.makeText(requireContext(), "Không thể tải danh sách playlist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<PlaylistResponse>>> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(requireContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onLikeChanged(String playlistId, boolean isLiked) {
        for (int i = 0; i < playlists.size(); i++) {
            PlaylistResponse playlist = playlists.get(i);
            if (playlist.getId().equals(playlistId)) {
                if (!isLiked) {
                    playlists.remove(i);
                    playlistAdapter.notifyDataSetChanged();
                    Log.d("PlaylistsFragment", "Removed playlist id=" + playlistId + " from list");
                } else {
                    playlist.setIsLiked(true);
                    playlistAdapter.notifyDataSetChanged();
                    Log.d("PlaylistsFragment", "Updated isLiked=true for playlist id=" + playlistId);
                }
                break;
            }
        }
    }
}