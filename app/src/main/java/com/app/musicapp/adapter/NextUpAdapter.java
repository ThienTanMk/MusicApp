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
import com.app.musicapp.helper.UrlHelper;
import com.app.musicapp.model.response.TrackResponse;
import com.app.musicapp.service.MusicService;
import com.bumptech.glide.Glide;

import java.util.List;

public class NextUpAdapter extends BaseAdapter {
    private List<TrackResponse> nextUpItems;
    private Context context;
    private MusicService musicService;

    public NextUpAdapter(List<TrackResponse> nextUpItems, Context context, MusicService musicService) {
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
        TrackResponse item = nextUpItems.get(position);
        cover.setImageResource(R.drawable.img);
        title.setText(item.getTitle());
        username.setText(item.getArtist());
        Glide.with(context).load(UrlHelper.getCoverImageUrl(item.getCoverImageName())).placeholder(R.drawable.img).into(cover);

        if(musicService.getCurrentIndex()==position){
            if(musicService.isPlaying())
                username.setText("Now Playing");
            else username.setText("Paused");
            username.setTextColor(ContextCompat.getColor(context, R.color.soundcloud));
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(musicService.getCurrentIndex()!=position)
                    musicService.playMusicAtIndex(position);
                else if(musicService.isPlaying()) musicService.pauseCurrentMusic();
                else musicService.playCurrentMusic();
            }
        });
        return view;
    }
}
