package com.quantstock.qsmobile.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quantstock.qsmobile.api.ApiService
import com.quantstock.qsmobile.api.Location
import com.quantstock.qsmobile.api.LocationRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {
    private val _locations = MutableStateFlow<List<Location>>(emptyList())
    val locations: StateFlow<List<Location>> = _locations

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        loadLocations()
    }

    fun loadLocations() {
        viewModelScope.launch {
            try {
                val response = apiService.getLocations()
                _locations.value = response.items
                Log.d("It worked - loc", response.items.size.toString())
            } catch (e: Exception) {
                errorMessage = "Failed to load types: ${e.message}"
            }
        }
    }

    fun addLocation(name: String, description: String?) {
        viewModelScope.launch {
            try {
                val newLocation = LocationRequest(name = name, description = description)
                apiService.createLocation(newLocation)
                loadLocations()
            } catch (e: Exception) {
                errorMessage = "Failed to add type: ${e.message}"
            }
        }
    }

    fun deleteLocation(id: Int) {
        viewModelScope.launch {
            try {
                apiService.deleteLocation(id)
                loadLocations()
            } catch (e: Exception) {
                errorMessage = "Failed to delete: ${e.message}"
            }
        }
    }
}