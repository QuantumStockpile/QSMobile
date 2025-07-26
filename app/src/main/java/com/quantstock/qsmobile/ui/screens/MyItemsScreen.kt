package com.quantstock.qsmobile.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.quantstock.qsmobile.ui.common.FilterButton
import com.quantstock.qsmobile.ui.common.ItemCard
import com.quantstock.qsmobile.viewmodels.EquipmentFilter
import com.quantstock.qsmobile.viewmodels.ItemsViewModel

@Composable
fun MyItemsScreen(viewModel: ItemsViewModel = hiltViewModel()) {
    val items = viewModel.items

    var searchQuery by remember { mutableStateOf("") }

    var selectedItemName by remember { mutableStateOf<String?>(null) }

    val filteredItems = remember(searchQuery, items, viewModel.currentFilter) {
        val searchFiltered = if (searchQuery.isBlank()) {
            items
        } else {
            items.filter { item ->
                item.name.contains(searchQuery, ignoreCase = true)
            }
        }
        when (viewModel.currentFilter) {
            EquipmentFilter.NONE -> searchFiltered
            EquipmentFilter.CREATED_AT_NEWEST ->
                searchFiltered.sortedByDescending { it.createdAt }
            EquipmentFilter.CREATED_AT_OLDEST ->
                searchFiltered.sortedBy { it.createdAt }
            EquipmentFilter.STATUS_AVAILABLE ->
                searchFiltered.filter { it.status.equals("Available", ignoreCase = true) }
            EquipmentFilter.STATUS_IN_USE ->
                searchFiltered.filter { it.status.equals("In Use", ignoreCase = true) }
            EquipmentFilter.LOCATION_HQ ->
                searchFiltered.filter { it.location.name == "Headquarters" }
            EquipmentFilter.LOCATION_WAREHOUSE ->
                searchFiltered.filter { it.location.name == "Warehouse" }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // search bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search items…") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(50)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // filter button
                FilterButton(viewModel)
            }

            // actual items
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                items(filteredItems) { item ->
                    ItemCard(item) {
                        selectedItemName = item.name
                    }
                }
            }
        }

        FloatingActionButton(
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
            onClick = { viewModel.onItemScanned() }) {
            Text("+") // later replace with Icon
        }
    }


    if (selectedItemName != null) {
        AlertDialog(
            onDismissRequest = { selectedItemName = null },
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