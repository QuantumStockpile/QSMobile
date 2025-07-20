package com.quantstock.qsmobile.ui.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.quantstock.qsmobile.scanner.ScannerPreview
import com.quantstock.qsmobile.utils.PermissionManager
import com.quantstock.qsmobile.viewmodels.ItemsViewModel
import com.quantstock.qsmobile.viewmodels.ScannerViewModel

@Composable
fun ScannerScreen(
    viewModel: ScannerViewModel = hiltViewModel(),
    itemsViewModel: ItemsViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    // 👇 Observe ViewModel state
    val scannedCode by viewModel.scannedCode.collectAsState()

    // 🔑 Permission state
    var permissionGranted by remember { mutableStateOf(false) }
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionGranted = isGranted
    }

    // 🔑 Request permission on first composition
    LaunchedEffect(Unit) {
        when {
            PermissionManager.isPermissionGranted(context, Manifest.permission.CAMERA) -> {
                permissionGranted = true
            }

            else -> {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    // 🔑 UI
    if (permissionGranted) {
        Box(modifier = Modifier.fillMaxSize()) {
            ScannerPreview { code ->
                viewModel.onCodeScanned(code)
            }

            scannedCode?.let {
                Text(
                    text = "Scanned: $it",
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .background(Color.Black.copy(alpha = 0.7f))
                        .padding(16.dp)
                )
                itemsViewModel.onCodeScanned(it)
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Camera permission is denied, please allow Camera permissions.",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
