package com.quantstock.qsmobile

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.quantstock.qsmobile.api.ApiService
import com.quantstock.qsmobile.api.AuthInterceptor
import com.quantstock.qsmobile.ui.navigation.LoginNavigation
import com.quantstock.qsmobile.ui.screens.MainScreen
import com.quantstock.qsmobile.ui.theme.QSMobileTheme
import com.quantstock.qsmobile.viewmodels.AuthViewModel
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val apiService = remember { provideApiService(context) }
            val authViewModel: AuthViewModel = viewModel()

            val token by authViewModel.token.collectAsState()

            QSMobileTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (token != null) {
                        MainScreen(apiService, context, authViewModel)
                    } else {
                        LoginNavigation(apiService, context, authViewModel)
                    }
                }
            }
        }
    }

    fun provideApiService(context: Context): ApiService {
        val authInterceptor = AuthInterceptor(context)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://quantumstockpile.duckdns.org/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        return apiService
    }
}