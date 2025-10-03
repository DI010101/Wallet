package com.example.paybackwallet

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors

class ScanActivity : ComponentActivity() {
    private val cameraExecutor = Executors.newSingleThreadExecutor()

    companion object { fun createIntent(context: Context) = Intent(context, ScanActivity::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val previewView = androidx.camera.view.PreviewView(this)
            setContentView(previewView)

            val barcodeOptions = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
                .build()
            val scanner = BarcodeScanning.getClient(barcodeOptions)

            val analyzer = ImageAnalysis.Builder().build().also { analysis ->
                analysis.setAnalyzer(cameraExecutor) { imageProxy ->
                    processImageProxy(scanner, imageProxy)
                }
            }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            cameraProvider.bindToLifecycle(this, cameraSelector, analyzer)
        }, ContextCompat.getMainExecutor(this))
    }

    private fun processImageProxy(scanner: com.google.mlkit.vision.barcode.BarcodeScanner, imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    for (barcode in barcodes) {
                        val raw = barcode.rawValue ?: continue
                        handleScannedBarcode(raw)
                        break
                    }
                }
                .addOnCompleteListener { imageProxy.close() }
        } else imageProxy.close()
    }

    private fun handleScannedBarcode(value: String) {
        val db = AppDatabase.get(applicationContext)
        val entity = LoyaltyCard(0, "Unbekannt", value)
        Thread { db.cardDao().insert(entity); finish() }.start()
    }

    override fun onDestroy() { super.onDestroy(); cameraExecutor.shutdown() }
}
