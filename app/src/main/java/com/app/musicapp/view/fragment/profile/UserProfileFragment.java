package com.app.musicapp.view.fragment.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.app.musicapp.R;
import com.app.musicapp.adapter.album.AlbumAdapter;
import com.app.musicapp.adapter.album.AlbumRVAdapter;
import com.app.musicapp.adapter.playlist.PlayListRVAdapter;
import com.app.musicapp.adapter.playlist.PlaylistAdapter;
import com.app.musicapp.adapter.track.TrackAdapter;
import com.app.musicapp.api.ApiClient;
import com.app.musicapp.helper.SharedPreferencesManager;
import com.app.musicapp.helper.UrlHelper;
import com.app.musicapp.model.request.AddFollowRequest;
import com.app.musicapp.model.response.AlbumResponse;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.PageFollowResponse;
import com.app.musicapp.model.response.PlaylistResponse;
import com.app.musicapp.model.response.TrackResponse;
import com.app.musicapp.model.response.ProfileWithCountFollowResponse;
import com.app.musicapp.view.fragment.LibraryPageFragment;
import com.app.musicapp.view.fragment.UploadsFragment;
import com.app.musicapp.view.fragment.album.AlbumPageFragment;
import com.app.musicapp.view.fragment.follow.FollowerFragment;
import com.app.musicapp.view.fragment.follow.FollowingFragment;
import com.app.musicapp.view.fragment.playlist.PlaylistPageFragment;
import com.app.musicapp.view.fragment.playlist.PlaylistsFragment;
import com.bumptech.glide.Glide;

