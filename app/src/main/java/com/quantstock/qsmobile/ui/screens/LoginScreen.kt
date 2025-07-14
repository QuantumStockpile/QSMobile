package com.quantstock.qsmobile.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.quantstock.qsmobile.api.*
import com.quantstock.qsmobile.R
import com.quantstock.qsmobile.ui.common.PasswordTextField
import com.quantstock.qsmobile.viewmodels.AuthViewModel
import com.quantstock.qsmobile.viewmodels.LoginState

@Composable
fun LoginScreen(
    apiService: ApiService,
    context: Context,
    navController: NavController,
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
    val authViewModel: AuthViewModel = viewModel()
    val loginState by authViewModel.loginState.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = getString(context, R.string.welcome),
                modifier = Modifier.padding(bottom = 10.dp),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = getString(context, R.string.email)) },
                modifier = Modifier.padding(top = 2.dp, bottom = 3.dp)
            )
            PasswordTextField(
                label = { Text(text = getString(context, R.string.password)) },
                password = password,
                onPasswordChange = { password = it },
                modifier = Modifier.padding(top = 3.dp, bottom = 10.dp)
            )

            Button(
                onClick = {
                    authViewModel.login(email, password, apiService)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(text = getString(context, R.string.login))
            }

            // When login succeeds
            LaunchedEffect(loginState) {
                if (loginState is LoginState.Success) {
                    onLoginSuccess()
                }
            }

            if (loginState is LoginState.Error) {
                Text(
                    text = (loginState as LoginState.Error).message,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            TextButton(
                modifier = Modifier.padding(top = 6.dp),
                onClick = {
                    navController.navigate("register")
                }) {
                Text(text = getString(context, R.string.create_account))
            }

            if (status.isNotEmpty()) Text(status, color = Color.Red)
        }
    }
}