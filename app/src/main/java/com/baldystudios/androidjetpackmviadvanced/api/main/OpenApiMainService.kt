package com.baldystudios.androidjetpackmviadvanced.api.main

import com.baldystudios.androidjetpackmviadvanced.api.GenericResponse
import com.baldystudios.androidjetpackmviadvanced.api.main.responses.BlogCreateUpdateResponse
import com.baldystudios.androidjetpackmviadvanced.api.main.responses.BlogListSearchResponse
import com.baldystudios.androidjetpackmviadvanced.di.main.MainScope
import com.baldystudios.androidjetpackmviadvanced.models.AccountProperties
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

@MainScope
interface OpenApiMainService {

    @GET("account/properties")
    fun getAccountProperties(
        @Header("Authorization") authorization: String
    ): AccountProperties

    @PUT("account/properties/update")
    @FormUrlEncoded
    fun saveAccountProperties(
        @Header("Authorization") authorization: String,
        @Field("email") email: String,
        @Field("username") username: String
    ): GenericResponse

    @PUT("account/change_password/")
    @FormUrlEncoded
    fun updatePassword(
        @Header("Authorization") authorization: String,
        @Field("old_password") currentPassword: String,
        @Field("new_password") newPassword: String,
        @Field("confirm_new_password") confirmNewPassword: String
    ): GenericResponse

    @GET("blog/list")
    fun searchListBlogPosts(
        @Header("Authorization") authorization: String,
        @Query("search") query: String,
        @Query("ordering") ordering: String,
        @Query("page") page: Int
    ): BlogListSearchResponse

    @GET("blog/{slug}/is_author")
    fun isAuthorOfBlogPost(
        @Header("Authorization") authorization: String,
        @Path("slug") slug: String
    ): GenericResponse

    @DELETE("blog/{slug}/delete")
    fun deleteBlogPost(
        @Header("Authorization") authorization: String,
        @Path("slug") slug: String
    ): GenericResponse

    @Multipart
    @PUT("blog/{slug}/update")
    fun updateBlog(
        @Header("Authorization") authorization: String,
        @Path("slug") slug: String,
        @Part("title") title: RequestBody,
        @Part("body") body: RequestBody,
        @Part image: MultipartBody.Part?
    ): BlogCreateUpdateResponse

    @Multipart
    @POST("blog/create")
    fun createBlog(
        @Header("Authorization") authorization: String,
        @Part("title") title: RequestBody,
        @Part("body") body: RequestBody,
        @Part image: MultipartBody.Part?
    ): BlogCreateUpdateResponse
}