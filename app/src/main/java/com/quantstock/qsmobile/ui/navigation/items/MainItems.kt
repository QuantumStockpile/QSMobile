package com.quantstock.qsmobile.ui.navigation.items

import com.quantstock.qsmobile.R

// main screen navigation items
sealed class MainItems(val route: String, val icon: Int, val label: String) {
    object Scanner : MainItems(route = "scanner", icon = R.drawable.qr_scanner, label = "Scanner")
    object Account : MainItems(route = "account", icon = R.drawable.account, label = "Account")
    object Server : MainItems(route = "administration", icon = R.drawable.administration, label = "Administration")

    object Items : MainItems(route = "items", icon = R.drawable.items, label = "My Items")
}