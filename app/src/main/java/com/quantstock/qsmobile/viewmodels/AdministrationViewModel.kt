package com.quantstock.qsmobile.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quantstock.qsmobile.api.ApiService
import com.quantstock.qsmobile.api.RoleResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RolesViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {

    private val _roles = MutableStateFlow<List<RoleResponse>>(emptyList())
    val roles: StateFlow<List<RoleResponse>> = _roles

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchRolesIfAdmin(roleId: Int?) {
        if (roleId == 2) {
            viewModelScope.launch {
                _loading.value = true
                _error.value = null
                try {
                    _roles.value = apiService.getRoles()
                } catch (e: Exception) {
                    _error.value = e.localizedMessage
                } finally {
                    _loading.value = false
                }
            }
        } else {
            _roles.value = emptyList()
        }
    }
}
