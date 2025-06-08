package com.app.musicapp.api;

import com.app.musicapp.model.request.CommentRequest;
import com.app.musicapp.model.request.RepliedCommentRequest;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.CommentResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CommentApiService {
    @GET("/api/comment-service/comments")
     Call<ApiResponse<List<CommentResponse>>> getCommentsByTrackId(@Query("track_id") String trackId);
    @POST("/api/comment-service/comments")
    Call<ApiResponse<CommentResponse>> addComment(@Body CommentRequest request);

    @POST("/api/comment-service/comments/{commentId}/replies")
    Call<ApiResponse<CommentResponse>> replyComment(@Path("commentId") String commentId, @Body RepliedCommentRequest request);

    @GET("/api/comment-service/comments/count")
    Call<ApiResponse<Integer>> getCommentCountByTrackId(@Query("track_id") String trackId);

    @POST("/api/comment-service/comments/{id}/likes")
    Call<ApiResponse<Void>> likeComment(@Path("id") String commentId);
    @DELETE("/api/comment-service/comments/{id}/likes")
    Call<ApiResponse<Void>> unlikeComment(@Path("id") String commentId);
}
