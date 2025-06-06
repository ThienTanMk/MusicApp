package com.app.musicapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.app.musicapp.R;
import com.app.musicapp.model.Track;
import com.app.musicapp.service.MusicService;

import java.util.List;

public class NextUpAdapter extends BaseAdapter {
    private List<Track> nextUpItems;
    private Context context;
    private MusicService musicService;
    private String username;
    private View playingItem;
    private int currentPosition;
    public NextUpAdapter(List<Track> nextUpItems, Context context, MusicService musicService) {
        this.nextUpItems = nextUpItems;
        this.context = context;
        this.musicService = musicService;
    }

    @Override
    public int getCount() {
        return nextUpItems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.next_up_item,null);
        ImageView cover = view.findViewById(R.id.image_nextup_cover);
        TextView title = view.findViewById(R.id.text_nextup_title);
        TextView username = view.findViewById(R.id.text_nextup_username);
        Track item = nextUpItems.get(position);
        cover.setImageResource(R.drawable.img);
        title.setText(item.getTitle());
        username.setText(item.getArtist());

        if(musicService.getCurrentIndex()==position){
//            username.setText(item.getArtist());
            this.username = item.getArtist();
            if(musicService.isPlaying())
                username.setText("Now Playing");
            else username.setText("Paused");
            username.setTextColor(ContextCompat.getColor(context, R.color.soundcloud));
            this.playingItem = view;
            currentPosition = position;
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.setText("Now Playing");
                if(currentPosition==position){
                    musicService.playCurrentMusic();
                    return;
                }
                TextView temp_username = playingItem.findViewById(R.id.text_nextup_username);
                temp_username.setTextColor(ContextCompat.getColor(context, com.google.android.material.R.color.design_default_color_background));
                temp_username.setText(NextUpAdapter.this.username);

                username.setTextColor(ContextCompat.getColor(context, R.color.soundcloud));
                musicService.playMusicAtIndex(position);

                currentPosition = position;
                playingItem = view;
                NextUpAdapter.this.username = item.getArtist();
            }
        });
        return view;
    }
}
