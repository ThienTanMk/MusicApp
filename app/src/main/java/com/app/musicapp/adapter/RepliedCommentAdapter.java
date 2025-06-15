package com.app.musicapp.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.app.musicapp.R;
import com.app.musicapp.api.ApiClient;
import com.app.musicapp.helper.SharedPreferencesManager;
import com.app.musicapp.helper.UrlHelper;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.CommentResponse;
import com.app.musicapp.view.activity.CommentActivity;
import com.app.musicapp.view.fragment.profile.UserProfileFragment;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepliedCommentAdapter extends RecyclerView.Adapter<RepliedCommentAdapter.ViewHolder> {

    private CommentResponse parentComment;
    private CommentActivity context;
    public RepliedCommentAdapter(CommentResponse parentComment, CommentActivity context) {
        this.parentComment=parentComment;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.replied_comment_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CommentResponse comment = parentComment.getReplies().get(position);
        holder.displayName.setText(comment.getUser().getDisplayName());
        holder.commentContent.setText(comment.getContent());
        Glide.with(context).load(UrlHelper.getAvatarImageUrl(comment.getUser().getAvatar())).into(holder.imageAvatar);

        if(comment.getLiked()) holder.like.setImageResource(R.drawable.red_heart_icon);


        holder.reply.setOnClickListener(v -> {
            ImageView imageSendBtn = context.findViewById(R.id.image_send_btn);
            EditText editComment = context.findViewById(R.id.edit_text_comment);
            editComment.setText("@" + comment.getUser().getDisplayName());
            context.setRepliedType();
            context.setCommentId(comment.getId());
        });
        String userId = SharedPreferencesManager.getInstance(context).getUserId();
        if(!userId.equals(comment.getUserId())) holder.moreAction.setVisibility(View.GONE);
        else holder.moreAction.setVisibility(View.VISIBLE); // Reset lại mặc định
        holder.moreAction.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(v.getContext());
            View sheetView = LayoutInflater.from(v.getContext()).inflate(R.layout.comment_bottom_sheet, null);
            bottomSheetDialog.setContentView(sheetView);

            sheetView.findViewById(R.id.option_delete).setOnClickListener(view -> {
                ApiClient.getCommentService().deleteComment(comment.getId()).enqueue(new Callback<ApiResponse<Void>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                        parentComment.getReplies().remove(comment);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {

                    }
                });
                bottomSheetDialog.dismiss();
            });

            bottomSheetDialog.show();
        });
        holder.like.setOnClickListener(v -> {
            if(!comment.getLiked()){
                ApiClient.getCommentService().likeComment(comment.getId()).enqueue(new Callback< ApiResponse<Void>>(){

                    @Override
                    public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                        if(response.isSuccessful()){
                            holder.like.setImageResource(R.drawable.red_heart_icon);
                            comment.setLiked(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {

                    }
                });
            }
            else{
                ApiClient.getCommentService().unlikeComment(comment.getId()).enqueue(new Callback< ApiResponse<Void>>(){

                    @Override
                    public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                        if(response.isSuccessful()){
                            holder.like.setImageResource(R.drawable.heart_icon);
                            comment.setLiked(false);
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        if(parentComment.getReplies()==null) return 0;
        return parentComment.getReplies().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageAvatar;
        TextView displayName ;
        TextView commentContent;
        TextView reply;
        ImageView like;
        ImageView moreAction;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageAvatar = itemView.findViewById(R.id.replied_image_avatar);
            displayName = itemView.findViewById(R.id.replied_text_display_name);
            commentContent = itemView.findViewById(R.id.replied_text_content);
            reply = itemView.findViewById(R.id.replied_text_reply_btn);
            like = itemView.findViewById(R.id.replied_image_like_btn);
            moreAction = itemView.findViewById(R.id.replied_image_more_action);
        }
    }
}
