package com.quantstock.qsmobile.api

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


// this handles retrofit's requests and endpoints
interface ApiService {
    @POST("qsusers/users/")
    suspend fun createNewUser(@Body userRequest: CreateUserRequest): UserResponse

    @FormUrlEncoded
    @POST("qsusers/token")
    suspend fun login(
        @Field("grant_type") grantType: String = "password",
        @Field("username") username: String,
        @Field("password") password: String,
    ): TokenResponse

    @POST("qsusers/refresh")
    suspend fun refresh(@Body refreshToken: RefreshToken): TokenResponse

    @GET("qsusers/users/")
    suspend fun getUsers(): List<UserResponse>

    @POST("qsusers/roles/elevate")
    suspend fun elevateUser(@Query("target_email") targetEmail: String): Boolean

    @GET("qsinventory/inventory/")
    suspend fun getInventory(): EquipmentResponse

    @POST("qsinventory/inventory/")
    suspend fun createItem(@Body equipment: EquipmentRequest): Boolean

    @PATCH("qsinventory/inventory/{item_id}/patch")
    suspend fun updateItem(
        @Path("item_id") itemId: Int,
        @Body equipment: EquipmentRequest
    ): EquipmentResponse

    @GET("qsinventory/types/")
    suspend fun getEquipmentTypes(): EquipmentTypeResponse

    @POST("qsinventory/types/")
    suspend fun createEquipmentType(@Body equipmentType: EquipmentTypeRequest): Boolean

    @GET("qsinventory/types/{item_id}")
    suspend fun getEquipmentType(@Path("item_id") id: Int): EquipmentType

    @PATCH("qsinventory/types/{item_id}")
    suspend fun updateEquipmentType(@Path("item_id") id: Int, @Body type: EquipmentTypeRequest): Boolean

    @DELETE("qsinventory/types/{item_id}")
    suspend fun deleteEquipmentType(@Path("item_id") id: Int): Boolean

    @GET("qsinventory/locations/")
    suspend fun getLocations(): LocationResponse

    @POST("qsinventory/locations/")
    suspend fun createLocation(@Body location: LocationRequest): Boolean

    @DELETE("qsinventory/locations/{item_id}")
    suspend fun deleteLocation(@Path("item_id") id: Int): Boolean

}