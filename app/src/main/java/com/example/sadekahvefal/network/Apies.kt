package com.example.sadekahvefal.network

import androidx.lifecycle.LiveData
import com.example.sadekahvefal.model.response.*
import retrofit2.Response
import retrofit2.http.*

interface Apies {
    @FormUrlEncoded()
    @POST("register.php")
    suspend fun userRegister(
        @Field("user_name") userName: String,
        @Field("first_name") firstName: String,
        @Field("last_name") lastName: String,
        @Field("email") email: String,
        @Field("phone") phone: String,
        @Field("password") password: String
    ): Response<AuthResponse>

    @FormUrlEncoded
    @POST("login.php")
    suspend fun userLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ) : Response<AuthResponse>

    @GET("get_job.php")
    suspend fun getJob() : JobResponse

    @FormUrlEncoded
    @POST("save_post.php")
    suspend fun savePost(
        @Field("image_1") image1: String,
        @Field("name1") name1: String,
        @Field("image_2") image2: String,
        @Field("name2") name2: String,
        @Field("image_3") image3: String,
        @Field("name3") name3: String,
        @Field("user_id") userId: Int,
        @Field("gender_id") genderId: Int,
        @Field("job_id") jobId: Int,
        @Field("relation_id") relationId: Int,
        @Field("age") age: Int,
        @Field("ekstra_infromation") ekstraInfromation: String
    ) : Response<StaticResponse>

    @GET("get_post.php")
    suspend fun getPost(
        @Query("page") page : Int,
        @Query("row_per_page") rowPerPage : Int,
    ) : PostResponse

    @GET("get_top_users.php")
    suspend fun getTopUsers() : TopUserResponse

    @FormUrlEncoded
    @POST("save_comment.php")
    suspend fun saveComment(
        @Field("post_id") postId: Int,
        @Field("commentor_id") commentorId: Int,
        @Field("post_owner_id") postOwnerId: Int,
        @Field("comment") comment: String
    ) : Response<StaticResponse>

    @FormUrlEncoded
    @POST("get_comment.php")
    suspend fun getComment(
        @Field("post_owner_id") postOwnerId: Int
    ) : CommentResponse

    @FormUrlEncoded
    @POST("get_comment.php")
    suspend fun getCommentLive(
        @Field("post_owner_id") postOwnerId: Int
    ) : LiveData<CommentResponse>

    @FormUrlEncoded
    @POST("update_is_showing.php")
    suspend fun updateIsShowing(
        @Field("comment_id") commentId: Int
    ) : StaticResponse

    @FormUrlEncoded
    @POST("update_rating_score.php")
    suspend fun updateRatingAndScore(
        @Field("comment_id") commentId: Int,
        @Field("commentator_id") commentatorId: Int,
        @Field("rating") rating: Int
    ) : StaticResponse

    @FormUrlEncoded
    @POST("get_user.php")
    suspend fun getUserProfile(
        @Field("user_id") userId: Int
    ) : UserProfileResponse

}