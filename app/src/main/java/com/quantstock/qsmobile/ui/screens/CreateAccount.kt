package com.quantstock.qsmobile.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getString
import androidx.navigation.NavController
import com.quantstock.qsmobile.api.*
import kotlinx.coroutines.*
import com.quantstock.qsmobile.R
import com.quantstock.qsmobile.ui.common.PasswordTextField

@Composable
fun CreateUserScreen(apiService: ApiService, context: Context, navController: NavController) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var resultMessage by remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(all = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(bottom = 10.dp),
                text = getString(context, R.string.create_account),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text(text = getString(context, R.string.username)) },
                modifier = Modifier.padding(top = 2.dp, bottom = 3.dp)
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = getString(context, R.string.email)) },
                modifier = Modifier.padding(top = 3.dp, bottom = 3.dp)
            )
            PasswordTextField(
                label = { Text(text = getString(context, R.string.password)) },
                password = password,
                onPasswordChange = { password = it },
                modifier = Modifier.padding(top = 3.dp, bottom = 6.dp)
            )

            Button(
                modifier = Modifier.padding(top = 6.dp),
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val userRequest = CreateUserRequest(username, email, password)
                            apiService.createNewUser(userRequest)

                            withContext(Dispatchers.Main) {
                                resultMessage = getString(context, R.string.account_created)
                                navController.popBackStack() // go back to login
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                resultMessage = getString(context, R.string.registration_failed)
                            }
                        }
                    }
                }) {
                Text(text = getString(context, R.string.register))
            }

            if (resultMessage.isNotEmpty()) Text(resultMessage, color = Color.Green)
        }
    }
}