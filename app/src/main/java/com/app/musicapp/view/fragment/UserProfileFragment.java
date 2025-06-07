package com.app.musicapp.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.app.musicapp.R;
import com.app.musicapp.adapter.ProfileContentAdapter;
import com.app.musicapp.model.AlbumResponse;
import com.app.musicapp.model.FollowingUser;
import com.app.musicapp.model.response.GenreResponse;
import com.app.musicapp.model.response.LikedPlaylistResponse;
import com.app.musicapp.model.response.LikedTrackResponse;
import com.app.musicapp.model.response.PlaylistResponse;
import com.app.musicapp.model.response.TrackResponse;
import com.app.musicapp.model.response.ProfileWithCountFollowResponse;
import com.app.musicapp.model.response.TagResponse;
import com.bumptech.glide.Glide;

import java.time.LocalDateTime;
import java.util.*;

public class UserProfileFragment extends Fragment {

    private ProfileWithCountFollowResponse profile;
    private FollowingUser followingUser;
    private TextView tvUsername;
    private TextView tvFollowInfo;
    private ImageView imgAvatar;
    private Button btnFollowing;
    private ImageView btnEmail;
    private ImageView btnShare;
    private ImageView btnPlay;
    private ImageView ivBack;
    private RecyclerView rvProfileContent;
    private String source;

    public UserProfileFragment() {
        // Required empty public constructor
    }
    public static UserProfileFragment newInstance(ProfileWithCountFollowResponse profile, String source) {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable("profile", profile);
        args.putString("source", source);
        fragment.setArguments(args);
        return fragment;
    }

    public static UserProfileFragment newInstance(FollowingUser followingUser, String source) {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable("following_user", followingUser);
        args.putString("source", source);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            profile = (ProfileWithCountFollowResponse) getArguments().getSerializable("profile");
            followingUser = (FollowingUser) getArguments().getSerializable("following_user");
            source = getArguments().getString("source", "");
        }

        // Xử lý nút Back của điện thoại
//        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                if ("search".equals(source) && getParentFragmentManager().getBackStackEntryCount() > 1) {
//                    getParentFragmentManager().popBackStack();
//                } else {
//                    getParentFragmentManager().popBackStack();
//                    View viewPager = requireActivity().findViewById(R.id.view_pager);
//                    View fragmentContainer = requireActivity().findViewById(R.id.fragment_container);
//                    if (viewPager != null && fragmentContainer != null) {
//                        viewPager.setVisibility(View.VISIBLE);
//                        fragmentContainer.setVisibility(View.GONE);
//                    }
//                    RecyclerView recyclerView = requireActivity().findViewById(R.id.recyclerViewSearchUser);
//                    if (recyclerView != null) {
//                        recyclerView.setVisibility(View.GONE);
//                    }
//                    GridView gridView = requireActivity().findViewById(R.id.gridViewVibes);
//                    TextView textView = requireActivity().findViewById(R.id.textViewVibes);
//                    if (gridView != null && textView != null) {
//                        gridView.setVisibility(View.VISIBLE);
//                        textView.setVisibility(View.VISIBLE);
//                    }
//                }
//            }
//        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        // Ánh xạ view
        tvUsername = view.findViewById(R.id.tv_username);
        tvFollowInfo = view.findViewById(R.id.tv_follow_info);
        imgAvatar = view.findViewById(R.id.img_avatar);
        ivBack = view.findViewById(R.id.iv_back);
        btnFollowing = view.findViewById(R.id.btn_following);
        btnEmail = view.findViewById(R.id.btn_email);
        btnShare = view.findViewById(R.id.btn_share);
        btnPlay = view.findViewById(R.id.btn_play);
        rvProfileContent = view.findViewById(R.id.rv_profile_content);

