package com.quantstock.qsmobile.api

import com.squareup.moshi.JsonClass

// Data Transfer Object for the users microservice
// vals MUST be named the same way the api expects them as if called by curl - this is a retrofit requirement
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