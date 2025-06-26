package com.example.whichbin.classification

import android.content.Intent
import android.graphics.Bitmap
import android.view.WindowManager
import android.net.Uri
import android.os.*
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.whichbin.Login.Feedback.FeedbackActivity
import com.example.whichbin.R
import org.opencv.android.*
import org.opencv.core.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class RealTimeActivity : CameraActivity(), CameraBridgeViewBase.CvCameraViewListener2 {
    private lateinit var aRgba: Mat
    private lateinit var bmp: Bitmap
    private var count = 0
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var imageUri: Uri
    private var loadResult = false
    private lateinit var mRgba: Mat
    private lateinit var outputImage: File
    private lateinit var rect: Rect
    private val resultLabel = ArrayList<String>()
    private var results = arrayListOf("", "", "")
    private val scores = arrayListOf(0f, 0f, 0f)
    private var showResult = 1
    private lateinit var squeezencnn: MobileNet
    private val times = arrayListOf(0L, 0L, 0L)

    private val mLoaderCallback = object : BaseLoaderCallback(this) {
        override fun onManagerConnected(status: Int) {
            when (status) {
                LoaderCallbackInterface.SUCCESS -> {
                    val cameraView = findViewById<JavaCameraView>(R.id.java_surface_view)
                    cameraView.enableView()
                    loadResult = true
                }
                else -> {
                    super.onManagerConnected(status)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.activity_real_time)

        findViewById<JavaCameraView>(R.id.java_surface_view).apply {
            visibility = View.VISIBLE
            setCvCameraViewListener(this@RealTimeActivity)
        }

        // 确保Context就绪后再初始化模型
        squeezencnn = MobileNet().apply {
            init(assets)
//            if (!init(assets)) {
//                Toast.makeText(this@PhotoActivity, "模型加载错误", Toast.LENGTH_SHORT).show()
//                finish()
//            }
        }
        readCacheLabelFromLocalFile()
    }

    override fun onPause() {
        super.onPause()
        findViewById<JavaCameraView>(R.id.java_surface_view)?.disableView()
    }

    override fun onResume() {
        super.onResume()
        if (!OpenCVLoader.initDebug()) {
            Toast.makeText(this, "未找到OpenCV库，请使用OpenCV Manager来初始化", Toast.LENGTH_SHORT).show()
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback)
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS)
        }

        findViewById<Button>(R.id.info).setOnClickListener { saveImageToGallery() }
        findViewById<Button>(R.id.feedbackBtn).setOnClickListener { prepareFeedback() }
        findViewById<Button>(R.id.real_back).setOnClickListener { finish() }
    }

    private fun saveImageToGallery() {
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            .format(Date())
            .replace(":", "")
            .replace("-", "")
            .replace(" ", "_")

        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            "IMG_$timestamp.jpg"
        ).apply {
            if (exists()) delete()
            createNewFile()
        }

        BufferedOutputStream(FileOutputStream(file)).use { stream ->
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        }
        Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show()
    }

    private fun prepareFeedback() {
        createFile("output_image")
        BufferedOutputStream(FileOutputStream(outputImage)).use { stream ->
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        }

        Intent(this, FeedbackActivity::class.java).apply {
            putExtra("Uri", imageUri.toString())
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(this)
        }
    }

    override fun getCameraViewList(): List<CameraBridgeViewBase> {
        return listOf(findViewById(R.id.java_surface_view))
    }

    override fun onDestroy() {
        super.onDestroy()
        findViewById<JavaCameraView>(R.id.java_surface_view)?.disableView()
    }

    override fun onCameraViewStarted(width: Int, height: Int) {
        mRgba = Mat(height, width, CvType.CV_8UC4)
        aRgba = Mat(height, width, CvType.CV_8UC4)
        val size = minOf(width, height)
        rect = Rect((width - size) / 2, (height - size) / 2, size, size)
    }

    override fun onCameraViewStopped() {
    }

    override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame): Mat {
        val rgba = inputFrame.rgba()
        mRgba = Mat(rgba, rect)
        handler.sendEmptyMessageDelayed(1, 1000)

        val rotation = findViewById<JavaCameraView>(R.id.java_surface_view).display.rotation
        when (rotation) {
            0 -> Core.rotate(mRgba, aRgba, Core.ROTATE_90_CLOCKWISE)
            1 -> Core.copyTo(mRgba, aRgba, Mat())
            2 -> Core.rotate(mRgba, aRgba, Core.ROTATE_180)
            3 -> Core.rotate(mRgba, aRgba, Core.ROTATE_90_COUNTERCLOCKWISE)
        }

        bmp = Bitmap.createBitmap(aRgba.cols(), aRgba.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(aRgba, bmp)

        if (showResult == 3) {
            showResult = 1
            predict_image(bmp)
        }

        return rgba
    }

    private fun readCacheLabelFromLocalFile() {
        try {
            openFileInput("synset.txt").bufferedReader().use { reader ->
                reader.forEachLine { line ->
                    resultLabel.add(line)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun predict_image(bitmap: Bitmap) {
        val inputBmp = Bitmap.createScaledBitmap(bitmap, 224, 224, false)
        try {
            val startTime = System.currentTimeMillis()
            val result = squeezencnn.detect(inputBmp)
            val elapsedTime = System.currentTimeMillis() - startTime

            val maxResult = getMaxResult(result)
            if (result[maxResult] > 0.5) {
                results[count] = resultLabel[maxResult]
                scores[count] = result[maxResult]
                times[count] = elapsedTime

                count = if (count == 2) 0 else count + 1

                if (results[0] == results[1] || results[0] == results[2]) {
                    findViewById<TextView>(R.id.realTimeResult).text = buildString {
                        append("名称：${results[0]}  概率：${"%.2f".format(scores.maxOrNull())}  耗时：${times.minOrNull()}ms")
                    }
                } else {
                    findViewById<TextView>(R.id.realTimeResult).text = ""
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getMaxResult(result: FloatArray): Int {
        var maxIndex = 0
        var maxValue = result[0]
        result.forEachIndexed { index, value ->
            if (value > maxValue) {
                maxValue = value
                maxIndex = index
            }
        }
        return maxIndex
    }

    private fun createFile(name: String) {
        outputImage = File(externalCacheDir, name).apply {
            if (exists()) delete()
            createNewFile()
        }

        imageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(
                this,
                "com.example.whichbin.fileprovider",
                outputImage
            )
        } else {
            Uri.fromFile(outputImage)
        }
    }
}