package com.quantstock.qsmobile.api

import com.quantstock.qsmobile.viewmodels.HardcodedUser
import com.squareup.moshi.JsonClass
import java.time.LocalDateTime

// every single Data Transfer Object for the different endpoints
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

@JsonClass(generateAdapter = true)
data class Equipment(
    val id: Int,
    val name: String,
    val type: EquipmentType,
    val serialNumber: String,
    val status: ItemStatus,
    val condition: Int,
    val location: Location,
    val photoUrl: String?,
    val qrCodeData: String?,
    val metadata: String?,     // optional json notes
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

data class EquipmentType(
    val id: Int,
    val name: String
)

data class Location(
    val id: Int,
    val name: String,
    val description: String?
)

data class Request(
    val id: Int,
    val user: HardcodedUser, // no implementation
    val status: RequestStatus,
    val requested_at: Long,
    val approved_by: HardcodedUser?, // not yet implemented
    val approved_at: Long?,
    val returnedAt: Long?,
    val note: String?,
    val items: List<RequestItem>
)

data class RequestItem(
    val id: Int,
    val requestId: Int, // instead of holding full Request to avoid recursion
    val equipment: Equipment,
    val borrowFrom: String,
    val borrowTo: String
)