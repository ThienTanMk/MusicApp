package com.app.musicapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.app.musicapp.R;
import com.app.musicapp.model.Album;
import com.app.musicapp.model.LikedPlaylist;
import com.app.musicapp.model.LikedTrack;
import com.app.musicapp.model.Playlist;
import com.app.musicapp.model.Track;
import com.app.musicapp.view.fragment.SongOptionsBottomSheet;

import java.util.List;

public class ProfileContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_SECTION_HEADER = 0;
    private static final int TYPE_TRACK = 1;
    private static final int TYPE_ALBUM = 2;
    private static final int TYPE_PLAYLIST = 3;
    private static final int TYPE_LIKED_TRACK = 4;
    private static final int TYPE_LIKED_PLAYLIST = 5;

    private Fragment fragment;
    private List<Object> items;
    public ProfileContentAdapter(Fragment fragment, List<Object> items) {
        this.fragment = fragment;
        this.items = items;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_SECTION_HEADER) {
            View view = inflater.inflate(R.layout.item_section_header, parent, false);
            return new SectionHeaderViewHolder(view);
        } else if (viewType == TYPE_TRACK) {
            View view = inflater.inflate(R.layout.list_item_track, parent, false);
            return new TrackViewHolder(view,fragment);
        } else if (viewType == TYPE_ALBUM) {
            View view = inflater.inflate(R.layout.list_album_userprofile, parent, false);
            return new AlbumViewHolder(view);
        } else if (viewType == TYPE_PLAYLIST) {
            View view = inflater.inflate(R.layout.list_playlist_userprofile, parent, false);
            return new PlaylistViewHolder(view);
        } else if (viewType == TYPE_LIKED_TRACK) {
            View view = inflater.inflate(R.layout.list_item_track, parent, false);
            return new LikedTrackViewHolder(view);
        } else if (viewType == TYPE_LIKED_PLAYLIST) {
            View view = inflater.inflate(R.layout.list_playlist_userprofile, parent, false);
            return new LikedPlaylistViewHolder(view);
        }
        throw new IllegalArgumentException("Invalid view type");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = items.get(position);
        if (holder instanceof SectionHeaderViewHolder) {
            ((SectionHeaderViewHolder) holder).bind((String) item);
        } else if (holder instanceof TrackViewHolder) {
            ((TrackViewHolder) holder).bind((Track) item);
        } else if (holder instanceof AlbumViewHolder) {
            ((AlbumViewHolder) holder).bind((Album) item);
        } else if (holder instanceof PlaylistViewHolder) {
            ((PlaylistViewHolder) holder).bind((Playlist) item);
        } else if (holder instanceof LikedTrackViewHolder) {
            ((LikedTrackViewHolder) holder).bind((LikedTrack) item);
        } else if (holder instanceof LikedPlaylistViewHolder) {
            ((LikedPlaylistViewHolder) holder).bind((LikedPlaylist) item);
        }
    }
    @Override
    public int getItemViewType(int position) {
        Object item = items.get(position);
        if (item instanceof String) {
            return TYPE_SECTION_HEADER;
        } else if (item instanceof Track) {
            return TYPE_TRACK;
        } else if (item instanceof Album) {
            return TYPE_ALBUM;
        } else if (item instanceof Playlist) {
            return TYPE_PLAYLIST;
        } else if (item instanceof LikedTrack) {
            return TYPE_LIKED_TRACK;
        } else if (item instanceof LikedPlaylist) {
            return TYPE_LIKED_PLAYLIST;
        }
        throw new IllegalArgumentException("Unknown item type at position " + position);
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    static class SectionHeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tvSectionTitle;

        SectionHeaderViewHolder(View itemView) {
            super(itemView);
            tvSectionTitle = itemView.findViewById(R.id.tv_section_title);
        }

        void bind(String sectionTitle) {
            tvSectionTitle.setText(sectionTitle);
        }
    }

    static class TrackViewHolder extends RecyclerView.ViewHolder {
        ImageView ivTrackImage;
        TextView tvTrackTitle;
        TextView tvTrackArtist;
        TextView tvPlayCount;
        TextView tvDuration;
        ImageView ivMenu;
        Fragment fragment;

        TrackViewHolder(View itemView, Fragment fragment) {
            super(itemView);
            ivTrackImage = itemView.findViewById(R.id.iv_track_image);
            tvTrackTitle = itemView.findViewById(R.id.tv_track_title);
            tvTrackArtist = itemView.findViewById(R.id.tv_track_artist);
            tvPlayCount = itemView.findViewById(R.id.tv_play_count);
            tvDuration = itemView.findViewById(R.id.tv_duration);
            ivMenu = itemView.findViewById(R.id.iv_menu);
            this.fragment = fragment;
        }

        void bind(Track track) {
            tvTrackTitle.setText(track.getTitle());
            tvTrackArtist.setText(track.getDescription());
            tvPlayCount.setText(String.valueOf(track.getCountPlay()));
            tvDuration.setText(track.getDuration());
            ivTrackImage.setImageResource(R.drawable.logo);
            ivMenu.setOnClickListener(v -> {
                Toast.makeText(itemView.getContext(), "Menu clicked for: " + track.getTitle(), Toast.LENGTH_SHORT).show();
                SongOptionsBottomSheet bottomSheet = SongOptionsBottomSheet.newInstance(track);
                bottomSheet.show(fragment.getParentFragmentManager(), bottomSheet.getTag());
            });
        }
    }

    static class AlbumViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAlbumCover;
        TextView tvAlbumTitle;
        TextView tvAlbumArtist;

        AlbumViewHolder(View itemView) {
            super(itemView);
            ivAlbumCover = itemView.findViewById(R.id.iv_album_cover);
            tvAlbumTitle = itemView.findViewById(R.id.tv_album_title);
            tvAlbumArtist = itemView.findViewById(R.id.tv_album_artist);
        }

        void bind(Album album) {
            tvAlbumTitle.setText(album.getAlbumTitle());
            tvAlbumArtist.setText("Artist");
            ivAlbumCover.setImageResource(R.drawable.logo);
        }
    }

    static class PlaylistViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPlaylistCover;
        TextView tvPlaylistTitle;
        TextView tvPlaylistArtist;

        PlaylistViewHolder(View itemView) {
            super(itemView);
            ivPlaylistCover = itemView.findViewById(R.id.iv_playlist_cover);
            tvPlaylistTitle = itemView.findViewById(R.id.tv_playlist_title);
            tvPlaylistArtist = itemView.findViewById(R.id.tv_playlist_artist);
        }

        void bind(Playlist playlist) {
            tvPlaylistTitle.setText(playlist.getTitle());
            tvPlaylistArtist.setText(playlist.getUserId());
            ivPlaylistCover.setImageResource(R.drawable.logo);
        }
    }

    static class LikedTrackViewHolder extends RecyclerView.ViewHolder {
        ImageView ivTrackImage;
        TextView tvTrackTitle;
        TextView tvTrackArtist;
        TextView tvPlayCount;
        TextView tvDuration;
        ImageView ivMenu;

        LikedTrackViewHolder(View itemView) {
            super(itemView);
            ivTrackImage = itemView.findViewById(R.id.iv_track_image);
            tvTrackTitle = itemView.findViewById(R.id.tv_track_title);
            tvTrackArtist = itemView.findViewById(R.id.tv_track_artist);
            tvPlayCount = itemView.findViewById(R.id.tv_play_count);
            tvDuration = itemView.findViewById(R.id.tv_duration);
            ivMenu = itemView.findViewById(R.id.iv_menu);
        }

        void bind(LikedTrack likedTrack) {
            tvTrackTitle.setText("Liked Track " + likedTrack.getTrackId());
            tvTrackArtist.setText("Artist");
            tvPlayCount.setText("0");
            tvDuration.setText("0:00");
            ivTrackImage.setImageResource(R.drawable.logo);
            ivMenu.setOnClickListener(v -> {
                Toast.makeText(itemView.getContext(), "Menu clicked for liked track", Toast.LENGTH_SHORT).show();
                // Cần logic để lấy Track từ trackId và mở bottom sheet
            });
        }
    }

    static class LikedPlaylistViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPlaylistCover;
        TextView tvPlaylistTitle;
        TextView tvPlaylistArtist;

        LikedPlaylistViewHolder(View itemView) {
            super(itemView);
            ivPlaylistCover = itemView.findViewById(R.id.iv_playlist_cover);
            tvPlaylistTitle = itemView.findViewById(R.id.tv_playlist_title);
            tvPlaylistArtist = itemView.findViewById(R.id.tv_playlist_artist);

            if (tvPlaylistTitle == null || tvPlaylistArtist == null || ivPlaylistCover == null) {
                throw new IllegalStateException("One or more views are null in LikedPlaylistViewHolder");
            }
        }

        void bind(LikedPlaylist likedPlaylist) {
            if (likedPlaylist != null) {
                Playlist playlist = likedPlaylist.getPlaylist();
                if (playlist != null) {
                    tvPlaylistTitle.setText(playlist.getTitle() != null ? playlist.getTitle() : "Untitled");
                    tvPlaylistArtist.setText(playlist.getUserId() != null ? playlist.getUserId() : "Unknown User");
                } else {
                    tvPlaylistTitle.setText("No Playlist");
                    tvPlaylistArtist.setText("Unknown");
                }
            } else {
                tvPlaylistTitle.setText("No Data");
                tvPlaylistArtist.setText("Unknown");
            }
            ivPlaylistCover.setImageResource(R.drawable.logo);
        }
    }
}
