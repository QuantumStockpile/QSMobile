package com.quantstock.qsmobile.ui.screens

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.quantstock.qsmobile.viewmodels.ItemsViewModel

@Composable
fun MyItemsScreen(viewModel: ItemsViewModel = hiltViewModel()) {
    val items = viewModel.items

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.onItemScanned() }) {
                Text("+") // later replace with Icon
            }
        }
    ) { padding ->
        LazyColumn(contentPadding = padding) {
            items(items) { item ->
                ListItem(
                    headlineContent = { Text(item) },
                    modifier = Modifier.fillMaxWidth()
                )
                HorizontalDivider()
            }
        }
    }

    // Dialog for entering item name
    var tempName by remember { mutableStateOf("") }
    if (viewModel.showNameDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onDialogDismiss() },
            title = { Text("New Item") },
            text = {
                OutlinedTextField(
                    value = tempName,
                    onValueChange = { tempName = it },
                    label = { Text("Item Name") }
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.onNameConfirm(tempName)
                    tempName = ""
                }) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    viewModel.onDialogDismiss()
                    tempName = ""
                }) {
                    Text("Cancel")
                }
            }
        )
    }
}