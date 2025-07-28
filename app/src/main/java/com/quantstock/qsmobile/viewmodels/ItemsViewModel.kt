package com.quantstock.qsmobile.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quantstock.qsmobile.api.ApiService
import com.quantstock.qsmobile.api.Equipment
import com.quantstock.qsmobile.ui.common.EquipmentFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// a viewmodel responsible for handling items returned from the api
@HiltViewModel
class ItemsViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {
    private val _items = MutableStateFlow<List<Equipment>>(emptyList())
    val items: StateFlow<List<Equipment>> = _items

    private val _currentFilter = MutableStateFlow<EquipmentFilter>(EquipmentFilter.None)
    val currentFilter: StateFlow<EquipmentFilter> = _currentFilter


    var showNameDialog by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        fetchItems()
    }

    fun onItemScanned() {
        showNameDialog = true
    }

    fun onFilterSelected(filter: EquipmentFilter) {
        _currentFilter.value = filter
    }

    fun onDialogDismiss() {
        showNameDialog = false
    }

    private fun fetchItems() {
        viewModelScope.launch {
            try {
                val response = apiService.getInventory()
                _items.value = response.items
            } catch (e: Exception) {
                errorMessage = e.localizedMessage
            }
        }
    }

    fun onCodeScanned(scannedCode: String) {
        if (scannedCode.isNotBlank()) {
            Log.d("Thing worked", "It worked!")
            /*val newItem = Equipment(
                id = items.size + 1,
                name = "New item",
                type = EquipmentType(3, "Accessory"),
                serialNumber = "SN${items.size + 1}",
                status = ItemStatus.AVAILABLE,
                condition = 10,
                location = Location(3, "Storage", "skladp2"),
                photoUrl = null,
                qrCodeData = scannedCode,
                metadata = null,
                createdAt = java.time.LocalDateTime.now(),
                updatedAt = java.time.LocalDateTime.now()
            )
            items += newItem*/
        }
    }

    fun onNameConfirm(name: String) {
        if (name.isNotBlank()) {
            Log.d("Thing worked", "It worked!")
            /*val newItem = Equipment(
                id = items.size + 1,
                name = name,
                type = EquipmentType(3, "Accessory"),
                serialNumber = "SN${items.size + 1}",
                status = ItemStatus.AVAILABLE,
                condition = 10,
                location = Location(3, "Storage", "skladp2"),
                photoUrl = null,
                qrCodeData = "qr${items.size + 1}",
                metadata = null,
                createdAt = java.time.LocalDateTime.now(),
                updatedAt = java.time.LocalDateTime.now()
            )
            items += newItem*/
        }
        showNameDialog = false
    }
}