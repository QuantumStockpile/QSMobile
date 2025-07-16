package com.quantstock.qsmobile.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.quantstock.qsmobile.api.ApiService
import com.quantstock.qsmobile.ui.navigation.items.MainItems
import com.quantstock.qsmobile.viewmodels.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    apiService: ApiService,
    context: Context,
    authViewModel: AuthViewModel
) {
    val navController = rememberNavController()

    val menuItems = listOf(MainItems.Account, MainItems.Scanner)

    Scaffold(
        bottomBar = {
            NavigationBar {
                val currentDestination = navController.currentBackStackEntryAsState().value?.destination
                menuItems.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(painter = painterResource(id = item.icon), contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = currentDestination?.route == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = MainItems.Account.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(MainItems.Scanner.route) {
                ScannerScreen(context = context)
            }
            composable(MainItems.Account.route) {
                ApiInfoScreen(apiService = apiService, context = context, authViewModel = authViewModel)
            }
        }

    }
}