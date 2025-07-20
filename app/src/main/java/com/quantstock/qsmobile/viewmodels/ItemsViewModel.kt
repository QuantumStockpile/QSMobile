package com.quantstock.qsmobile.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ItemsViewModel @Inject constructor() : ViewModel() {
    private val _items = mutableStateListOf<String>()

    val items: List<String> get() = _items

    var showNameDialog by mutableStateOf(false)
        private set

    fun onItemScanned() {
        showNameDialog = true
    }

    fun onCodeScanned(scannedCode: String) {
        if (scannedCode.isNotBlank()) {
            _items.add(scannedCode)
        }
    }

    fun onDialogDismiss() {
        showNameDialog = false
    }

    fun onNameConfirm(name: String) {
        if (name.isNotBlank()) {
            _items.add(name)
        }
        showNameDialog = false
    }
}