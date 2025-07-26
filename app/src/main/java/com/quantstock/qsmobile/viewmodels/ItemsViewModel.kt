package com.quantstock.qsmobile.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.quantstock.qsmobile.api.Equipment
import com.quantstock.qsmobile.api.EquipmentType
import com.quantstock.qsmobile.api.Location
import com.quantstock.qsmobile.ui.common.ItemMockups
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// filter class
enum class EquipmentFilter {
    NONE,
    CREATED_AT_NEWEST,
    CREATED_AT_OLDEST,
    STATUS_AVAILABLE,
    STATUS_IN_USE,
    LOCATION_HQ,
    LOCATION_WAREHOUSE
}

// a viewmodel responsible for handling items returned from the api - not yet
@HiltViewModel
class ItemsViewModel @Inject constructor() : ViewModel() {
    var items by mutableStateOf<List<Equipment>>(emptyList())
        private set

    var currentFilter by mutableStateOf(EquipmentFilter.NONE)
        private set

    var showNameDialog by mutableStateOf(false)
        private set

    init {
        items = ItemMockups.testEquipments
    }

    fun onItemScanned() {
        showNameDialog = true
    }

    fun onFilterSelected(filter: EquipmentFilter) {
        currentFilter = filter
    }

    fun onDialogDismiss() {
        showNameDialog = false
    }

    fun onCodeScanned(scannedCode: String) {
        if (scannedCode.isNotBlank()) {
            val newItem = Equipment(
                id = items.size + 1,
                name = "New item",
                type = EquipmentType(3, "Accessory"),
                serialNumber = "SN${items.size + 1}",
                status = "Available",
                condition = "New",
                location = Location(3, "Storage", "skladp2"),
                photoUrl = null,
                qrCodeData = scannedCode,
                metadata = null,
                createdAt = java.time.LocalDateTime.now(),
                updatedAt = java.time.LocalDateTime.now()
            )
            items += newItem
        }
    }

    fun onNameConfirm(name: String) {
        if (name.isNotBlank()) {
            val newItem = Equipment(
                id = items.size + 1,
                name = name,
                type = EquipmentType(3, "Accessory"),
                serialNumber = "SN${items.size + 1}",
                status = "Available",
                condition = "New",
                location = Location(3, "Storage", "skladp2"),
                photoUrl = null,
                qrCodeData = "qr${items.size + 1}",
                metadata = null,
                createdAt = java.time.LocalDateTime.now(),
                updatedAt = java.time.LocalDateTime.now()
            )
            items += newItem
        }
        showNameDialog = false
    }
}