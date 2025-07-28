package com.quantstock.qsmobile.viewmodels

import androidx.lifecycle.ViewModel
import com.quantstock.qsmobile.api.Request
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

data class HardcodedUser(
    val id: Int,
    val username: String,
    val email: String
)

@HiltViewModel
class RequestsViewModel @Inject constructor() : ViewModel() {
    private val currentUser = HardcodedUser(
        id = 1,
        username = "John Doe",
        email = "john.doe@mail.com"
    )

    private val _requests = MutableStateFlow<List<Request>>(emptyList())
    val requests: StateFlow<List<Request>> = _requests
}