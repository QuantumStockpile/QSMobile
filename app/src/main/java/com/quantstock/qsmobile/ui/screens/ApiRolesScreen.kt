package com.quantstock.qsmobile.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.quantstock.qsmobile.R
import com.quantstock.qsmobile.api.RoleResponse
import com.quantstock.qsmobile.viewmodels.AuthViewModel
import com.quantstock.qsmobile.viewmodels.RolesViewModel

@Composable
fun ApiRolesScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    rolesViewModel: RolesViewModel = hiltViewModel()
) {
    val roleId by authViewModel.roleId.collectAsState()

    val roles by rolesViewModel.roles.collectAsState()
    val loading by rolesViewModel.loading.collectAsState()
    val error by rolesViewModel.error.collectAsState()

    // Trigger fetching when roleId changes
    LaunchedEffect(roleId) {
        rolesViewModel.fetchRolesIfAdmin(roleId)
    }


    // UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier.weight(1f)
        ) {
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
                    Text(
                        text = "Loading roles…",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                error != null -> {
                    Text(
                        text = "Error: $error",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                else -> {
                    LazyColumn {
                        items(roles) { role ->
                            RoleItem(role)
                        }
                    }
                }
            }
        }

        Button(
            onClick = {
                authViewModel.logout()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = stringResource(R.string.logout))
        }
    }
}

@Composable
fun RoleItem(role: RoleResponse) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = "ID: ${role.id}", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Description: ${role.description}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Created at: ${role.created_at}", style = MaterialTheme.typography.bodySmall)
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 4.dp),
            thickness = DividerDefaults.Thickness,
            color = DividerDefaults.color
        )
    }
}