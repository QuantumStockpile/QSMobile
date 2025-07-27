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
import com.quantstock.qsmobile.viewmodels.toLabel

// a Composable filter button
@Composable
fun FilterButton(viewModel: ItemsViewModel = hiltViewModel()) {
    var expanded by remember { mutableStateOf(false) }

    val filters = EquipmentFilter.entries

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Filter")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            filters.forEach {
                DropdownMenuItem(
                    text = { Text(it.toLabel()) },
                    onClick = {
                        viewModel.onFilterSelected(it)
                        expanded = false
                    }
                )
            }
        }
    }
}