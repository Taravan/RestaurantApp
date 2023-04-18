package com.isp.restaurantapp.views

import android.Manifest
import android.annotation.SuppressLint
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
import androidx.lifecycle.ViewModelProvider
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.isp.restaurantapp.R
import com.isp.restaurantapp.databinding.ActivityMainBinding
import com.isp.restaurantapp.viewModels.MainActivityVM
import com.isp.restaurantapp.views.customerPart.CustomerActivity
import com.isp.restaurantapp.views.customerPart.StaffMainScreen
import java.util.concurrent.Executors

private const val CAMERA_PERMISSION_REQUEST_CODE = 1

@ExperimentalGetImage
class MainActivity : AppCompatActivity(), GestureDetector.OnGestureListener {

    private lateinit var viewModel: MainActivityVM
    private lateinit var binding: ActivityMainBinding
    private lateinit var gestureDetector: GestureDetector

    private var cameraProvider: ProcessCameraProvider? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        this.setContentView(R.layout.activity_main)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[MainActivityVM::class.java]


        viewModel.fetchTables()


        // TODO: Make scanner slower


        // REDIRECT TO NEW VIEW
//        viewModel.onQrScanned("kodStoluCisloPet")
//        val newAct = Intent(this, CustomerActivity::class.java)
//        startActivity(newAct)
        // END OF REDIRECTION

        // REDIRECT TO TERMINAL
        val newAct = Intent(this, StaffMainScreen::class.java)
        startActivity(newAct)
        // END OF REDIRECTION


        viewModel.navigateToNext.observe(this) { tableToGo ->
            if (tableToGo != null) {
                cameraProvider?.unbindAll()
                val intent = Intent(this, CustomerActivity::class.java).apply {
                    putExtra("TABLE", tableToGo)
                }
                startActivity(intent)
            }
        }

        viewModel.navigateToStaffScreen.observe(this) { startStaffActivity ->
            if (startStaffActivity) {
                cameraProvider?.unbindAll()
                val intent = Intent(this, StaffMainScreen::class.java)
                startActivity(intent)
                viewModel.resetNavigateToStaffScreen()
            }
        }

        gestureDetector = GestureDetector(this, this)
        binding.swipingLayout.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }

        if (hasCameraPermission()) {
            bindAll()
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
            bindAll()
        } else {
            Toast.makeText(this, getString(R.string.camera_required), Toast.LENGTH_LONG).show()
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    // Building parts for scanner
    private fun getCamera(): CameraSelector {
        return CameraSelector.DEFAULT_BACK_CAMERA
    }

    private fun buildPreview(): Preview {
        val preview = Preview.Builder().build()
            .also {
                it.setSurfaceProvider(binding.cameraView.surfaceProvider)
            }
        return preview
    }

    private fun buildBarcodeScanner(): BarcodeScanner {
        val options = BarcodeScannerOptions.Builder().setBarcodeFormats(
            Barcode.FORMAT_QR_CODE
        ).build()
        return BarcodeScanning.getClient(options)
    }

    private fun buildImageAnalysis(scanner: BarcodeScanner): ImageAnalysis {
        val executor = Executors.newSingleThreadExecutor()
        val analysis = ImageAnalysis.Builder().setBackpressureStrategy(
            ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
        ).build()
        analysis.setAnalyzer(executor) { imageProxy ->
            processImageProxy(scanner, imageProxy)
        }
        return analysis
    }

    // Bind parts to cameraProvider
    private fun bindAll() {

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            val cameraProvider = cameraProviderFuture.get()
            val preview = buildPreview()
            val barcodeScanner = buildBarcodeScanner()
            val imageAnalysis = buildImageAnalysis(barcodeScanner)
            val cameraSelector = getCamera()

            try {
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageAnalysis
                )
            } catch (illegalStateException: IllegalStateException) {
                // If the use case has already been bound to another lifecycle or method is not called on main thread.
                Log.e("Exception", illegalStateException.message.orEmpty())

            } catch (illegalArgumentException: java.lang.IllegalArgumentException){
                // If the provided camera selector is unable to resolve a camera to be used for the given use cases.
                Log.e("Exception", illegalArgumentException.message.orEmpty())

            }

        }, ContextCompat.getMainExecutor(this))
    }

    // Analyze found qr code
    private fun processImageProxy(barcodeScanner: BarcodeScanner, imageProxy: ImageProxy) {
        imageProxy.image?.let { image ->
            val inputImage = InputImage.fromMediaImage(image, imageProxy.imageInfo.rotationDegrees)

            barcodeScanner.process(inputImage).addOnSuccessListener { barcodeList ->
                val barcode = barcodeList.getOrNull(0)
                barcode?.rawValue?.let { value ->
                    viewModel.onQrScanned(value)
                }
            }
                .addOnFailureListener {
                    // Model could not be downloaded (bad connection, not signed in to google play...)
                    Toast.makeText(this, getString(R.string.internet_googleplay_required),
                        Toast.LENGTH_LONG).show()
                    Log.e("Exception", it.message.orEmpty())
                }.addOnCompleteListener {
                    imageProxy.image?.close()
                    imageProxy.close()
                }
        }
    }

    // Start scanning again
    override fun onResume() {
        super.onResume()
        if (hasCameraPermission()) {
            bindAll()
        } else {
            requestPermission()
        }
    }

    // Detecting swipes of an user
    override fun onFling(p0: MotionEvent, p1: MotionEvent, p2: Float, p3: Float): Boolean {
        return viewModel.onFlingRegistered(p0, p1)
    }

    override fun onDown(p0: MotionEvent): Boolean {
        return false
    }

    override fun onShowPress(p0: MotionEvent) {
    }

    override fun onSingleTapUp(p0: MotionEvent): Boolean {
        return false
    }

    override fun onScroll(p0: MotionEvent, p1: MotionEvent, p2: Float, p3: Float): Boolean {
        return false
    }

    override fun onLongPress(p0: MotionEvent) {
    }
}