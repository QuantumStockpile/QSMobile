package com.qtst.quantstockscanner

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Size
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.qtst.quantstockscanner.ui.theme.Blackground
import com.qtst.quantstockscanner.ui.theme.QuantStockScannerTheme
import com.qtst.quantstockscanner.utils.CameraAnalyzer
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                1001
            )
        }
        enableEdgeToEdge()
        setContent {
            val barcodeState = remember { mutableStateOf<String?>(null) }
            QuantStockScannerTheme {
                LaunchedEffect(barcodeState.value) {
                    if (barcodeState.value != null) {
                        delay(1500)
                        barcodeState.value = null
                    }
                }

                Box(Modifier.fillMaxSize()) {
                    CameraPreview(
                        onBarcodeScanned = { barcode ->
                            barcodeState.value = barcode
                        }
                    )

                    barcodeState.value?.let { barcode ->
                        Text(
                            text = "Scanned: $barcode",
                            color = Color.White,
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .offset(y = 48.dp)
                                .background(Blackground)
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CameraPreview(
    onBarcodeScanned: (String) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder().build().also {
                    it.surfaceProvider = previewView.surfaceProvider
                }

                val imageAnalysis = ImageAnalysis.Builder()
                    .setTargetResolution(Size(640, 480)) // downscale for MLKit
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()

                val analyzer = CameraAnalyzer(onBarcodeScanned) // create the analyzer

                imageAnalysis.setAnalyzer(
                    ContextCompat.getMainExecutor(ctx),
                    analyzer
                )

                val cameraSelector =
                    CameraSelector.DEFAULT_BACK_CAMERA // will add camera switching later

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalysis
                )

            }, ContextCompat.getMainExecutor(ctx))

            previewView
        },
        modifier = Modifier.fillMaxSize()
    )
}
