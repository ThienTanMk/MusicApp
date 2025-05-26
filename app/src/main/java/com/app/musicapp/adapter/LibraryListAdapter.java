package com.app.musicapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.musicapp.R;
import com.app.musicapp.model.ListView.LibraryList;

import java.util.List;

public class LibraryListAdapter extends BaseAdapter {
    private Context context;
    private List<LibraryList> libraryLists;
    public LibraryListAdapter(Context context, List<LibraryList> libraryLists){
        this.context = context;
        this.libraryLists = libraryLists;
    }
    @Override
    public int getCount() {
        return libraryLists.size();
    }

    @Override
    public Object getItem(int i) {
        return libraryLists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.library_list,viewGroup,false);
            viewHolder = new ViewHolder();
            viewHolder.tvLibraryItem = view.findViewById(R.id.tv_library_item);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        // Gán dữ liệu
        LibraryList libraryItem = libraryLists.get(i);
        viewHolder.tvLibraryItem.setText(libraryItem.getName());
        return view;
    }
    static class ViewHolder {
        TextView tvLibraryItem;
    }
}
