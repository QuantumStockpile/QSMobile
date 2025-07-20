package com.quantstock.qsmobile.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.quantstock.qsmobile.viewmodels.AuthViewModel
import com.quantstock.qsmobile.viewmodels.CreateUserState

@Composable
fun CreateUserScreen(navController: NavController, authViewModel: AuthViewModel = hiltViewModel()) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val state by authViewModel.createUserState.collectAsState()

    LaunchedEffect(state) {
        if (state is CreateUserState.Success) {
            // Navigate back when success
            navController.popBackStack()
        }
    }

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
                text = stringResource(R.string.create_account),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text(text = stringResource(R.string.username)) },
                modifier = Modifier.padding(top = 2.dp, bottom = 3.dp)
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = stringResource(R.string.email)) },
                modifier = Modifier.padding(top = 3.dp, bottom = 3.dp)
            )
            PasswordTextField(
                label = { Text(text = stringResource(R.string.password)) },
                password = password,
                onPasswordChange = { password = it },
                modifier = Modifier.padding(top = 3.dp, bottom = 6.dp)
            )

            Button(
                modifier = Modifier.padding(top = 6.dp),
                onClick = {
                    authViewModel.createNewUser(username, email, password)
//                    CoroutineScope(Dispatchers.IO).launch {
//                        try {
//                            val userRequest = CreateUserRequest(username, email, password)
//                            apiService.createNewUser(userRequest)
//
//                            withContext(Dispatchers.Main) {
//                                resultMessage = stringResource(R.string.account_created)
//                                navController.popBackStack() // go back to login
//                            }
//                        } catch (e: Exception) {
//                            withContext(Dispatchers.Main) {
//                                resultMessage = stringResource(R.string.registration_failed)
//                            }
//                        }
//                    }
                }) {
                Text(text = stringResource(R.string.register))
            }
            when(state) {
                is CreateUserState.Error -> {
                    Text(
                        text = (state as CreateUserState.Error).message,
                        color = Color.Red,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                is CreateUserState.Success -> {
                    Text(
                        text = stringResource(id = R.string.account_created),
                        color = Color.Green,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                else -> {}
            }
        }
    }
}