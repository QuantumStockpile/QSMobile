package com.quantstock.qsmobile.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.quantstock.qsmobile.api.EquipmentType
import com.quantstock.qsmobile.api.Location
import com.quantstock.qsmobile.viewmodels.ItemsViewModel

// a Composable filter button
@Composable
fun FilterButton(
    itemsViewModel: ItemsViewModel,
    locations: List<Location>,
    types: List<EquipmentType>
) {
    var expanded by remember { mutableStateOf(false) }

    val staticFilters = listOf(
        EquipmentFilter.None,
        EquipmentFilter.CreatedAtNewest,
        EquipmentFilter.CreatedAtOldest
    )

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Filter")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            staticFilters.forEach {
                DropdownMenuItem(
                    text = { Text(it.toLabel()) },
                    onClick = {
                        itemsViewModel.onFilterSelected(it)
                        expanded = false
                    }
                )
            }

            HorizontalDivider()

            locations.forEach { location ->
                DropdownMenuItem(
                    text = { Text("Location: ${location.name}") },
                    onClick = {
                        itemsViewModel.onFilterSelected(EquipmentFilter.Location(location.id, location.name))
                        expanded = false
                    }
                )
            }

            types.forEach { type ->
                DropdownMenuItem(
                    text = { Text("Type: ${type.name}") },
                    onClick = {
                        itemsViewModel.onFilterSelected(EquipmentFilter.Type(type.id, type.name))
                        expanded = false
                    }
                )
            }
        }
    }
}