import java.util.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileFragment extends Fragment {
    private static final String ARG_USER_ID = "user_id";
    private static final String ARG_SOURCE = "source";
    private String userId, source;
    private ProfileWithCountFollowResponse currentProfile;
    private ImageView ivAvatar, ivCover, ivBack, ivEdit;
    private TextView tvDisplayName, tvFollowerCount, tvFollowingCount, tvSeeAll, tvNoTracks ,tvNoPlaylists, tvNoAlbums;
    private Button btnFollow;
    private ListView lvTracks;
    private RecyclerView rvPlaylists, rvAlbums;
    private TrackAdapter trackAdapter;
    private PlayListRVAdapter playlistAdapter;
    private AlbumRVAdapter albumAdapter;

    public UserProfileFragment() {
        // Required empty public constructor
    }
    public static UserProfileFragment newInstance(String userId, String source) {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_ID, userId);
        args.putString(ARG_SOURCE, source);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString(ARG_USER_ID);
            source = getArguments().getString(ARG_SOURCE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        initViews(view);
        initAdapters();
        loadUserProfile();
        loadUserProfileData();
        loadFollowCounts();
        setupBackButton();

        return view;
    }
    private void initViews(View view) {
        ivAvatar = view.findViewById(R.id.img_avatar);
        ivCover = view.findViewById(R.id.img_background);
        ivBack = view.findViewById(R.id.iv_back);
        ivEdit = view.findViewById(R.id.iv_edit);
        tvDisplayName = view.findViewById(R.id.tv_username);
        tvFollowerCount = view.findViewById(R.id.tv_follower_info);
        tvFollowingCount = view.findViewById(R.id.tv_following_info);
        tvSeeAll = view.findViewById(R.id.tv_SeeAll);
        tvNoTracks = view.findViewById(R.id.tv_no_tracks);
        tvNoPlaylists = view.findViewById(R.id.tv_no_playlists);
        tvNoAlbums = view.findViewById(R.id.tv_no_albums);
        btnFollow = view.findViewById(R.id.btn_following);
        lvTracks = view.findViewById(R.id.lv_Track);
        rvPlaylists = view.findViewById(R.id.recyclerViewPlaylists);
        rvAlbums = view.findViewById(R.id.recyclerViewAlbums);

    }

    private void initAdapters() {
        trackAdapter = new TrackAdapter(this, new ArrayList<>());
        playlistAdapter = new PlayListRVAdapter(new ArrayList<>(), playlist -> {
            PlaylistPageFragment fragment = PlaylistPageFragment.newInstance(new ArrayList<>(Collections.singletonList(playlist)));
            navigateToFragment(fragment);
        });
        albumAdapter = new AlbumRVAdapter(new ArrayList<>(), album -> {
            AlbumPageFragment fragment = AlbumPageFragment.newInstance(album);
            navigateToFragment(fragment);
        });

        lvTracks.setAdapter(trackAdapter);

        rvPlaylists.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        rvPlaylists.setAdapter(playlistAdapter);
        rvAlbums.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        rvAlbums.setAdapter(albumAdapter);

        Log.d("UserProfileFragment", "Adapters initialized");
    }
    private void navigateToFragment(Fragment fragment) {
        if (getActivity() != null) {
            View mainView = getActivity().findViewById(R.id.main);
            View viewPager = mainView != null ? mainView.findViewById(R.id.view_pager) : null;
            View fragmentContainer = mainView != null ? mainView.findViewById(R.id.fragment_container) : null;

            if (viewPager != null && fragmentContainer != null) {
                viewPager.setVisibility(View.GONE);
                fragmentContainer.setVisibility(View.VISIBLE);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            } else {
                Log.e("UserProfileFragment", "viewPager or fragmentContainer is null");
            }
        } else {
            Log.e("UserProfileFragment", "Activity is null");
        }
    }
    private void setupBackButton() {
        if (ivBack == null) {
            Log.e("UserProfileFragment", "setupBackButton: ivBack is null");
            return;
        }
        ivBack.setOnClickListener(v -> {
            Log.d("UserProfileFragment", "Back button clicked, source=" + source);
            if (getActivity() != null) {
                View mainView = requireActivity().findViewById(R.id.main);
                View viewPager = mainView != null ? mainView.findViewById(R.id.view_pager) : null;
                View fragmentContainer = mainView != null ? mainView.findViewById(R.id.fragment_container) : null;

                if ("search".equals(source)) {
                    if (viewPager != null && fragmentContainer != null) {
                        viewPager.setVisibility(View.VISIBLE);
                        fragmentContainer.setVisibility(View.GONE);
                        Log.d("UserProfileFragment", "Navigated back to search, showing viewPager");
                    } else {
                        Log.w("UserProfileFragment", "viewPager or fragmentContainer is null for search source");
                    }
                }
                if ("follower".equals(source) || "following".equals(source)) {
                    Fragment targetFragment = "follower".equals(source) ? FollowerFragment.newInstance() : FollowingFragment.newInstance();
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, targetFragment)
                            .commit();
                    Log.d("UserProfileFragment", "Navigated back to " + ("follower".equals(source) ? "FollowerFragment" : "FollowingFragment"));
                }
                if ("library".equals(source)) {
                    LibraryPageFragment library = new LibraryPageFragment();
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, library)
                            .commit();
                    Log.w("UserProfileFragment", "Back to: " + source);
                }
                if ("playlist".equals(source)) {
                    PlaylistsFragment playlistsFragment = new PlaylistsFragment();
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, playlistsFragment)
                            .commit();
                    Log.w("UserProfileFragment", "Back to: " + source);
                }
                getActivity().getSupportFragmentManager().popBackStack();
            } else {
                Log.e("UserProfileFragment", "getActivity is null");
            }
        });
    }
    private void setUpEdit(){
        if (ivEdit == null) {
            Log.e("UserProfileFragment", "setupBackButton: ivBack is null");
            return;
        }
        String currentUserId = SharedPreferencesManager.getInstance(getContext()).getUserId();
        if (currentUserId != null && currentUserId.equals(userId)) {
            ivEdit.setVisibility(View.VISIBLE);
            ivEdit.setOnClickListener(v -> {
                if (currentProfile != null) {
                    String displayName = currentProfile.getDisplayName() != null ? currentProfile.getDisplayName() : "Người dùng không xác định";
                    String avatarUrl = currentProfile.getAvatar() != null ? UrlHelper.getAvatarImageUrl(currentProfile.getAvatar()) : null;
                    String coverUrl = currentProfile.getCover() != null ? UrlHelper.getCoverImageUrl(currentProfile.getCover()) : null;
                    EditProfileFragment edit = EditProfileFragment.newInstance(coverUrl, avatarUrl, displayName);
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, edit)
                            .addToBackStack(null)
                            .commit();
                } else {
                    Toast.makeText(requireContext(), "Không thể tải hồ sơ để chỉnh sửa", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            ivEdit.setVisibility(View.GONE);
        }
    }
    private void setUpSeeAll(){
        if (tvSeeAll == null) {
            Log.e("UserProfileFragment", "setupBackButton: tvSeeAll is null");
            return;
        }
        String currentUserId = SharedPreferencesManager.getInstance(getContext()).getUserId();
        if (currentUserId != null && currentUserId.equals(userId)) {
            tvSeeAll.setOnClickListener(v -> {
                UploadsFragment uploadsFragment = new UploadsFragment().newInstance();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, uploadsFragment)
                        .addToBackStack(null)
                        .commit();
            });
        } else {
            tvSeeAll.setOnClickListener(v -> {
                AllTrackProfileFragment allTrackProfileFragment = new AllTrackProfileFragment();
                Bundle bundle = new Bundle();
                bundle.putString("userId", userId); // Truyền userId
                allTrackProfileFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, allTrackProfileFragment)
                        .addToBackStack(null)
                        .commit();
            });
        }
    }

    private void loadUserProfile() {
        String currentUserId = SharedPreferencesManager.getInstance(getContext()).getUserId();
        boolean isOwnProfile = userId.equals(currentUserId);
        btnFollow.setVisibility(isOwnProfile ? View.GONE : View.VISIBLE);

        ApiClient.getUserProfileApiService().getUserProfile(userId).enqueue(new Callback<ApiResponse<ProfileWithCountFollowResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<ProfileWithCountFollowResponse>> call, @NonNull Response<ApiResponse<ProfileWithCountFollowResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    currentProfile = response.body().getData();
                    updateUserProfile(currentProfile);
                    setUpEdit();
                    setUpSeeAll();
                } else {
                    Toast.makeText(requireContext(), "Không thể tải hồ sơ", Toast.LENGTH_SHORT).show();
                    ivEdit.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<ProfileWithCountFollowResponse>> call, @NonNull Throwable t) {
                Toast.makeText(requireContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                ivEdit.setVisibility(View.GONE);
            }
        });
    }

    private void updateUserProfile(ProfileWithCountFollowResponse profile) {
        tvDisplayName.setText(profile.getDisplayName() != null ? profile.getDisplayName() : "Người dùng không xác định");

        if (profile.getAvatar() != null) {
            Glide.with(requireContext())
                    .load(UrlHelper.getAvatarImageUrl(profile.getAvatar()))
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(ivAvatar);
        }

        if (profile.getCover() != null) {
            Glide.with(requireContext())
                    .load(UrlHelper.getCoverImageUrl(profile.getCover()))
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(ivCover);
        }

        btnFollow.setText(profile.isFollowing() ? "Following" : "Follow");
        btnFollow.setOnClickListener(v -> convertFollow(profile));
    }
    private void loadFollowCounts() {
        // Tải số lượng follower
        ApiClient.getUserService().getFollowers(userId, 0, 1000).enqueue(new Callback<ApiResponse<PageFollowResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<PageFollowResponse>> call, @NonNull Response<ApiResponse<PageFollowResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    PageFollowResponse pageFollowResponse = response.body().getData();
                    tvFollowerCount.setText(String.valueOf(pageFollowResponse.getContent() != null ? pageFollowResponse.getContent().size() + " Followers " : 0+ " Followers"));
                } else {
                    tvFollowerCount.setText("0");
                    Toast.makeText(requireContext(), "Không thể tải số lượng người theo dõi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<PageFollowResponse>> call, @NonNull Throwable t) {
                tvFollowerCount.setText("0 Follower");
                Toast.makeText(requireContext(), "Lỗi khi tải số lượng người theo dõi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Tải số lượng following
        ApiClient.getUserService().getFollowings(userId, 0, 1000).enqueue(new Callback<ApiResponse<PageFollowResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<PageFollowResponse>> call, @NonNull Response<ApiResponse<PageFollowResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    PageFollowResponse pageFollowResponse = response.body().getData();
                    tvFollowingCount.setText(String.valueOf(pageFollowResponse.getContent() != null ? pageFollowResponse.getContent().size() + " Followings" : 0+ " Followings"));
                } else {
                    tvFollowingCount.setText("0 Following");
                    Toast.makeText(requireContext(), "Không thể tải số lượng người đang theo dõi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<PageFollowResponse>> call, @NonNull Throwable t) {
                tvFollowingCount.setText("0");
                Toast.makeText(requireContext(), "Lỗi khi tải số lượng người đang theo dõi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void convertFollow(ProfileWithCountFollowResponse profile) {
        String followStatus = btnFollow.getText().toString();
        if (followStatus.equals("Follow")) {
            AddFollowRequest request = new AddFollowRequest(userId);
            ApiClient.getUserService().follow(request).enqueue(new Callback<ApiResponse<Object>>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse<Object>> call, @NonNull Response<ApiResponse<Object>> response) {
                    if (response.isSuccessful()) {
                        btnFollow.setText("Following");
                        profile.setFollowing(true); // Cập nhật trạng thái
                        loadFollowCounts(); // Cập nhật số lượng follower
                    } else {
                        Log.e("UserProfileFragment", "Follow failed, response code=" + response.code());
                        Toast.makeText(requireContext(), "Failed to follow", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse<Object>> call, @NonNull Throwable t) {
                    Log.e("UserProfileFragment", "Follow error: " + t.getMessage());
                    Toast.makeText(requireContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            ApiClient.getUserService().unfollow(profile.getUserId()).enqueue(new Callback<ApiResponse<Object>>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse<Object>> call, @NonNull Response<ApiResponse<Object>> response) {
                    if (response.isSuccessful()) {
                        btnFollow.setText("Follow");
                        profile.setFollowing(false); // Cập nhật trạng thái
                        loadFollowCounts(); // Cập nhật số lượng follower
                    } else {
                        Log.e("UserProfileFragment", "Unfollow failed, response code=" + response.code());
                        Toast.makeText(requireContext(), "Failed to unfollow", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse<Object>> call, @NonNull Throwable t) {
                    Log.e("UserProfileFragment", "Unfollow error: " + t.getMessage());
                    Toast.makeText(requireContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void loadUserProfileData() {
        Log.e("UserProfileFragment", "userId: "+ userId );
        // Tải danh sách bài hát
        ApiClient.getTrackApiService().getTracksByUserId(userId).enqueue(new Callback<ApiResponse<List<TrackResponse>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<TrackResponse>>> call, @NonNull Response<ApiResponse<List<TrackResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<TrackResponse> tracks = response.body().getData();
                    trackAdapter = new TrackAdapter(UserProfileFragment.this, tracks);
                    lvTracks.setAdapter(trackAdapter);
                    if (tracks.isEmpty()){
                        lvTracks.setVisibility(View.GONE);
                        tvNoTracks.setVisibility(View.VISIBLE);
                    }else {
                        lvTracks.setVisibility(View.VISIBLE);
                        tvNoTracks.setVisibility(View.GONE);
                    }
                } else {
                    lvTracks.setVisibility(View.GONE);
                    tvNoTracks.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<TrackResponse>>> call, @NonNull Throwable t) {
                Toast.makeText(requireContext(), "Lỗi khi tải danh sách bài hát: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                lvTracks.setVisibility(View.GONE);
                tvNoTracks.setVisibility(View.VISIBLE);
            }
        });

        // Tải danh sách playlist
        ApiClient.getPlaylistService().getPlaylistsByUserId(userId).enqueue(new Callback<ApiResponse<List<PlaylistResponse>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<PlaylistResponse>>> call, @NonNull Response<ApiResponse<List<PlaylistResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getData() != null) {
                        List<PlaylistResponse> playlists = response.body().getData();
                        setSizePlaylist(playlists);
                        playlistAdapter.updatePlaylists(playlists);
                        Log.d("UserProfileFragment", "Playlists loaded: " + playlists.size());
                        if (playlists.isEmpty()) {
                            rvPlaylists.setVisibility(View.GONE);
                            tvNoPlaylists.setVisibility(View.VISIBLE);
                        } else {
                            rvPlaylists.setVisibility(View.VISIBLE);
                            tvNoPlaylists.setVisibility(View.GONE);
                        }
                    } else {
                        Log.e("UserProfileFragment", "Playlist API error: " + response.body().getMessage() + ", code: " + response.body().getCode());
                        Toast.makeText(requireContext(), "Lỗi tải playlist: " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        rvPlaylists.setVisibility(View.GONE);
                        tvNoPlaylists.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.e("UserProfileFragment", "Failed to load playlists: " + response.message() + ", HTTP code: " + response.code());
                    Toast.makeText(requireContext(), "Lỗi tải playlist: " + response.message(), Toast.LENGTH_SHORT).show();
                    rvPlaylists.setVisibility(View.GONE);
                    tvNoPlaylists.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<PlaylistResponse>>> call, @NonNull Throwable t) {
                Log.e("UserProfileFragment", "Network error loading playlists: " + t.getMessage());
                Toast.makeText(requireContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                rvPlaylists.setVisibility(View.GONE);
                tvNoPlaylists.setVisibility(View.VISIBLE);
            }
        });

        // Tải danh sách album
        ApiClient.getAlbumService().getLikedAlbums(userId).enqueue(new Callback<ApiResponse<List<AlbumResponse>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<AlbumResponse>>> call, @NonNull Response<ApiResponse<List<AlbumResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getData() != null) {
                        List<AlbumResponse> albums = response.body().getData();
                        albumAdapter.updateAlbums(albums);
                        Log.d("UserProfileFragment", "Albums loaded: " + albums.size());
                        if (albums.isEmpty()) {
                            rvAlbums.setVisibility(View.GONE);
                            tvNoAlbums.setVisibility(View.VISIBLE);
                        } else {
                            rvAlbums.setVisibility(View.VISIBLE);
                            tvNoAlbums.setVisibility(View.GONE);
                        }
                    } else {
                        Log.e("UserProfileFragment", "Album API error: " + response.body().getMessage() + ", code: " + response.body().getCode());
                        Toast.makeText(requireContext(), "Lỗi tải album: " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        rvAlbums.setVisibility(View.GONE);
                        tvNoAlbums.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.e("UserProfileFragment", "Failed to load albums: " + response.message() + ", HTTP code: " + response.code());
                    Toast.makeText(requireContext(), "Lỗi tải album: " + response.message(), Toast.LENGTH_SHORT).show();
                    rvAlbums.setVisibility(View.GONE);
                    tvNoAlbums.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<AlbumResponse>>> call, @NonNull Throwable t) {
                Log.e("UserProfileFragment", "Network error loading albums: " + t.getMessage());
                Toast.makeText(requireContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                rvAlbums.setVisibility(View.GONE);
                tvNoAlbums.setVisibility(View.VISIBLE);
            }
        });
    }
    private void setSizePlaylist(List<PlaylistResponse> playlists) {
        if (rvPlaylists == null) return;

        ViewGroup.LayoutParams params = rvPlaylists.getLayoutParams();
        if (playlists != null && playlists.size() == 2) {
            // Set height = 210dp nếu có đúng 2 item
            params.height = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 210, getResources().getDisplayMetrics());
        } else {
            // Set lại chiều cao mặc định như trong XML
            params.height = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 420, getResources().getDisplayMetrics());
        }
        rvPlaylists.setLayoutParams(params);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserProfile(); // Tải lại hồ sơ
        loadUserProfileData();
    }
}