package com.app.musicapp.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.musicapp.R;
import com.app.musicapp.adapter.CommentAdapter;
import com.app.musicapp.api.ApiClient;
import com.app.musicapp.helper.UrlHelper;
import com.app.musicapp.model.request.CommentRequest;
import com.app.musicapp.model.request.RepliedCommentRequest;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.CommentResponse;
import com.app.musicapp.model.response.TrackResponse;
import com.bumptech.glide.Glide;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends AppCompatActivity {

    RecyclerView commentRecyclerView;;
    TextView commentCount, trackTitle, trackArtist;
    EditText editTextComment;
    ImageView imageCover, imageCloseBtn, imageSortedBtn,imageSendBtn;
    ArrayList<CommentResponse> commentResponses;
    CommentAdapter commentAdapter;
    private String trackId;

    private Boolean isReplied=false;

    private String commentId;

    public void setCommentId(String commentId){
        this.commentId = commentId;
    }
    public void setRepliedType(){
        this.isReplied = true;
    }
    public void setNotRepliedType(){
        this.isReplied = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_comment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        commentRecyclerView = findViewById(R.id.recycle_comments);
        commentResponses = new ArrayList<>();
        commentAdapter = new CommentAdapter(commentResponses,this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        commentRecyclerView.setLayoutManager(layoutManager);
        commentRecyclerView.setAdapter(commentAdapter);
        initView();
        getComments();
    }
    private void getComments(){


        ApiClient.getCommentService().getCommentsByTrackId(trackId).enqueue(new Callback<ApiResponse<List<CommentResponse>>>(){
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<ApiResponse<List<CommentResponse>>> call, Response<ApiResponse<List<CommentResponse>>> response){
                commentResponses.clear();
                if(response.body()!=null)
                    commentResponses.addAll(response.body().getData());
                commentAdapter.notifyDataSetChanged();
                commentCount.setText(String.valueOf(commentResponses.size())+" Comments");
            }

            @Override
            public void onFailure(Call<ApiResponse<List<CommentResponse>>> call, Throwable t) {
                t.printStackTrace();
            }

        });
    }
    public void initView(){

        Intent intent = getIntent();
        trackId = intent.getStringExtra("track_id");
        String _trackTitle = intent.getStringExtra("track_title");
        String _trackArtist = intent.getStringExtra("track_artist");
        String _trackCover = intent.getStringExtra("track_cover");




        trackArtist = findViewById(R.id.text_artist);
        trackTitle = findViewById(R.id.text_track_title);
        commentCount = findViewById(R.id.text_comment_count);
        imageCover = findViewById(R.id.image_cover);
        imageCloseBtn = findViewById(R.id.image_close_btn);
        imageSortedBtn = findViewById(R.id.image_sorted_btn);
        editTextComment = findViewById(R.id.edit_text_comment);
        imageSendBtn = findViewById(R.id.image_send_btn);


        this.trackTitle.setText(_trackTitle);
        this.trackArtist.setText(_trackArtist);
        Glide.with(this).load(UrlHelper.getCoverImageUrl(_trackCover)).into(imageCover);


        editTextComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                imageSendBtn.setVisibility(s.toString().trim().isEmpty() ? View.INVISIBLE : View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        imageSendBtn.setOnClickListener(v -> {

            String content = editTextComment.getText().toString();
            editTextComment.setText("");
            if(isReplied){
                RepliedCommentRequest comment = new RepliedCommentRequest(content);
                ApiClient.getCommentService().replyComment(commentId,comment).enqueue(new Callback<ApiResponse<CommentResponse>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<CommentResponse>> call, Response<ApiResponse<CommentResponse>> response) {
                        getComments();
                    }
                    @Override
                    public void onFailure(Call<ApiResponse<CommentResponse>> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
            else{
                CommentRequest comment = new CommentRequest(trackId,content);
                Log.i("a",content);
                ApiClient.getCommentService().addComment(comment).enqueue(new Callback<ApiResponse<CommentResponse>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<CommentResponse>> call, Response<ApiResponse<CommentResponse>> response) {
                        getComments();
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<CommentResponse>> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
            setNotRepliedType();
        });
        imageCloseBtn.setOnClickListener(v -> finish());

    }


}