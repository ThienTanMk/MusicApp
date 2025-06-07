package com.app.musicapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.app.musicapp.R;
import com.app.musicapp.model.response.CommentResponse;

import java.util.List;


public class CommentAdapter extends BaseAdapter {
    private List<CommentResponse> commentResponses;
    private Context context;

    public CommentAdapter(List<CommentResponse> commentResponses, Context context) {
        this.commentResponses = commentResponses;
        this.context = context;
    }

    @Override
    public int getCount() {
        return commentResponses.size();
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
        View view = inflater.inflate(R.layout.comment_item,null);
        ImageView imageView = view.findViewById(R.id.comment_avatar);
        TextView displayName = view.findViewById(R.id.display_name);
        TextView commentContent = view.findViewById(R.id.comment_content);
        TextView reply = view.findViewById(R.id.reply_btn);
        return view;
    }
}
