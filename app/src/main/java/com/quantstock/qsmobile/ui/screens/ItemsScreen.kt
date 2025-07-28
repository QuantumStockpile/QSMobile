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
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.quantstock.qsmobile.api.Equipment
import com.quantstock.qsmobile.ui.common.EquipmentFilter
import com.quantstock.qsmobile.ui.common.ExpandedItemView
import com.quantstock.qsmobile.ui.common.FilterButton
import com.quantstock.qsmobile.ui.common.ItemCard
import com.quantstock.qsmobile.ui.common.SpeedDial
import com.quantstock.qsmobile.ui.common.toLabel
import com.quantstock.qsmobile.viewmodels.EquipmentTypeViewModel
import com.quantstock.qsmobile.viewmodels.ItemsViewModel
import com.quantstock.qsmobile.viewmodels.LocationViewModel

@Composable
fun ItemsScreen(
    roleId: Int,
    itemsViewModel: ItemsViewModel,
    locationsViewModel: LocationViewModel,
    typesViewModel: EquipmentTypeViewModel
) {
    val items by itemsViewModel.items.collectAsState()
    val currentFilter by itemsViewModel.currentFilter.collectAsState()

    val locations by locationsViewModel.locations.collectAsState()
    val types by typesViewModel.types.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    var selectedItem by remember { mutableStateOf<Equipment?>(null) }

    val filteredItems = remember(searchQuery, items, currentFilter) {
        val searchFiltered = if (searchQuery.isBlank()) {
            items
        } else {
            items.filter { item ->
                item.name.contains(searchQuery, ignoreCase = true)
            }
        }

        when (currentFilter) {
            is EquipmentFilter.None -> searchFiltered

            is EquipmentFilter.CreatedAtNewest ->
                searchFiltered.sortedByDescending { it.created_at }

            is EquipmentFilter.CreatedAtOldest ->
                searchFiltered.sortedBy { it.created_at }

            is EquipmentFilter.Status -> {
                val filter = currentFilter as EquipmentFilter.Status
                searchFiltered.filter { it.status.equals(filter.status, ignoreCase = true) }
            }

            is EquipmentFilter.Location -> {
                val filter = currentFilter as EquipmentFilter.Location
                searchFiltered.filter { it.location.id == filter.locationId }
            }

            is EquipmentFilter.Type -> {
                val filter = currentFilter as EquipmentFilter.Type
                searchFiltered.filter { it.type.id == filter.typeId }
            }
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
                FilterButton(itemsViewModel, locations, types)
            }

            // small filter reminder
            if (currentFilter != EquipmentFilter.None) {
                Text(
                    text = "Filtered by: ${currentFilter.toLabel()}",
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
        if(roleId == 2) {
            SpeedDial(
                modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
                onNewItem = { itemsViewModel.onItemScanned() },
                onNewType = { typesViewModel.addEquipmentType("Testing") },
                onNewLocation = { locationsViewModel.addLocation("Long-term Storage", "A long term storage") }
            )
        }
    }


    if (selectedItem != null) {
        ExpandedItemView(selectedItem!!) {
            selectedItem = null
        }
    }

    // Dialog for entering item name
    var tempName by remember { mutableStateOf("") }
    if (itemsViewModel.showNameDialog) {
        AlertDialog(
            onDismissRequest = { itemsViewModel.onDialogDismiss() },
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
                    itemsViewModel.onNameConfirm(tempName)
                    tempName = ""
                }) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    itemsViewModel.onDialogDismiss()
                    tempName = ""
                }) {
                    Text("Cancel")
                }
            }
        )
    }
}