package com.dawalisoftwares.easycheck

import android.graphics.Bitmap
import android.graphics.RectF
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.core.ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888
import androidx.camera.core.ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
import androidx.camera.core.ImageCapture.FLASH_MODE_AUTO
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.camera.view.TransformExperimental
import androidx.camera.view.transform.CoordinateTransform
import androidx.camera.view.transform.ImageProxyTransformFactory
import androidx.camera.view.transform.OutputTransform
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.nio.ByteBuffer
import java.util.concurrent.Executors


@TransformExperimental
@ExperimentalGetImage
class ScanCardActivity : AppCompatActivity() {
    private lateinit var recognizer: TextRecognizer
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var preveiwView: PreviewView
    private lateinit var camera: Camera
    private lateinit var flash: MaterialButtonToggleGroup
    private lateinit var imageAnalysis: ImageAnalysis
    private lateinit var analysis: ImageAnalysis.Analyzer
    private lateinit var capture: ImageCapture
    private lateinit var sampleImage: ImageView
    private lateinit var box: ImageView
    private var source: OutputTransform? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_card)
        recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        preveiwView = findViewById(R.id.camera_preview)
        flash = findViewById(R.id.toggle_group)
        sampleImage = findViewById(R.id.sample_image)
        box = findViewById(R.id.image_box)

        imageAnalysis =
            ImageAnalysis.Builder().setBackpressureStrategy(STRATEGY_KEEP_ONLY_LATEST)
                .setOutputImageFormat(OUTPUT_IMAGE_FORMAT_RGBA_8888).build()
        analysis = ImageAnalysis.Analyzer { imageProxy ->

            val target: OutputTransform =
                ImageProxyTransformFactory().getOutputTransform(imageProxy)
            runOnUiThread { source = preveiwView.outputTransform }
            if (source != null) {
                val coordinateTransform = CoordinateTransform(source!!, target)
                val rect = RectF(
                    box.left.toFloat(),
                    box.top.toFloat(),
                    box.right.toFloat(),
                    box.bottom.toFloat()
                )
                coordinateTransform.mapRect(rect)
                val croppedImage = cropImage(imageProxy, rect)
                val inputImage: InputImage =
                    InputImage.fromBitmap(croppedImage, imageProxy.imageInfo.rotationDegrees)
                runOnUiThread { sampleImage.setImageBitmap(croppedImage) }
                recognizer.process(inputImage).addOnSuccessListener { text ->
                    Toast.makeText(applicationContext, text.text, Toast.LENGTH_SHORT).show()
                }.addOnCompleteListener { imageProxy.close() }
            }

        }
        imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor(), analysis)

        capture = ImageCapture.Builder().setFlashMode(FLASH_MODE_AUTO).build()


        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(this))
        flash.addOnButtonCheckedListener { _, _, isChecked ->
            if (camera.cameraInfo.hasFlashUnit()) camera.cameraControl.enableTorch(
                isChecked
            )
        }

        source = preveiwView.outputTransform
    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        var preview: Preview = Preview.Builder().build()
        var cameraSelector: CameraSelector =
            CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
        preview.setSurfaceProvider(preveiwView.surfaceProvider)
        camera = cameraProvider.bindToLifecycle(
            this as LifecycleOwner,
            cameraSelector,
            preview,
            capture,
            imageAnalysis
        )

    }

    private fun cropImage(image: ImageProxy, rect: RectF): Bitmap {
        val planes: Array<out ImageProxy.PlaneProxy> = image.planes
        val plane = planes[0]

        val buffer = plane.buffer

        val width = image.width
        val height = image.height
        val rowStride = plane.rowStride

        val bufferSize = rowStride * height
        val data = ByteArray(bufferSize)
        buffer[data]

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.copyPixelsFromBuffer(ByteBuffer.wrap(data))

        return Bitmap.createBitmap(
            bitmap,
            rect.left.toInt(),
            rect.top.toInt(),
            rect.width().toInt(),
            rect.height().toInt()
        )
    }


}