package com.quantstock.qsmobile.api

import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("/qs/api-info")
    suspend fun getApiInfo(): ApiInfo

    @POST("/qs/users")
    suspend fun createNewUser(@Body user: CreateUserRequest): CreatedUserResponse

    @FormUrlEncoded
    @POST("/qs/token")
    suspend fun login(
        @Field("grant_type") grantType: String = "password",
        @Field("username") username: String,
        @Field("password") password: String,
    ): TokenResponse
}