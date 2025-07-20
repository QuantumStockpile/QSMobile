package com.quantstock.qsmobile.viewmodels

import android.app.Application
import android.content.Context
import android.util.Base64
import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.quantstock.qsmobile.R
import com.quantstock.qsmobile.api.ApiService
import com.quantstock.qsmobile.api.CreateUserRequest
import com.quantstock.qsmobile.api.GenericErrorResponse
import com.quantstock.qsmobile.api.RefreshToken
import com.quantstock.qsmobile.api.TokenStorage
import com.quantstock.qsmobile.api.ValidationErrorResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

sealed class LoginState {
    object Idle : LoginState()
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
}

sealed class CreateUserState {
    object Loading : CreateUserState()
    object Success : CreateUserState()
    data class Error(val message: String) : CreateUserState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    application: Application,
    private val apiService: ApiService,
    private val tokenStorage: TokenStorage
) : ViewModel() {
    private val appContext: Context = application.applicationContext

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)

    private val _createUserState = MutableStateFlow<CreateUserState>(CreateUserState.Loading)
    private val _token = MutableStateFlow<String?>(null)
    private val _roleId = MutableStateFlow<Int?>(null)
    val loginState: StateFlow<LoginState> = _loginState
    val createUserState: StateFlow<CreateUserState> = _createUserState
    //val token: StateFlow<String?> = _token

    val roleId: StateFlow<Int?> = _roleId

    init {
        // Load token from SharedPreferences at startup
        viewModelScope.launch {
            val storedRefresh = tokenStorage.getRefreshToken()

            if (storedRefresh.isNullOrEmpty()) {
                _loginState.value = LoginState.Idle
                return@launch
            }

            try {
                val response = apiService.refresh(RefreshToken(storedRefresh))
                tokenStorage.saveTokens(
                    response.access_token,
                    response.refresh_token
                )
                decodeRoleFromToken(response.access_token)
                _token.value = response.access_token
                _loginState.value = LoginState.Success
            } catch (h: HttpException) {
                val errorBody = h.response()?.errorBody()?.string()
                if (errorBody?.contains("Invalid refresh token") == true) {
                    tokenStorage.clear()
                    _loginState.value = LoginState.Error("Session expired, please log in again.")
                } else {
                    _loginState.value = LoginState.Error("Error: ${h.message() ?: "Unknown error"}")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Unexpected error: ${e.localizedMessage}")
            }
        }
    }

    fun createNewUser(username: String, email: String, password: String) {
        viewModelScope.launch {
            _createUserState.value = CreateUserState.Loading
            try {
                val userRequest = CreateUserRequest(username, email, password)
                apiService.createNewUser(userRequest)
                _createUserState.value = CreateUserState.Success
            } catch (e: HttpException) {
                val errorMessage = parseHttpError(e)
                _createUserState.value = CreateUserState.Error(
                    "${
                        getString(
                            appContext,
                            R.string.login_failed
                        )
                    } $errorMessage"
                )
                _createUserState.value = CreateUserState.Error(
                    "${
                        getString(
                            appContext,
                            R.string.login_failed
                        )
                    } $errorMessage"
                )
            } catch (e: Exception) {
                _createUserState.value = CreateUserState.Error(
                    "${
                        getString(
                            appContext,
                            R.string.unknown_error
                        )
                    } ${e.localizedMessage}"
                )
            }
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Idle
            try {
                val response = apiService.login(username = username, password = password)
                decodeRoleFromToken(response.access_token)
                tokenStorage.saveTokens(response.access_token, response.refresh_token)
                _token.value = response.access_token
                _loginState.value = LoginState.Success
            } catch (e: HttpException) {
                val errorMessage = parseHttpError(e)
                _loginState.value = LoginState.Error(
                    "${
                        getString(
                            appContext,
                            R.string.login_failed
                        )
                    } $errorMessage"
                )
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(
                    "${
                        getString(
                            appContext,
                            R.string.unknown_error
                        )
                    } ${e.localizedMessage}"
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            tokenStorage.clear()
            _token.value = null
            _loginState.value = LoginState.Error("Logged out.")
        }
    }

    private fun parseHttpError(exception: HttpException): String {
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

    private fun decodeRoleFromToken(token: String) {
        try {
            val parts = token.split(".")
            if (parts.size >= 2) {
                val payloadJson = String(Base64.decode(parts[1], Base64.URL_SAFE))
                val jsonObj = JSONObject(payloadJson)
                if (jsonObj.has("role")) {
                    _roleId.value = jsonObj.getInt("role")
                }
            }
        } catch (e: Exception) {
            _roleId.value = null
        }
    }
}