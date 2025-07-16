package com.quantstock.qsmobile.ui.screens

import android.Manifest
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.quantstock.qsmobile.scanner.ScannerPreview
import com.quantstock.qsmobile.utils.PermissionManager
import kotlinx.coroutines.delay

@Composable
fun ScannerScreen(
    context: Context
) {
    var permissionGranted by remember { mutableStateOf(false) }
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionGranted = isGranted
    }

    val scannedCode = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(scannedCode.value) {
        scannedCode.value?.let {
            delay(850) // pause to prevent duplicate scans
            scannedCode.value = null
        }
    }

    LaunchedEffect(Unit) {
        when {
            PermissionManager.isPermissionGranted(context, Manifest.permission.CAMERA) -> {
                permissionGranted = true
            } else -> {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    if (permissionGranted) {
        Box(modifier = Modifier.fillMaxSize()) {
            ScannerPreview { code ->
                if (scannedCode.value == null) scannedCode.value = code
            }

            scannedCode.value?.let {
                Text(
                    text = "Scanned: $it",
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .background(Color.Black.copy(alpha = 0.7f))
                        .padding(16.dp)
                )
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Camera permission is denied, please allow Camera permissions.",
                textAlign = TextAlign.Center
            )
        }
    }
}