        // Gán dữ liệu
        if (profile != null) {
            // Hiển thị thông tin từ ProfileWithCountFollowResponse
            tvUsername.setText(profile.getDisplayName() != null ? profile.getDisplayName() : "Unknown");
            tvFollowInfo.setText(profile.getFollowerCount() + " Followers • " + profile.getFollowingCount() + " Following");
            Glide.with(this).load(profile.getAvatar() != null && !profile.getAvatar().isEmpty() ? profile.getAvatar() : R.drawable.logo).placeholder(R.drawable.logo).into(imgAvatar);
        } else if (followingUser != null) {
            // Hiển thị thông tin từ FollowingUser
            tvUsername.setText(followingUser.getName() != null ? followingUser.getName() : "Unknown");
            tvFollowInfo.setText(followingUser.getFollowersCount() + " Followers • N/A Following"); // FollowingUser không có followingCount
            Glide.with(this).load(followingUser.getAvatarUrl() != null && !followingUser.getAvatarUrl().isEmpty() ? followingUser.getAvatarUrl() : R.drawable.logo).placeholder(R.drawable.logo).into(imgAvatar);
        } else {
            // Trường hợp không có dữ liệu
            tvUsername.setText("Unknown User");
            tvFollowInfo.setText("0 Followers • 0 Following");
            imgAvatar.setImageResource(R.drawable.logo);
        }
//        ivBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());
//        ivBack.setOnClickListener(v -> {
//            if (getActivity() != null) {
//                String source = getArguments() != null ? getArguments().getString("source", "") : "";
//
//                if ("search".equals(source)) {
//                    getActivity().onBackPressed();
//                } else if ("following".equals(source)) {
//                    Fragment targetFragment = new FollowingFragment();
//                    getActivity().getSupportFragmentManager()
//                            .beginTransaction()
//                            .replace(R.id.fragment_container, targetFragment)
//                            .addToBackStack(null)
//                            .commit();
//                } else {
//                    getActivity().onBackPressed();
//                }
//            }
//        });

        ivBack.setOnClickListener(v -> {
            if ("search".equals(source) && getParentFragmentManager().getBackStackEntryCount() > 1) {
                getParentFragmentManager().popBackStack();
            } else {
                getParentFragmentManager().popBackStack();
                View viewPager = requireActivity().findViewById(R.id.view_pager);
                View fragmentContainer = requireActivity().findViewById(R.id.fragment_container);
                if (viewPager != null && fragmentContainer != null) {
                    viewPager.setVisibility(View.VISIBLE);
                    fragmentContainer.setVisibility(View.GONE);
                }
                RecyclerView recyclerView = requireActivity().findViewById(R.id.recyclerViewSearchUser);
                if (recyclerView != null) {
                    recyclerView.setVisibility(View.GONE);
                }
                GridView gridView = requireActivity().findViewById(R.id.gridViewVibes);
                TextView textView = requireActivity().findViewById(R.id.textViewVibes);
                if (gridView != null && textView != null) {
                    gridView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                }
            }
        });

        btnFollowing.setOnClickListener(v -> {
            if (profile != null) {
            } else if (followingUser != null) {
                followingUser.setFollowing(!followingUser.isFollowing());
                btnFollowing.setText(followingUser.isFollowing() ? "Following" : "Follow");
            }
        });

        btnEmail.setOnClickListener(v -> {
            if (profile != null && profile.getEmail() != null) {
            }
        });

        btnShare.setOnClickListener(v -> {
            // Logic cho nút Share
        });

        btnPlay.setOnClickListener(v -> {
            // Logic cho nút Play
        });

        // Cấu hình RecyclerView cho các section
        rvProfileContent.setLayoutManager(new LinearLayoutManager(getContext()));
        List<Object> contentItems = new ArrayList<>();

        // Top Tracks
        contentItems.add("Top Tracks");
        contentItems.addAll(mockTopTracks());

        // Tracks
        contentItems.add("Tracks");
        contentItems.addAll(mockTracks());
        contentItems.add("See All");

        // Albums
        contentItems.add("Albums");
        contentItems.addAll(mockAlbums());

        // Playlists
        contentItems.add("Playlists");
        contentItems.addAll(mockPlaylists());

        // Likes
        contentItems.add("Likes");
        contentItems.addAll(mockLikedTracks());
        contentItems.addAll(mockLikedPlaylists());
        contentItems.add("See All");

        ProfileContentAdapter adapter = new ProfileContentAdapter(this, contentItems);
        rvProfileContent.setAdapter(adapter);

