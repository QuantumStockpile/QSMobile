package com.quantstock.qsmobile.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quantstock.qsmobile.api.ApiService
import com.quantstock.qsmobile.api.EquipmentType
import com.quantstock.qsmobile.api.EquipmentTypeRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EquipmentTypeViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {
    private val _types = MutableStateFlow<List<EquipmentType>>(emptyList())
    val types: StateFlow<List<EquipmentType>> = _types

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        loadEquipmentTypes()
    }

    fun loadEquipmentTypes() {
        viewModelScope.launch {
            try {
                val response = apiService.getEquipmentTypes()
                _types.value = response.items
                Log.d("It worked - equip", response.items.size.toString())
            } catch (e: Exception) {
                errorMessage = "Failed to load types: ${e.message}"
            }
        }
    }

    fun addEquipmentType(name: String) {
        viewModelScope.launch {
            try {
                val newType = EquipmentTypeRequest(name = name)
                apiService.createEquipmentType(newType)
                loadEquipmentTypes()
            } catch (e: Exception) {
                errorMessage = "Failed to add type: ${e.message}"
            }
        }
    }

    fun deleteEquipmentType(id: Int) {
        viewModelScope.launch {
            try {
                apiService.deleteEquipmentType(id)
                loadEquipmentTypes()
            } catch (e: Exception) {
                errorMessage = "Failed to delete: ${e.message}"
            }
        }
    }
}