package com.quantstock.qsmobile.api

import com.quantstock.qsmobile.viewmodels.HardcodedUser
import com.squareup.moshi.JsonClass

// Data Transfer Object for the inventory microservice
// vals MUST be named the same way the api expects them as if called by curl - this is a retrofit requirement

@JsonClass(generateAdapter = true)
data class Equipment(
    val id: Int,
    val name: String,
    val serial_number: String,
    val status: String,
    val condition: Int,
    val photo_url: String?,
    val qr_code_data: String?,
    val metadata: Map<String, Any>?,
    val type: EquipmentType,
    val location: Location,
    val history: List<History>,
    val created_at: String,
    val updated_at: String
)

@JsonClass(generateAdapter = true)
data class EquipmentRequest(
    val name: String,
    val serial_number: String,
    val status: String,
    val condition: Int,
    val photo_url: String?,
    val qr_code_data: String?,
    val metadata: Map<String, Any>?,
    val search_vector: String?,
    val location_id: Int,
    val type_id: Int
)

@JsonClass(generateAdapter = true)
data class EquipmentResponse(
    val items: List<Equipment>,
    val total: Int
)

@JsonClass(generateAdapter = true)
data class EquipmentTypeResponse(
    val items: List<EquipmentType>,
    val total: Int
)

@JsonClass(generateAdapter = true)
data class LocationResponse(
    val items: List<Location>,
    val total: Int
)

@JsonClass(generateAdapter = true)
data class EquipmentTypeRequest(
    val name: String
)

@JsonClass(generateAdapter = true)
data class EquipmentType(
    val id: Int,
    val name: String,
    val created_at: String,
    val updated_at: String,
    val equipments: List<Equipment>? = null
)

@JsonClass(generateAdapter = true)
data class LocationRequest(
    val name: String,
    val description: String?
)

@JsonClass(generateAdapter = true)
data class Location(
    val id: Int,
    val name: String,
    val description: String,
    val created_at: String,
    val updated_at: String,
    val equipments: List<Equipment>? = null
)

@JsonClass(generateAdapter = true)
data class History(
    val id: Int,
    val action: String,
    val old: Map<String, Any>?,
    val new: Map<String, Any>?,
    val email: String,
    val created_at: String,
    val updated_at: String
)

data class Request(
    val id: Int,
    val user: HardcodedUser, // TODO no implementation
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