package com.app.musicapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.app.musicapp.R;
import com.app.musicapp.model.Album;
import com.app.musicapp.view.fragment.album.AlbumOptionsBottomSheet;

import java.util.List;

public class AlbumAdapter extends BaseAdapter {
    private Context context;
    private List<Album> albums;

    public AlbumAdapter(Context context, List<Album> albums) {
        this.context = context;
        this.albums = albums;
    }

    @Override
    public int getCount() {
        return albums.size();
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
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_album, viewGroup, false);
        }
        // Ánh xạ các view
        ImageView ivAlbumImage = view.findViewById(R.id.iv_album_image);
        TextView tvAlbumTitle = view.findViewById(R.id.tv_album_title);
        TextView tvArtist = view.findViewById(R.id.tv_artist);
        TextView tvLikeCount = view.findViewById(R.id.tv_like_count);
        TextView tvTrackCount = view.findViewById(R.id.tv_track_count);
        ImageView ivMenu = view.findViewById(R.id.iv_menu);

        Album album = albums.get(i);
        tvAlbumTitle.setText(album.getAlbumTitle());
        tvArtist.setText(album.getMainArtists());
        tvTrackCount.setText(album.getTracks().size() + " Tracks");

        tvLikeCount.setText("220");

        ivAlbumImage.setImageResource(R.drawable.logo);

        ivMenu.setOnClickListener(v -> {
            // Mở AlbumOptionsBottomSheet
            AlbumOptionsBottomSheet bottomSheet = AlbumOptionsBottomSheet.newInstance(album);
            bottomSheet.show(((FragmentActivity) context).getSupportFragmentManager(), bottomSheet.getTag());
        });
        return view;
    }
}
