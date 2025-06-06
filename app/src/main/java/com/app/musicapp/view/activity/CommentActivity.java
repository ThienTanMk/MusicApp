package com.app.musicapp.view.activity;

import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.app.musicapp.R;
import com.app.musicapp.adapter.CommentAdapter;
import com.app.musicapp.model.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends AppCompatActivity {

    ListView commentListView;
    ArrayList<Comment> comments;
    CommentAdapter commentAdapter;
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

        commentListView = findViewById(R.id.comment_listview);
        comments = new ArrayList<>();

// Khởi tạo comment 1
        comments.add(new Comment(
                "1",
                "track01",
                "user01",
                null,
                false,
                "This is the first comment.",
                LocalDateTime.now(),
                2,
                new ArrayList<>()
        ));

// Khởi tạo comment 2
        comments.add(new Comment(
                "2",
                "track01",
                "user02",
                null,
                true,
                "Second comment here!",
                LocalDateTime.now().minusMinutes(5),
                5,
                new ArrayList<>()
        ));

// Khởi tạo comment 3 có replies
        List<Comment> replies = new ArrayList<>();
        replies.add(new Comment(
                "3-1",
                "track01",
                "user03",
                "user02",
                false,
                "Replying to comment 2",
                LocalDateTime.now().minusMinutes(2),
                1,
                new ArrayList<>()
        ));

        comments.add(new Comment(
                "3",
                "track01",
                "user02",
                null,
                false,
                "Third comment with a reply.",
                LocalDateTime.now().minusMinutes(10),
                3,
                replies
        ));
        commentAdapter = new CommentAdapter(comments,this);
        commentListView.setAdapter(commentAdapter);

    }

}