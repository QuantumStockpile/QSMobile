package com.quantstock.qsmobile.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quantstock.qsmobile.api.ApiService
import com.quantstock.qsmobile.api.RoleResponse
import com.quantstock.qsmobile.api.UserResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdministrationViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {

    private val _roles = MutableStateFlow<List<RoleResponse>>(emptyList())
    val roles: StateFlow<List<RoleResponse>> = _roles

    private val _users = MutableStateFlow<List<UserResponse>>(emptyList())
    val users: StateFlow<List<UserResponse>> = _users

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _selectedUser = MutableStateFlow<UserResponse?>(null)
    val selectedUser: StateFlow<UserResponse?> = _selectedUser

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog

    fun fetchUsersIfAdmin(roleId: Int?) {
        if (roleId == 2) {
            viewModelScope.launch {
                _loading.value = true
                try {
                    _users.value = apiService.getUsers()
                    _error.value = null
                } catch (e: Exception) {
                    _error.value = "Failed to load users: ${e.message}"
                } finally {
                    _loading.value = false
                }
            }
        } else {
            _users.value = emptyList()
        }
    }

    fun onUserSelected(user: UserResponse) {
        _selectedUser.value = user
        _showDialog.value = true
    }

    fun dismissDialog() {
        _showDialog.value = false
    }

    fun elevateUser(roleId: Int?) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val success = apiService.elevateUser(selectedUser.value?.email ?: "")
                if (success) {
                    fetchUsersIfAdmin(roleId) // Refresh list
                }
                _error.value = if (!success) "Failed to elevate user" else null
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            } finally {
                _loading.value = false
                _showDialog.value = false
            }
        }
    }
}
