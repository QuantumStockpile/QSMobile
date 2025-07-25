package com.quantstock.qsmobile.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.quantstock.qsmobile.ui.navigation.items.MainItems
import com.quantstock.qsmobile.viewmodels.AuthViewModel
import com.quantstock.qsmobile.viewmodels.ItemsViewModel
import com.quantstock.qsmobile.viewmodels.AdministrationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(authViewModel: AuthViewModel, itemsViewModel: ItemsViewModel) {
    val navController = rememberNavController()

    lateinit var menuItems: List<MainItems>

    val roleId by authViewModel.roleId.collectAsState()
    menuItems = if (roleId == 2) {
        listOf(MainItems.Administration, MainItems.Scanner, MainItems.Items)
    } else {
        listOf(MainItems.Scanner, MainItems.Items)
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                val currentDestination =
                    navController.currentBackStackEntryAsState().value?.destination
                menuItems.forEach { item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                painter = painterResource(id = item.icon),
                                contentDescription = item.label
                            )
                        },
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
            startDestination = MainItems.Scanner.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(MainItems.Scanner.route) {
                ScannerScreen(itemsViewModel = itemsViewModel)
            }

            composable(MainItems.Items.route) {
                MyItemsScreen(viewModel = itemsViewModel)
            }

            composable(MainItems.Administration.route) { backStackEntry ->
                val administrationViewModel: AdministrationViewModel = hiltViewModel(backStackEntry)
                AdministrationScreen(authViewModel, administrationViewModel)
            }

//            composable(MainItems.Server.route) { backStackEntry ->
//                val administrationViewModel: AdministrationViewModel = hiltViewModel(backStackEntry)
//                ApiRolesScreen(authViewModel, administrationViewModel)
//            }
        }

    }
}