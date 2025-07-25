package com.quantstock.qsmobile.api

import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


// this handles retrofit's requests and endpoints
interface ApiService {
    @POST("qsmsusers/users/")
    suspend fun createNewUser(@Body userRequest: CreateUserRequest): UserResponse

    @FormUrlEncoded
    @POST("qsmsusers/token")
    suspend fun login(
        @Field("grant_type") grantType: String = "password",
        @Field("username") username: String,
        @Field("password") password: String,
    ): TokenResponse

    @POST("qsmsusers/refresh")
    suspend fun refresh(@Body refreshToken: RefreshToken): TokenResponse

    @GET("qsmsusers/roles/")
    suspend fun getRoles(): List<RoleResponse>

    @GET("qsmsusers/users/")
    suspend fun getUsers(): List<UserResponse>

    @POST("qsmsusers/roles/elevate")
    suspend fun elevateUser(@Query("target_email") targetEmail: String): Boolean
}