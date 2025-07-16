package com.quantstock.qsmobile.viewmodels

import android.app.Application
import android.content.Context
import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.quantstock.qsmobile.R
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
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
}

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val appContext: Context = application.applicationContext

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Loading)
    private val _token = MutableStateFlow<String?>(null)
    val loginState: StateFlow<LoginState> = _loginState
    val token: StateFlow<String?> = _token

    init {
        // Load token from SharedPreferences at startup
        viewModelScope.launch {
            val storedToken = TokenStorage.getAccessToken(appContext)
            _token.value = storedToken
        }
    }

    fun login(username: String, password: String, apiService: ApiService) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val response = apiService.login(username = username, password = password)
                TokenStorage.saveTokens(getApplication(), response.access_token, response.refresh_token)
                _token.value = response.access_token
                _loginState.value = LoginState.Success
            } catch (e: HttpException) {
                val errorMessage = parseHttpError(e)
                _loginState.value = LoginState.Error("${getString(appContext, R.string.login_failed)} $errorMessage")
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("${getString(appContext, R.string.unknown_error)} ${e.localizedMessage}")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            TokenStorage.clear(appContext)
            _token.value = null
        }
    }

    private fun parseHttpError(exception: HttpException) : String {
        val errorBody = exception.response()?.errorBody()?.string()
        if (errorBody.isNullOrBlank()) return getString(appContext, R.string.unknown_error)

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
            "${getString(appContext, R.string.unknown_error)}: ${e.localizedMessage}"
        }
    }
}