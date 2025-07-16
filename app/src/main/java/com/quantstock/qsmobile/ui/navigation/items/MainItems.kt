package com.quantstock.qsmobile.ui.navigation.items

import com.quantstock.qsmobile.R

sealed class MainItems(val route: String, val icon: Int, val label: String) {
    object Scanner : MainItems(route = "scanner", icon = R.drawable.qr_scanner, label = "Scanner")
    object Account : MainItems(route = "account", icon = R.drawable.account, label = "Account")
}