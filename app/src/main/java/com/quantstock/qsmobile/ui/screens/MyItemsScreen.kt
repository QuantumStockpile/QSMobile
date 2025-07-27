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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.quantstock.qsmobile.api.Equipment
import com.quantstock.qsmobile.api.ItemStatus
import com.quantstock.qsmobile.ui.common.ExpandedItemView
import com.quantstock.qsmobile.ui.common.FilterButton
import com.quantstock.qsmobile.ui.common.ItemCard
import com.quantstock.qsmobile.viewmodels.EquipmentFilter
import com.quantstock.qsmobile.viewmodels.ItemsViewModel
import com.quantstock.qsmobile.viewmodels.toLabel

@Composable
fun MyItemsScreen(viewModel: ItemsViewModel = hiltViewModel()) {
    val items = viewModel.items

    var searchQuery by remember { mutableStateOf("") }

    var selectedItem by remember { mutableStateOf<Equipment?>(null) }

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
                searchFiltered.filter { it.status == ItemStatus.AVAILABLE }

            EquipmentFilter.STATUS_IN_USE ->
                searchFiltered.filter { it.status == ItemStatus.CHECKED_OUT }

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

            // small filter reminder
            if (viewModel.currentFilter != EquipmentFilter.NONE) {
                Text(
                    text = "Filtered by: ${viewModel.currentFilter.toLabel()}",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onSurface
                )
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
                        selectedItem = item
                    }
                }
            }
        }
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            onClick = { viewModel.onItemScanned() }) {
            Text("+") // later replace with Icon
        }
    }


    if (selectedItem != null) {
        ExpandedItemView(selectedItem!!) {
            selectedItem = null
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