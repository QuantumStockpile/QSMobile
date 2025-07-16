package com.quantstock.qsmobile.ui.navigation.items

sealed class LoginItems(val route: String) {
    object Login : LoginItems(route = "login")
    object Register : LoginItems(route = "register")
}