        return view;
    }
    private List<TrackResponse> mockTopTracks() {
        List<TrackResponse> trackResponses = new ArrayList<>();
        trackResponses.add(new TrackResponse("1", "Bản ghi mới 14", "Description", "file1.mp3", "cover1.jpg",
                LocalDateTime.now(), "user123", "4:18", "public", 10,
                new GenreResponse("1","Rock",LocalDateTime.now()),
                List.of(new TagResponse("1", "pop", LocalDateTime.now(), "user123"))));
        trackResponses.add(new TrackResponse("2", "N21DCCN089 Tran Viet Quang", "Description", "file2.mp3", "cover2.jpg",
                LocalDateTime.now(), "user123", "2:12", "public", 5,
                new GenreResponse("1","Rock",LocalDateTime.now()),
                List.of(new TagResponse("2", "rock", LocalDateTime.now(), "user123"))));
        return trackResponses;
    }

    private List<TrackResponse> mockTracks() {
        List<TrackResponse> trackResponses = new ArrayList<>();
        trackResponses.add(new TrackResponse("3", "Track 1", "Artist 1", "file3.mp3", "cover3.jpg",
                LocalDateTime.now(), "user123", "3:45", "public", 8,
                new GenreResponse("1","Rock",LocalDateTime.now()),
                List.of(new TagResponse("3", "pop", LocalDateTime.now(), "user123"))));
        trackResponses.add(new TrackResponse("4", "Track 2", "Artist 2", "file4.mp3", "cover4.jpg",
                LocalDateTime.now(), "user123", "4:00", "public", 12,
                new GenreResponse("1","Rock",LocalDateTime.now()),
                List.of(new TagResponse("4", "hiphop", LocalDateTime.now(), "user123"))));
        return trackResponses;
    }

    private List<AlbumResponse> mockAlbums() {
        List<AlbumResponse> albumResponses = new ArrayList<>();
        albumResponses.add(new AlbumResponse(
                "Album 1",                  // albumTitle
                "Artist A",                 // mainArtists
                "1",                        // genreId
                "Single",                   // albumType
                new ArrayList<>(),          // tags
                "Description",             // description
                "public",                  // privacy
                "http://example.com/1",    // albumLink
                "album1.jpg",              // imagePath
                "user123",                 // userId
                "1",                       // id
                LocalDateTime.now(),       // createdAt
                new ArrayList<>(),         // tracks
                new GenreResponse("1", "Rock", LocalDateTime.now()) // genre
        ));

        albumResponses.add(new AlbumResponse(
                "Album 2", "Artist B", "1", "Album",
                new ArrayList<>(), "Description", "public", "http://example.com/2",
                "album2.jpg", "user123", "2", LocalDateTime.now(),
                new ArrayList<>(), new GenreResponse("1", "Rock", LocalDateTime.now())
        ));
        return albumResponses;
    }

    private List<PlaylistResponse> mockPlaylists() {
        List<PlaylistResponse> playlistResponses = new ArrayList<>();
        playlistResponses.add(new PlaylistResponse("1", "Playlist 1", LocalDateTime.now(), "Description",
                "public", "user123",
                new GenreResponse("1","Rock",LocalDateTime.now()),
                "playlist1.jpg", LocalDateTime.now(), new ArrayList<>(),
                new ArrayList<>(),false,null));
        playlistResponses.add(new PlaylistResponse("2", "Playlist 2", LocalDateTime.now(), "Description",
                "public", "user123",
                new GenreResponse("1","Rock",LocalDateTime.now()),
                "playlist2.jpg", LocalDateTime.now(), new ArrayList<>(), new ArrayList<>(),false,null));
        return playlistResponses;
    }

    private List<LikedTrackResponse> mockLikedTracks() {
        List<LikedTrackResponse> likedTrackResponses = new ArrayList<>();
        likedTrackResponses.add(new LikedTrackResponse("1", "3", "user123"));
        likedTrackResponses.add(new LikedTrackResponse("2", "4", "user123"));
        return likedTrackResponses;
    }

    private List<LikedPlaylistResponse> mockLikedPlaylists() {
        List<LikedPlaylistResponse> likedPlaylistResponses = new ArrayList<>();
        likedPlaylistResponses.add(new LikedPlaylistResponse("1", "user123", LocalDateTime.now(),
                mockPlaylists().get(0))); // Playlist 1
        likedPlaylistResponses.add(new LikedPlaylistResponse("2", "user123", LocalDateTime.now(),
                mockPlaylists().get(1))); // Playlist 2
        return likedPlaylistResponses;
    }
}