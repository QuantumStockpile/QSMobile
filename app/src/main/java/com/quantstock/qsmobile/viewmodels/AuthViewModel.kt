package com.quantstock.qsmobile.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.quantstock.qsmobile.api.ApiService
import com.quantstock.qsmobile.api.GenericErrorResponse
import com.quantstock.qsmobile.api.TokenStorage
import com.quantstock.qsmobile.api.ValidationErrorResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

sealed class LoginState {
    object Loading : LoginState()
    data class Success(val token: String) : LoginState()
    data class Error(val message: String) : LoginState()
}

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Loading)
    private val _token = MutableStateFlow<String?>(null)
    val loginState: StateFlow<LoginState> = _loginState
    val token: StateFlow<String?> = _token

    init {
        // Load token from SharedPreferences at startup
        viewModelScope.launch {
            val context = getApplication<Application>().applicationContext
            val storedToken = TokenStorage.getAccessToken(context)
            _token.value = storedToken
        }
    }

    fun login(username: String, password: String, api: ApiService) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val response = api.login(username = username, password = password)
                TokenStorage.saveTokens(getApplication(), response.access_token, response.refresh_token)
                _token.value = response.access_token
                _loginState.value = LoginState.Success(response.access_token)
            } catch (e: HttpException) {
                val errorMessage = parseHttpError(e)
                _loginState.value = LoginState.Error("Login failed: $errorMessage")
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Unexpected error: ${e.localizedMessage}")
            }
        }
    }

    fun logout() {
        TokenStorage.clear(getApplication())
        _token.value = null
    }

    private fun parseHttpError(exception: HttpException) : String {
        val errorBody = exception.response()?.errorBody()?.string()
        if (errorBody.isNullOrBlank()) return "Unknown error occurred."

        return try {
            val gson = Gson()

            if (exception.code() == 422) {
                val validationError = gson.fromJson(errorBody, ValidationErrorResponse::class.java)
                validationError.detail.joinToString("\n") { it.msg }
            } else {
                val errorResponse = gson.fromJson(errorBody, GenericErrorResponse::class.java)
                errorResponse.detail
            }
        } catch (e: Exception) {
            "Failed to parse error: ${e.localizedMessage}"
        }
    }
}