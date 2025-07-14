package com.quantstock.qsmobile.api

data class ApiInfo(
    val api_version: String,
    val build_version: String
)

data class CreateUserRequest(
    val username: String,
    val email: String,
    val password: String
)

data class CreatedUserResponse(
    val id: Int,
    val created_at: String,
    val updated_at: String,
    val username: String,
    val email: String,
    val password_hash: String,
    val is_active: Boolean
)

data class TokenResponse(
    val access_token: String,
    val refresh_token: String,
    val token_type: String
)