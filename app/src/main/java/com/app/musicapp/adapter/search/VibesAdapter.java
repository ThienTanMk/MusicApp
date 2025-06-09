package com.app.musicapp.adapter.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.musicapp.R;
import com.app.musicapp.model.GridView.Vibes;

import java.util.List;

public class VibesAdapter extends BaseAdapter {
    private List<Vibes> vibeItems;
    private LayoutInflater inflater;

    public VibesAdapter(Context context, List<Vibes> vibeItems) {
        this.vibeItems = vibeItems;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return vibeItems.size();
    }

    @Override
    public Object getItem(int i) {
        return vibeItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.items_search, viewGroup, false);
            holder = new ViewHolder();
            holder.vibeImage = view.findViewById(R.id.vibeImage);
            holder.vibeTitle = view.findViewById(R.id.vibeTitle);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Vibes item = vibeItems.get(i);
        holder.vibeImage.setImageResource(item.getImageResId());
        holder.vibeTitle.setText(item.getTitle());

        return view;
    }

    static class ViewHolder {
        ImageView vibeImage;
        TextView vibeTitle;
    }
}
