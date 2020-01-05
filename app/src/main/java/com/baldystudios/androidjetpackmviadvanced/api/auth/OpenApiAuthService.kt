package com.baldystudios.androidjetpackmviadvanced.api.auth

import androidx.lifecycle.LiveData
import com.baldystudios.androidjetpackmviadvanced.api.auth.network_responses.LoginResponse
import com.baldystudios.androidjetpackmviadvanced.api.auth.network_responses.RegistrationResponse
import com.baldystudios.androidjetpackmviadvanced.util.GenericApiResponse
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface OpenApiAuthService {

    @POST("account/login")
    @FormUrlEncoded
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): LiveData<GenericApiResponse<LoginResponse>>

    @POST("account/register")
    @FormUrlEncoded
    fun register(
        @FieldMap registerUserMap: Map<String, String>
    ): LiveData<GenericApiResponse<RegistrationResponse>>

}