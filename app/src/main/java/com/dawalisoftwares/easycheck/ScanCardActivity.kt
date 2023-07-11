package com.dawalisoftwares.easycheck

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Rect
import android.graphics.RectF
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
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
import com.dawalisoftwares.easycheck.adapters.RechargeCardSelectSimAdapter
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
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
    private lateinit var analyzeIdicator: ImageView
    private lateinit var analyzetext: TextView
    private lateinit var box: ImageView
    private var source: OutputTransform? = null
    private var lastPin = ""
    private var count = 0
    private var isAnalyzing = true
    private lateinit var preview: Preview
    private lateinit var cameraSelector: CameraSelector
    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var context: Context
    private lateinit var sims: List<Sim>
    private lateinit var rotation: Animation
    private lateinit var blink: Animation
    private lateinit var recharge: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_card)
        recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        preveiwView = findViewById(R.id.camera_preview)
        flash = findViewById(R.id.toggle_group)
        box = findViewById(R.id.image_box)
        analyzetext = findViewById(R.id.analyze_text)
        analyzeIdicator = findViewById(R.id.analyze_indicator)
        context = this@ScanCardActivity
        recharge = findViewById(R.id.load)
        blink = AnimationUtils.loadAnimation(context,R.anim.blink)
        rotation = AnimationUtils.loadAnimation(context,R.anim.rotation)
        sims = SimCardManager.getInstance(context).activeSimCards

//        preveiwView.implementationMode = PreviewView.ImplementationMode.COMPATIBLE
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
                recognizer.process(inputImage).addOnSuccessListener { text ->
                    val pin: String? = getPin(text.textBlocks)
                    if (!pin.isNullOrBlank()){
                        showAnalyze()
                        if (pin == lastPin) {
                            count++
                        }else {
                            count = 0
                        }
                        lastPin = pin
                        if (count >= 3) {
                            cameraProvider.unbindAll()
                            dismissAnalyze()
                            rechargeNow(pin)
                        }
                    }else {
                        isAnalyzing = false
                        dismissAnalyze()
                    }
                }.addOnCompleteListener { imageProxy.close() }
            }

        }
        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), analysis)

        capture = ImageCapture.Builder().setFlashMode(FLASH_MODE_AUTO).build()

        recharge.setOnClickListener {
            showAnalyze()
            capture.takePicture(Executors.newSingleThreadExecutor(),object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)
                    dismissAnalyze()
                    if (image.image != null) {
                        val inputImage: InputImage =
                            InputImage.fromMediaImage(image.image!!,image.imageInfo.rotationDegrees)
                        recognizer.process(inputImage).addOnSuccessListener { text ->
                            val pin: String? = getPin(text.textBlocks)
                            if (!pin.isNullOrBlank()){
                                rechargeNow(pin)
                            }else {
                                Toast.makeText(context,"Failed to detect pin",Toast.LENGTH_SHORT).show()
                            }
                        }.addOnCompleteListener { image.close() }
                    }

                }
            })
        }
        cameraProviderFuture.addListener(Runnable {
            cameraProvider = cameraProviderFuture.get()
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
        preview = Preview.Builder().build()
        cameraSelector =
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

    private fun getPin(blocks: List<Text.TextBlock>) : String? {
        val lines = mutableListOf<Text.Line>()
        blocks.forEach {
            it.lines.forEach {x ->
                if (hasPossiblePin(x.text)) {
                    lines.add(x)
                }
            }
        }
        var pin = getLineWithPin(lines)
        if (pin.isNullOrBlank()) {
            pin = getLineWithLargestArea(lines)
        }
        return pin
    }

    private fun getLineWithPin(lines: List<Text.Line>) : String? {
        lines.forEach {
            if (it.text.contains("pin",true) && hasPossiblePin(it.text)){
                  return it.text.filter { c -> c.isDigit() }
                }
        }
        return null
    }

    private fun getNumberOfDigits(str: String) : Int {
        return str.count { c -> c.isDigit() }
    }

    private fun hasPossiblePin(str: String) : Boolean {
        return getNumberOfDigits(str) >= 10
    }

    private fun getLineWithLargestArea(lines: List<Text.Line> ) : String? {
        val areas = mutableListOf<Int>()
        lines.forEach { areas.add(getArea(it.boundingBox)) }
        val max = areas.maxOrNull() ?: return null
        val maxIndex = areas.indexOf(max)
        return lines[maxIndex].text.filter { c -> c.isDigit() }
    }

    private fun getArea(rect: Rect?) : Int {
        return ((rect?.height() ?: 0) * (rect?.width() ?: 0))
    }

    private fun rechargeNow(pin: String) {
        dismissAnalyze()
        val view: View = layoutInflater.inflate(R.layout.recharge_select_sim,LinearLayout(context),false)
        val listView: ListView = view.findViewById(R.id.recharge_select_sim_list_view);
        val adapter = RechargeCardSelectSimAdapter(context,R.layout.recharge_select_sim_face,sims)
        listView.adapter = adapter
        val dialog =  MaterialAlertDialogBuilder(context,R.style.Theme_EasyCheck).create()
        listView.setOnItemClickListener { adapterView, _, position, _ ->
           val sim: Sim = adapterView.getItemAtPosition(position) as Sim
            if (sim.network != null) {
                sim.network!!.loadAirtime(sim,pin)
                dialog.cancel()
                finish()
            }else {
                dialog.dismiss()
                MaterialAlertDialogBuilder(context,R.style.USSDTheme).setTitle("Ops").setMessage("This sim is not yet supported")
                    .setPositiveButton(android.R.string.ok) { d, _ ->
                        d.cancel()
                        finish()
                    }.create().show()
            }
            dialog.cancel()
        }
        dialog.setView(view)
        dialog.show()
        dialog.setOnCancelListener {
            dismissAnalyze()
            bindPreview(cameraProvider) }
    }

    private fun showAnalyze() {
        isAnalyzing = true
        analyzeIdicator.visibility = View.VISIBLE
        analyzetext.visibility = View.VISIBLE
        box.setColorFilter(resources.getColor(R.color.green_400,theme))
        analyzeIdicator.startAnimation(rotation)
        analyzetext.startAnimation(blink)
    }

    private fun dismissAnalyze() {
        isAnalyzing = false
        analyzeIdicator.animation?.cancel()
        analyzetext.animation?.cancel()
        analyzeIdicator.visibility = View.GONE
        analyzetext.visibility = View.GONE
        box.setColorFilter(Color.WHITE)

    }
}