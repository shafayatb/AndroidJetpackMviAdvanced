package com.baldystudios.androidjetpackmviadvanced.api.main

import androidx.lifecycle.LiveData
import com.baldystudios.androidjetpackmviadvanced.api.GenericResponse
import com.baldystudios.androidjetpackmviadvanced.api.main.responses.BlogListSearchResponse
import com.baldystudios.androidjetpackmviadvanced.models.AccountProperties
import com.baldystudios.androidjetpackmviadvanced.util.GenericApiResponse
import retrofit2.http.*

interface OpenApiMainService {

    @GET("account/properties")
    fun getAccountProperties(
        @Header("Authorization") authorization: String
    ): LiveData<GenericApiResponse<AccountProperties>>

    @PUT("account/properties/update")
    @FormUrlEncoded
    fun putAccountProperties(
        @Header("Authorization") authorization: String,
        @Field("email") email: String,
        @Field("username") username: String
    ): LiveData<GenericApiResponse<GenericResponse>>

    @PUT("account/change_password/")
    @FormUrlEncoded
    fun updatePassword(
        @Header("Authorization") authorization: String,
        @Field("old_password") currentPassword: String,
        @Field("new_password") newPassword: String,
        @Field("confirm_new_password") confirmNewPassword: String
    ): LiveData<GenericApiResponse<GenericResponse>>

    @GET("blog/list")
    fun searchListBlogPost(
        @Header("Authorization") authorization: String,
        @Query("search") query: String,
        @Query("ordering") ordering: String,
        @Query("page") page: Int
    ): LiveData<GenericApiResponse<BlogListSearchResponse>>
}