package com.app.musicapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.app.musicapp.R;
import com.app.musicapp.api.ApiClient;
import com.app.musicapp.helper.UrlHelper;
import com.app.musicapp.model.response.AlbumResponse;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.view.fragment.album.AlbumOptionsBottomSheet;
import com.bumptech.glide.Glide;
import com.app.musicapp.interfaces.OnLikeChangeListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlbumAdapter extends BaseAdapter {
    private Context context;
    private List<AlbumResponse> albums;
    private OnLikeChangeListener likeChangeListener;

    public AlbumAdapter(Context context, List<AlbumResponse> albums) {
        this.context = context;
        this.albums = albums != null ? albums : new ArrayList<>();
    }

    public void setOnLikeChangeListener(OnLikeChangeListener listener) {
        this.likeChangeListener = listener;
    }

    @Override
    public int getCount() {
        return albums != null ? albums.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return albums.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_album, viewGroup, false);
            holder = new ViewHolder();
            holder.ivAlbumImage = view.findViewById(R.id.iv_album_image);
            holder.tvAlbumTitle = view.findViewById(R.id.tv_album_title);
            holder.tvArtist = view.findViewById(R.id.tv_artist);
            holder.tvLikeCount = view.findViewById(R.id.tv_like_count);
            holder.tvTrackCount = view.findViewById(R.id.tv_track_count);
            holder.ivMenu = view.findViewById(R.id.iv_menu);
            holder.ivLike = view.findViewById(R.id.iv_like_button);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        AlbumResponse album = albums.get(i);
        holder.tvAlbumTitle.setText(album.getAlbumTitle());
        holder.tvArtist.setText(album.getMainArtists());
        
        // Add null check for tracks
        int trackCount = album.getTracks() != null ? album.getTracks().size() : 0;
        holder.tvTrackCount.setText(trackCount + " Tracks");

        // Load album image
        if (album.getImagePath() != null) {
            Glide.with(context)
                    .load(UrlHelper.getCoverImageUrl(album.getImagePath()))
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(holder.ivAlbumImage);
        } else {
            holder.ivAlbumImage.setImageResource(R.drawable.logo);
        }

        // Update like UI based on current state
        updateLikeUI(holder, album.getIsLiked());
            
        // Set like button click listener
        holder.ivLike.setOnClickListener(v -> {
            if (album.getIsLiked()) {
                unlikeAlbum(album.getId(), holder);
            } else {
                likeAlbum(album.getId(), holder);
            }
        });

         holder.ivMenu.setOnClickListener(v -> {
             AlbumOptionsBottomSheet bottomSheet = AlbumOptionsBottomSheet.newInstance(album);
             bottomSheet.setOnLikeChangeListener(likeChangeListener);
             bottomSheet.show(((FragmentActivity) context).getSupportFragmentManager(), "AlbumOptionsBottomSheet");
         });

        return view;
    }

    private static class ViewHolder {
        ImageView ivAlbumImage;
        TextView tvAlbumTitle;
        TextView tvArtist;
        TextView tvLikeCount;
        TextView tvTrackCount;
        ImageView ivMenu;
        ImageView ivLike;
    }

    private void checkLikeStatus(String albumId, ViewHolder holder) {
        // Find album with matching id
        for (AlbumResponse album : albums) {
            if (album.getId().equals(albumId)) {
                updateLikeUI(holder, album.getIsLiked());
                break;
            }
        }
    }

    private void updateAlbumLikeState(String albumId, boolean isLiked) {
        for (AlbumResponse album : albums) {
            if (album.getId().equals(albumId)) {
                album.setIsLiked(isLiked);
                break;
            }
        }
        notifyDataSetChanged();
    }

    private void likeAlbum(String albumId, ViewHolder holder) {
        ApiClient.getAlbumService().likeAlbum(albumId).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateLikeUI(holder, true);
                    updateAlbumLikeState(albumId, true);
                    if (likeChangeListener != null) {
                        likeChangeListener.onLikeChanged(albumId, true);
                    }
                    Toast.makeText(context, "Đã thích album", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Không thể thích album", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                Toast.makeText(context, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void unlikeAlbum(String albumId, ViewHolder holder) {
        ApiClient.getAlbumService().unlikeAlbum(albumId).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateLikeUI(holder, false);
                    updateAlbumLikeState(albumId, false);
                    if (likeChangeListener != null) {
                        likeChangeListener.onLikeChanged(albumId, false);
                    }
                    Toast.makeText(context, "Đã bỏ thích album", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Không thể bỏ thích album", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                Toast.makeText(context, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateLikeUI(ViewHolder holder, boolean isLiked) {
        if (holder != null && holder.ivLike != null) {
            holder.ivLike.setImageResource(R.drawable.ic_favorite);
            holder.ivLike.setColorFilter(
                ContextCompat.getColor(context,
                    isLiked ? R.color.like_active : R.color.like_inactive)
            );
        }
    }
}
