package com.isp.restaurantapp.views

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.isp.restaurantapp.R
import com.isp.restaurantapp.databinding.ActivityMainBinding
import com.isp.restaurantapp.models.Table
import com.isp.restaurantapp.repositories.DataMock
import com.isp.restaurantapp.views.customerPart.CustomerActivity
import com.isp.restaurantapp.views.customerPart.StaffMainScreen
import java.util.concurrent.Executors

private const val CAMERA_PERMISSION_REQUEST_CODE = 1

@ExperimentalGetImage
class MainActivity : AppCompatActivity(), GestureDetector.OnGestureListener {

    private val NUMBER_OF_SWIPES = 6
    private val data = DataMock()

    private lateinit var binding: ActivityMainBinding
    private lateinit var gestureDetector: GestureDetector
    private lateinit var tables: List<Table>
    private var swipeCounter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        this.setContentView(R.layout.activity_main)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        tables = getTables()


        val newAct = Intent(this, CustomerActivity::class.java)
            .also { it.putExtra("tableNumber", tables[0].tableNumber) }
        startActivity(newAct)

        gestureDetector = GestureDetector(this, this)
        binding.swipingLayout.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }


        if (hasCameraPermission()) {
            bindCameraUseCases()
        } else {
            requestPermission()
        }
    }


    // Permission checks
    private fun hasCameraPermission() =
        ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            bindCameraUseCases()
        } else {
            Toast.makeText(this, "Camera permission required", Toast.LENGTH_LONG).show()
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    // Usecases
    private fun bindCameraUseCases() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val previewUseCase = Preview.Builder().build()
                .also {
                    it.setSurfaceProvider(binding.cameraView.surfaceProvider)
                }

            val options = BarcodeScannerOptions.Builder().setBarcodeFormats(
                Barcode.FORMAT_QR_CODE
            ).build()

            val scanner = BarcodeScanning.getClient(options)

            val analysisUseCase = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            analysisUseCase.setAnalyzer( Executors.newSingleThreadExecutor()) {
                    imageProxy -> processImageProxy(scanner, imageProxy, cameraProvider)
            }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    previewUseCase,
                    analysisUseCase
                )
            } catch (illegalStateException: IllegalStateException) {
                // If the use case has already been bound to another lifecycle or method is not called on main thread.
                Log.e("Exception", illegalStateException.message.orEmpty())
            } catch (illegalArgumentException: IllegalArgumentException) {
                // If the provided camera selector is unable to resolve a camera to be used for the given use cases.
                Log.e("Exception", illegalArgumentException.message.orEmpty())
            }
        }, ContextCompat.getMainExecutor(this))
    }


    // Analyze found qr code
    private fun processImageProxy(barcodeScanner: BarcodeScanner, imageProxy: ImageProxy, cameraProvider: ProcessCameraProvider) {
        imageProxy.image?.let { image ->
            val inputImage = InputImage.fromMediaImage(image, imageProxy.imageInfo.rotationDegrees)

            barcodeScanner.process(inputImage).addOnSuccessListener { barcodeList ->
                val barcode = barcodeList.getOrNull(0)


                for (bc in barcodeList) {
                    val qrCode = bc.rawValue
//                    val correctQrCode = "SpravnyKod"
//                    if (qrCode == correctQrCode) {
//                        cameraProvider.unbindAll()
//
//                        val newAct = Intent(this, TableMainScreen::class.java)
//                            .also { it.putExtra("text", "Stul cislo 5") }
//                        startActivity(newAct)
//                    }

                    val tableToGo = tables.find { it.qrCode == qrCode }
                    if (tableToGo != null) {
                        cameraProvider.unbindAll()
                        val newAct = Intent(this, CustomerActivity::class.java)
                            .also { it.putExtra("tableNumber", tableToGo.tableNumber) }
                        startActivity(newAct)
                    }


                }



            }
                .addOnFailureListener {
                    // Model could not be downloaded (bad connection, not signed in to google play...)
                    Log.e("Exception", it.message.orEmpty())
                }.addOnCompleteListener {
                    imageProxy.image?.close()
                    imageProxy.close()
                }
        }
    }

    override fun onFling(p0: MotionEvent, p1: MotionEvent, p2: Float, p3: Float): Boolean {
        if (p0.y > p1.y) {
            swipeCounter++
            if (swipeCounter >= NUMBER_OF_SWIPES) {
                val intent = Intent(this, StaffMainScreen::class.java)
                startActivity(intent)

                swipeCounter = 0
                return true
            }
        } else {
            swipeCounter = 0
        }

        return false
    }

    override fun onDown(p0: MotionEvent): Boolean {
        return true
    }

    override fun onShowPress(p0: MotionEvent) {
        TODO("Not yet implemented")
    }

    override fun onSingleTapUp(p0: MotionEvent): Boolean {
        return true
    }

    override fun onScroll(p0: MotionEvent, p1: MotionEvent, p2: Float, p3: Float): Boolean {
        return true
    }

    override fun onLongPress(p0: MotionEvent) {
        TODO("Not yet implemented")
    }

    private fun getTables(): List<Table> {
        return data.getTables().toList()
    }


}