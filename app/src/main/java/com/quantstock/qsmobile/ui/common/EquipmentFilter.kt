package com.quantstock.qsmobile.ui.common

// filter classes
sealed class EquipmentFilter {
    data object None : EquipmentFilter()
    object CreatedAtNewest : EquipmentFilter()
    object CreatedAtOldest : EquipmentFilter()
    data class Status(val status: String) : EquipmentFilter()
    data class Location(val locationId: Int, val locationName: String) : EquipmentFilter()
    data class Type(val typeId: Int, val typeName: String) : EquipmentFilter()
}

fun EquipmentFilter.toLabel(): String = when (this) {
    is EquipmentFilter.None -> "No filter"
    is EquipmentFilter.CreatedAtNewest -> "Newest first"
    is EquipmentFilter.CreatedAtOldest -> "Oldest first"
    is EquipmentFilter.Status -> "Status: ${this.status}"
    is EquipmentFilter.Location -> "Location: ${this.locationName}"
    is EquipmentFilter.Type -> "Type: ${this.typeName}"
}