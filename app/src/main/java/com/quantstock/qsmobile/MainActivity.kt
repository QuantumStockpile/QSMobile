package com.quantstock.qsmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.quantstock.qsmobile.ui.navigation.LoginNavigation
import com.quantstock.qsmobile.ui.screens.MainScreen
import com.quantstock.qsmobile.ui.theme.QSMobileTheme
import com.quantstock.qsmobile.viewmodels.AuthViewModel
import com.quantstock.qsmobile.viewmodels.ItemsViewModel
import com.quantstock.qsmobile.viewmodels.LoginState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val authViewModel: AuthViewModel = hiltViewModel()
            val itemsViewModel: ItemsViewModel = hiltViewModel()
            val loginState by authViewModel.loginState.collectAsState()

            QSMobileTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when(loginState) {
                        is LoginState.Success -> {
                            MainScreen(authViewModel, itemsViewModel)
                        }
                        else -> {
                            LoginNavigation(authViewModel)
                        }
                    }
                }
            }
        }
    }
}