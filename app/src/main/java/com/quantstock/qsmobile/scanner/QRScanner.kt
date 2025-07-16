package com.quantstock.qsmobile.scanner

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

class QRScanner(
    private val onBarcodeScanned: (String) -> Unit
) : ImageAnalysis.Analyzer {
    private val scanner: BarcodeScanner = BarcodeScanning.getClient()

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(
                mediaImage,
                imageProxy.imageInfo.rotationDegrees
            )

            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    handleBarcodes(barcodes, onBarcodeScanned)
                }
                //.addOnFailureListener {
                //    Log.e("QR", "Scan failed", it)
                //}
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }

    private fun handleBarcodes(
        barcodes: List<Barcode>,
        onBarcodeScanned: (String) -> Unit
    ) {
        barcodes.forEach {
            onBarcodeScanned(it.rawValue ?: "")
        }
    }
}