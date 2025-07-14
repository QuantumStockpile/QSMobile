package com.quantstock.qsmobile.ui.navigation

import android.content.Context
import androidx.compose.runtime.*
import androidx.navigation.compose.*
import com.quantstock.qsmobile.api.*
import com.quantstock.qsmobile.ui.screens.CreateUserScreen
import com.quantstock.qsmobile.ui.screens.LoginScreen

@Composable
fun LoginNavigation(apiService: ApiService, context: Context, onLoginSuccess: () -> Unit) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(apiService, context, navController, onLoginSuccess)
        }

        composable("register") {
            CreateUserScreen(apiService, context, navController)
        }
    }
}