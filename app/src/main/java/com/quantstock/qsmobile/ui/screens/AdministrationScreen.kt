package com.quantstock.qsmobile.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.quantstock.qsmobile.R
import com.quantstock.qsmobile.ui.common.UserCard
import com.quantstock.qsmobile.viewmodels.AdministrationViewModel
import com.quantstock.qsmobile.viewmodels.AuthViewModel

@Composable
fun AdministrationScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    administrationViewModel: AdministrationViewModel = hiltViewModel()
) {
    val roleId by authViewModel.roleId.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    val users by administrationViewModel.users.collectAsState()
    val loading by administrationViewModel.loading.collectAsState()
    val error by administrationViewModel.error.collectAsState()
    val showDialog by administrationViewModel.showDialog.collectAsState()
    val selectedUser by administrationViewModel.selectedUser.collectAsState()

    val filteredItems = remember(searchQuery, users) {
        if (searchQuery.isBlank()) {
            users
        } else {
            users.filter { user ->
                user.username.contains(searchQuery, ignoreCase = true) || user.email.contains(
                    searchQuery,
                    ignoreCase = true
                )
            }
        }
    }

    // fetch users on launch
    LaunchedEffect(roleId) {
        administrationViewModel.fetchUsersIfAdmin(roleId)
    }

    // dialog for elevation confirmation
    if (showDialog && selectedUser != null) {
        AlertDialog(
            onDismissRequest = { administrationViewModel.dismissDialog() },
            title = { Text("Elevate User") },
            text = {
                Text("Elevate ${selectedUser?.email} to admin?")
            },
            confirmButton = {
                Button(
                    onClick = { administrationViewModel.elevateUser(roleId) }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { administrationViewModel.dismissDialog() }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    // Main content
    Box(modifier = Modifier.fillMaxSize()) {
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
                CircularProgressIndicator(modifier = Modifier.padding(8.dp))
            }

            error != null -> {
                Text(
                    text = "Error: $error",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }

            else -> {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Search users…") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        singleLine = true,
                        shape = RoundedCornerShape(50)
                    )
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        items(filteredItems) { user ->
                            UserCard(
                                user = user,
                                onClick = { clickedUser ->
                                    administrationViewModel.onUserSelected(clickedUser)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}