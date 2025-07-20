package com.quantstock.qsmobile.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScannerViewModel @Inject constructor() : ViewModel() {
    private val _scannedCode = MutableStateFlow<String?>(null)
    val scannedCode: StateFlow<String?> = _scannedCode

    fun onCodeScanned(code: String) {
        if (_scannedCode.value == null) {
            _scannedCode.value = code
            // Optionally: process code (API call, etc.)
            viewModelScope.launch {
                delay(850) // reset after delay
                _scannedCode.value = null
            }
        }
    }
}