package com.quantstock.qsmobile.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.quantstock.qsmobile.viewmodels.EquipmentFilter
import com.quantstock.qsmobile.viewmodels.ItemsViewModel

// a Composable filter button
@Composable
fun FilterButton(viewModel: ItemsViewModel = hiltViewModel()) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Filter")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("None") },
                onClick = {
                    viewModel.onFilterSelected(EquipmentFilter.NONE)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Newest") },
                onClick = {
                    viewModel.onFilterSelected(EquipmentFilter.CREATED_AT_NEWEST)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Oldest") },
                onClick = {
                    viewModel.onFilterSelected(EquipmentFilter.CREATED_AT_OLDEST)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Available") },
                onClick = {
                    viewModel.onFilterSelected(EquipmentFilter.STATUS_AVAILABLE)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("In Use") },
                onClick = {
                    viewModel.onFilterSelected(EquipmentFilter.STATUS_IN_USE)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Location: HQ") },
                onClick = {
                    viewModel.onFilterSelected(EquipmentFilter.LOCATION_HQ)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Location: Warehouse") },
                onClick = {
                    viewModel.onFilterSelected(EquipmentFilter.LOCATION_WAREHOUSE)
                    expanded = false
                }
            )
        }
    }
}