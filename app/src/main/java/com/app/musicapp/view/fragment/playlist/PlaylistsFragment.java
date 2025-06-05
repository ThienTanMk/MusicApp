package com.app.musicapp.view.fragment.playlist;

import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.view.*;

import android.widget.*;

import com.app.musicapp.R;
import com.app.musicapp.adapter.PlaylistAdapter;
import com.app.musicapp.model.*;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.time.LocalDateTime;
import java.util.*;


public class PlaylistsFragment extends Fragment {

    private ListView listViewPlaylists;
    private PlaylistAdapter playlistAdapter;
    private List<Object> playlists; // Danh sách chứa cả Playlist và LikedPlaylist

    public PlaylistsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlists, container, false);

        // Ánh xạ ListView
        listViewPlaylists = view.findViewById(R.id.listViewPlaylists);

        playlists = new ArrayList<>();

        Playlist playlist1 = new Playlist(
                "1",
                "Weekly Wave",
                LocalDateTime.now(),
                "Made for ThienTanVCK",
                "public",
                "user1",
                new Genre("1","Rock",LocalDateTime.now()),
                "cover1.jpg",
                LocalDateTime.now(),
                new ArrayList<>(), // Không có bài hát
                Arrays.asList(new Tag("tag1", "weekly", LocalDateTime.now(), "user1"))
        );
        playlists.add(playlist1);

        // Playlist được người dùng thích từ người khác
        List<Track> diracTracks = new ArrayList<>();
        for (int i = 1; i <= 61; i++) {
            diracTracks.add(new Track(
                    String.valueOf(i),
                    "Track " + i,
                    "Artist " + i,
                    "Description " + i,
                    "cover" + i + ".jpg",
                    LocalDateTime.now(),
                    "Artist " + i,
                    "3:00",
                    "public",
                    (int) (Math.random() * 100000),
                    new Genre("1","Rock",LocalDateTime.now()),
                    Arrays.asList(new Tag("tag2" , "gentlebad", LocalDateTime.now(), "user3"))
            ));
        }
        Playlist playlist2 = new Playlist(
                "2",
                "Dirac Reverse Station",
                LocalDateTime.now(),
                "Made for freedommalaysia",
                "public",
                "user2",
                new Genre("1","Rock",LocalDateTime.now()),
                "cover2.jpg",
                LocalDateTime.now(),
                diracTracks, // 61 Tracks
                Arrays.asList(new Tag("tag2", "dirac", LocalDateTime.now(), "user2"))
        );
        playlists.add(new LikedPlaylist("lp1", "user1", LocalDateTime.now(), playlist2));

        // Playlist được người dùng thích từ người khác
        List<Track> gentleBadTracks = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            gentleBadTracks.add(new Track(
                    String.valueOf(i + 61),
                    "Gentle Bad Track " + i,
                    "GURBANE",
                    "Description " + (i + 61),
                    "cover" + (i + 61) + ".jpg",
                    LocalDateTime.now(),
                    "GURBANE",
                    "3:00",
                    "public",
                    (int) (Math.random() * 100000),
                    new Genre("1","Rock",LocalDateTime.now()),
                    Arrays.asList(new Tag("tag", "gentlebad", LocalDateTime.now(), "user3"))
            ));
        }
        Playlist playlist3 = new Playlist(
                "3",
                "\"Gentle Bad\" the EP - GURBANE",
                LocalDateTime.now(),
                "Description 3",
                "public",
                "user3",
                new Genre("1","Rock",LocalDateTime.now()),
                "cover3.jpg",
                LocalDateTime.now(),
                gentleBadTracks, // 5 Tracks
                Arrays.asList(new Tag("tag3", "gentlebad", LocalDateTime.now(), "user3"))
        );
        playlists.add(new LikedPlaylist("lp2", "user1", LocalDateTime.now(), playlist3));

        // Khởi tạo và gắn adapter vào ListView
        playlistAdapter = new PlaylistAdapter(getContext(), playlists);
        listViewPlaylists.setAdapter(playlistAdapter);

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
                    Toast.makeText(getContext(), "Playlist title cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Tạo playlist mới
                String privacy = switchMakePublic.isChecked() ? "public" : "private";
                Playlist newPlaylist = new Playlist(
                        String.valueOf(playlists.size() + 1),
                        title,
                        LocalDateTime.now(),
                        "Created by user1",
                        privacy,
                        "user1",
                        new Genre("1","Rock",LocalDateTime.now()),
                        null,
                        LocalDateTime.now(),
                        new ArrayList<>(),
                        new ArrayList<>()
                );

                // Thêm playlist mới vào danh sách
                playlists.add(0, newPlaylist); // Thêm vào đầu danh sách
                playlistAdapter.notifyDataSetChanged();

                Toast.makeText(getContext(), "Playlist created: " + title, Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            });
            bottomSheetDialog.show();
        });

        return view;
    }
}