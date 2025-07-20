package com.quantstock.qsmobile.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.quantstock.qsmobile.R
import com.quantstock.qsmobile.ui.common.PasswordTextField
import com.quantstock.qsmobile.ui.navigation.items.LoginItems
import com.quantstock.qsmobile.viewmodels.AuthViewModel
import com.quantstock.qsmobile.viewmodels.LoginState

@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
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
                text = stringResource(R.string.welcome),
                modifier = Modifier.padding(bottom = 10.dp),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = stringResource(R.string.email)) },
                modifier = Modifier.padding(top = 2.dp, bottom = 3.dp)
            )
            PasswordTextField(
                label = { Text(text = stringResource(R.string.password)) },
                password = password,
                onPasswordChange = { password = it },
                modifier = Modifier.padding(top = 3.dp, bottom = 10.dp)
            )

            Button(
                onClick = {
                    authViewModel.login(email, password)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(text = stringResource(R.string.login))
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
                    navController.navigate(LoginItems.Register.route)
                }) {
                Text(text = stringResource(R.string.create_account))
            }

            if (status.isNotEmpty()) Text(status, color = Color.Red)
        }
    }
}