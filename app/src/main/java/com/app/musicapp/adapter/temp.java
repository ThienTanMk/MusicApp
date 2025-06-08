//package com.app.musicapp.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.TextView;
//
//
//import com.app.musicapp.R;
//import com.app.musicapp.helper.UrlHelper;
//import com.app.musicapp.model.response.CommentResponse;
//import com.app.musicapp.view.activity.CommentActivity;
//import com.bumptech.glide.Glide;
//
//import java.util.List;
//
//
//public class CommentAdapter extends BaseAdapter {
//    private List<CommentResponse> commentResponses;
//    private CommentActivity context;
//
//    public CommentAdapter(List<CommentResponse> commentResponses, CommentActivity context) {
//        this.commentResponses = commentResponses;
//        this.context = context;
//    }
//
//    @Override
//    public int getCount() {
//        return commentResponses.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return null;
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.comment_item, null);
//        ImageView imageAvatar = view.findViewById(R.id.image_avatar);
//        TextView displayName = view.findViewById(R.id.text_display_name);
//        TextView commentContent = view.findViewById(R.id.text_content);
//        TextView reply = view.findViewById(R.id.text_reply_btn);
//        ImageView like = view.findViewById(R.id.image_like_btn);
//        CommentResponse commentResponse = commentResponses.get(position);
//        displayName.setText(commentResponse.getUser().getDisplayName());
//        commentContent.setText(commentResponse.getContent());
//        Glide.with(context).load(UrlHelper.getAvatarImageUrl(commentResponse.getUser().getAvatar())).into(imageAvatar);
//
//        reply.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ImageView imageSendBtn = context.findViewById(R.id.image_send_btn);
//                EditText editComment = context.findViewById(R.id.edit_text_comment);
//                editComment.setText("@" + commentResponse.getUser().getDisplayName());
//                imageSendBtn.setVisibility(View.VISIBLE);
//                context.setCommentId(commentResponse.getId());
//            }
//        });
//
//        ListView repliedComments = view.findViewById(R.id.list_replies_comments);
//        for(CommentResponse comment: commentResponse.getReplies()){
//            View replyView = repliedComments
//        }
//
//        return view;
//    }
//}
