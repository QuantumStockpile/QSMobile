package com.quantstock.qsmobile.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.quantstock.qsmobile.R
import com.quantstock.qsmobile.ui.common.UserCard
import com.quantstock.qsmobile.viewmodels.AuthViewModel
import com.quantstock.qsmobile.viewmodels.UserManagementViewModel

@Composable
fun UserManagementScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    userManagementViewModel: UserManagementViewModel = hiltViewModel()
) {
    val roleId by authViewModel.roleId.collectAsState()

    val users by userManagementViewModel.users.collectAsState()
    val loading by userManagementViewModel.loading.collectAsState()
    val error by userManagementViewModel.error.collectAsState()
    val showDialog by userManagementViewModel.showDialog.collectAsState()
    val selectedUser by userManagementViewModel.selectedUser.collectAsState()

    // Fetch users on first launch
    LaunchedEffect(roleId) {
        userManagementViewModel.fetchUsersIfAdmin(roleId)
    }

    // Dialog for elevation confirmation
    if (showDialog && selectedUser != null) {
        AlertDialog(
            onDismissRequest = { userManagementViewModel.dismissDialog() },
            title = { Text("Elevate User") },
            text = {
                Text("Elevate ${selectedUser?.email} to admin?")
            },
            confirmButton = {
                Button(
                    onClick = { userManagementViewModel.elevateUser(roleId) }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { userManagementViewModel.dismissDialog() }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    // Main content
    Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
        when {
            roleId == null -> {
                Text("Loading role info…")
            }

            roleId != 2 -> {
                Text(
                    text = stringResource(R.string.insufficient_permissions),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }

            loading -> {
                CircularProgressIndicator()
            }

            error != null -> {
                Text(
                    text = "Error: $error",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }

            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = padding
                ) {
                    items(users) { user ->
                        UserCard(
                            user = user,
                            onClick = { clickedUser ->
                                userManagementViewModel.onUserSelected(clickedUser)
                            }
                        )
                    }
                }
            }
        }
    }
}