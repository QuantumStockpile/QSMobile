package com.quantstock.qsmobile.ui.screens

import android.app.AlertDialog
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.quantstock.qsmobile.ui.common.ItemCard
import com.quantstock.qsmobile.viewmodels.ItemsViewModel

@Composable
fun MyItemsScreen(viewModel: ItemsViewModel = hiltViewModel()) {
    val items = viewModel.items

    var selectedItemName by remember { mutableStateOf<String?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.onItemScanned() }) {
                Text("+") // later replace with Icon
            }
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = padding) {
            items(items) { item ->
                ItemCard(
                    item,
                    "https://atlas-content-cdn.pixelsquid.com/stock-images/dice-B5mdRR0-600.jpg"
                ) {
                    selectedItemName = item
                }
//                ListItem(
//                    headlineContent = { Text(item) },
//                    modifier = Modifier.fillMaxWidth()
//                )
            }
        }
    }

    if (selectedItemName != null) {
        AlertDialog(
            onDismissRequest = { selectedItemName = null},
            title = {
                Text(text = "Item clicked")
            },
            text = {
                Text(text = "You clicked on: $selectedItemName")
            },
            confirmButton = {
                TextButton(onClick = { selectedItemName = null }) {
                    Text("OK")
                }
            }
        )
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