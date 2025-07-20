package com.quantstock.qsmobile.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.quantstock.qsmobile.ui.navigation.items.LoginItems
import com.quantstock.qsmobile.ui.screens.CreateUserScreen
import com.quantstock.qsmobile.ui.screens.LoginScreen
import com.quantstock.qsmobile.viewmodels.AuthViewModel


@Composable
fun LoginNavigation(authViewModel: AuthViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = LoginItems.Login.route) {
        composable(LoginItems.Login.route) {
            LoginScreen(navController, authViewModel)
        }

        composable(LoginItems.Register.route) {
            CreateUserScreen(navController, authViewModel)
        }
    }
}