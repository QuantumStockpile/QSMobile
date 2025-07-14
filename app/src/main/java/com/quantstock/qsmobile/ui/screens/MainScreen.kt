package com.quantstock.qsmobile.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getString
import androidx.navigation.compose.*
import com.quantstock.qsmobile.api.ApiService
import kotlinx.coroutines.*
import com.quantstock.qsmobile.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    apiService: ApiService,
    context: Context
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val menuItems = listOf("Scanner", "Account")

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(16.dp))
                menuItems.forEach { item ->
                    NavigationDrawerItem(
                        label = { Text(item) },
                        selected = false,
                        onClick = {
                            navController.navigate(item.lowercase())
                            scope.launch { drawerState.close() }
                        }
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(getString(context, R.string.app_name)) },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = "account",
                modifier = Modifier.padding(paddingValues)
            ) {
                composable("scanner") {
                    ScannerScreen(context = context)
                }
                composable("account") {
                    ApiInfoScreen(apiService = apiService, context = context)
                }
            }
        }
    }
}