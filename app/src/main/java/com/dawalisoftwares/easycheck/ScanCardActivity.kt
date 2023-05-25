package com.dawalisoftwares.easycheck

import android.content.Context
import android.graphics.*
import android.media.Image
import android.media.Image.Plane
import android.os.Bundle
import android.renderscript.Allocation
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicYuvToRGB
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.core.Camera
import androidx.camera.core.ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
import androidx.camera.core.ImageCapture.FLASH_MODE_AUTO
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.camera.view.TransformExperimental
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
import java.io.ByteArrayOutputStream
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_card)
        recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        preveiwView = findViewById(R.id.camera_preview)
        flash = findViewById(R.id.toggle_group)
        sampleImage = findViewById(R.id.sample_image)
        box = findViewById(R.id.image_box)

        var source: OutputTransform? = preveiwView.outputTransform
        imageAnalysis =
            ImageAnalysis.Builder().setBackpressureStrategy(STRATEGY_KEEP_ONLY_LATEST).build()
        analysis = ImageAnalysis.Analyzer { imageProxy ->
            val mediaImage = imageProxy.image
            if (mediaImage != null) {
                val target: OutputTransform =
                    ImageProxyTransformFactory().getOutputTransform(imageProxy)

                val rect = Rect(box.left, box.top, box.right, box.bottom)
                val croppedImage = cropImage(image = mediaImage, rect)
                val inputImage: InputImage = InputImage.fromBitmap(croppedImage, imageProxy.imageInfo.rotationDegrees)
                runOnUiThread { sampleImage.setImageBitmap(croppedImage) }
                recognizer.process(inputImage).addOnSuccessListener { text ->

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

    private fun cropImage(image: Image, rect: Rect): Bitmap {
        val buffer: ByteBuffer = image.planes[0].buffer
        val data = ByteArray(buffer.remaining())
        buffer.get(data)
        val bitmapImage: Bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
        return Bitmap.createBitmap(bitmapImage, rect.left, rect.top, rect.width(), rect.height())
    }


    private fun image2ByteArray(image: Image): ByteArray {
        val planes = image.planes
        val buffer = planes[0].buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        return bytes
    }


}