package com.app.musicapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.app.musicapp.R;
import com.app.musicapp.api.AdminApiService;
import com.app.musicapp.interfaces.BaseManageItem;
import com.app.musicapp.model.response.ApiResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Callback;

public class ManageListAdapter<T extends BaseManageItem> extends BaseAdapter {
    private Context context;
    private List<T> items;
    private AdminApiService apiService;
    private boolean isGenre; // true: Genre, false: Tag
    private Runnable reloadCallback;
    public ManageListAdapter(Context context, List<T> items, AdminApiService apiService, boolean isGenre, Runnable reloadCallback) {
        this.context = context;
        this.items = items;
        this.apiService = apiService;
        this.isGenre = isGenre;
        this.reloadCallback = reloadCallback;
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public T getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_manage, parent, false);
        }

        T item = getItem(position);
        TextView textItem = convertView.findViewById(R.id.text_item);
        textItem.setText(item.getName());

        ImageButton imbDelete = convertView.findViewById(R.id.image_delete);
        // Xử lý click icon delete
        imbDelete.setOnClickListener(v -> deleteItem(item.getId()));
        Log.e("ManageAdapter", "item=" + item + ", id=" + item.getId());
        Log.e("ManageAdapter", "isGenre=" + isGenre);
        return convertView;
    }
    private void deleteItem(String id) {
        Call<ApiResponse<String>> call = isGenre ? apiService.deleteGenre(id) : apiService.deleteTag(id);

        call.enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful()) {
                    reloadCallback.run();
                } else {
                    Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
