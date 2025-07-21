package com.quantstock.qsmobile.api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiInfo(
    val api_version: String,
    val build_version: String
)

@JsonClass(generateAdapter = true)
data class RefreshToken(
    val refresh_token: String
)

@JsonClass(generateAdapter = true)
data class CreateUserRequest(
    val username: String,
    val email: String,
    val password: String
)

@JsonClass(generateAdapter = true)
data class UserResponse(
    val id: Int,
    val created_at: String,
    val updated_at: String,
    val username: String,
    val email: String,
    val password_hash: String,
    val is_active: Boolean
)

@JsonClass(generateAdapter = true)
data class TokenResponse(
    val access_token: String,
    val refresh_token: String,
    val token_type: String
)

@JsonClass(generateAdapter = true)
data class RoleResponse(
    val id: Int,
    val created_at: String,
    val updated_at: String,
    val description: String
)