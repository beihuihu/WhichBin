package com.example.whichbin.classification

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.FileProvider
import com.example.whichbin.BaseActivity
import com.example.whichbin.Login.Feedback.FeedbackActivity
import com.example.whichbin.R
import com.example.whichbin.database.DatabaseUtil
import com.example.whichbin.http.ClassicService
import com.example.whichbin.http.ServiceCreater
import com.example.whichbin.http.Classification
import com.example.whichbin.http.Scores
import com.example.whichbin.userData
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.activity_photo.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.*

class PhotoActivity : BaseActivity() {
    private var context: Context? = null
    private val cropPhoto = 69
    private val fromAlbum = 2
    private var imageUri: Uri? = null
    private var outputImage: File? = null
    private val resultLabel = ArrayList<String>()
    private lateinit var squeezencnn: MobileNet
//    private val squeezencnn = MobileNet(assets)
    private val takePhoto = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)

        supportActionBar?.hide()

        // 确保Context就绪后再初始化模型
        squeezencnn = MobileNet().apply {
//            init(assets)
            if (!init(assets)) {
                Toast.makeText(this@PhotoActivity, "模型加载错误", Toast.LENGTH_SHORT).show()
//                finish()
            }
        }

        readCacheLabelFromLocalFile()
        context = this

        photo_back.setOnClickListener { finish() }

        takePhotoBtn.setOnClickListener {
            createFile("output_image")
            val intent = Intent("android.media.action.IMAGE_CAPTURE").apply {
                putExtra("output", imageUri)
            }
            startActivityForResult(intent, takePhoto)
        }

        fromAlbumBtn.setOnClickListener {
            createFile("output_image")
            val intent = Intent("android.intent.action.GET_CONTENT").apply {
                type = "image/*"
            }
            startActivityForResult(intent, fromAlbum)
        }

        question.setOnClickListener {
            if (userData.userName != "游客") {
                Intent(this, FeedbackActivity::class.java).apply {
                    putExtra("Uri", imageUri.toString())
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(this)
                }
            } else {
                Toast.makeText(this, "登录才可使用该功能~", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != RESULT_OK) {
            findViewById<TextView>(R.id.question).visibility = View.GONE
            findViewById<CardView>(R.id.detect_result).visibility = View.GONE
            findViewById<ImageView>(R.id.detect_imageView).setImageBitmap(null)
            return
        }

        when (requestCode) {
            takePhoto -> {
                UCrop.of(imageUri!!, imageUri!!)
                    .withAspectRatio(1f, 1f)
                    .withMaxResultSize(896, 896)
                    .start(this)
            }
            fromAlbum -> {
                data?.data?.let { uri ->
                    UCrop.of(uri, imageUri!!)
                        .withAspectRatio(1f, 1f)
                        .withMaxResultSize(896, 896)
                        .start(this)
                }
            }
            cropPhoto -> {
                data?.let {
                    findViewById<ImageView>(R.id.detect_imageView).visibility = View.VISIBLE
                    UCrop.getOutput(it)?.let { outputUri ->
                        getBitmapFromUri(outputUri)?.let { bitmap ->
                            findViewById<ImageView>(R.id.detect_imageView).setImageBitmap(
                                rotateIfRequired(bitmap, outputImage!!)
                            )
                            Merge(bitmap)
                        }
                    }
                }
            }
        }
    }

    private fun getBitmapFromUri(uri: Uri): Bitmap? {
        return contentResolver.openFileDescriptor(uri, "r")?.use { parcelFileDescriptor ->
            BitmapFactory.decodeFileDescriptor(parcelFileDescriptor.fileDescriptor)
        }
    }

    private fun readCacheLabelFromLocalFile() {
        try {
            openFileInput("synset.txt").use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).use { reader ->
                    reader.forEachLine { line ->
                        resultLabel.add(line)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun predict_image(bitmap: Bitmap): String {
        val inputBmp = Bitmap.createScaledBitmap(
            bitmap.copy(Bitmap.Config.ARGB_8888, true),
            224, 224, false
        )

        return try {
            val result = squeezencnn.detect(inputBmp)
            val maxResult = get_max_result(result)

            if (result[maxResult] < 0.75) {
                "no result"
            } else {
                resultLabel[maxResult]
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "no result"
        }
    }

    private fun Merge(bitmap: Bitmap) {
        val classicService = ServiceCreater.creat(ClassicService::class.java, this)
        val part = MultipartBody.Part.createFormData(
            "image",
            outputImage!!.name,
            RequestBody.create(
                MediaType.parse("multipart/form-data"),
                outputImage!!
            )
        )

        val inputBmp = Bitmap.createScaledBitmap(
            bitmap.copy(Bitmap.Config.ARGB_8888, true),
            224, 224, false
        )

        try {
            val result1 = squeezencnn.detect(inputBmp)

            if (userData.userName != "游客") {
                classicService.getScores(part)?.enqueue(object : Callback<Scores?> {
                    override fun onResponse(call: Call<Scores?>, response: Response<Scores?>) {
                        response.body()?.let { scores ->
                            val label = get_max_score(result1, scores.scores)
                            if (label != -1) {
                                showResult(label)
                            } else {
                                showNoResultToast()
                            }
                        } ?: run {
                            val label = get_max_result(result1)
                            if (label != -1) {
                                showResult(label)
                            } else {
                                showNoResultToast()
                            }
                        }
                    }

                    override fun onFailure(call: Call<Scores?>, t: Throwable) {
                        val label = get_max_result(result1)
                        if (label != -1) {
                            showResult(label)
                        } else {
                            showNoResultToast()
                        }
                    }

                })
            } else {
                val label = get_max_result(result1)
                if (label != -1) {
                    showResult(label)
                } else {
                    showNoResultToast()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            findViewById<CardView>(R.id.detect_result).visibility = View.GONE
            findViewById<TextView>(R.id.question).visibility = View.VISIBLE
        }
    }

    private fun showResult(label: Int) {
        findViewById<CardView>(R.id.detect_result).visibility = View.VISIBLE
        findViewById<TextView>(R.id.question).visibility = View.VISIBLE
        getResult(resultLabel[label])
    }

    private fun showNoResultToast() {
        Toast.makeText(this, "不能识别该图像", Toast.LENGTH_SHORT).show()
    }

    private fun get_max_score(result1: FloatArray, result2: FloatArray): Int {
        var maxScore = result1[0] * result2[0]
        var maxIndex = 0

        result1.forEachIndexed { index, value ->
            val score = Math.sqrt((value * result2[index]).toDouble()).toFloat()
            if (maxScore < score) {
                maxScore = score
                maxIndex = index
            }
        }

        return if (Math.sqrt(maxScore.toDouble()) > 0.5) maxIndex else -1
    }

    private fun get_max_result(result: FloatArray): Int {
        var maxValue = result[0]
        var maxIndex = 0

        result.forEachIndexed { index, value ->
            if (maxValue < value) {
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
                outputImage!!
            )
        } else {
            Uri.fromFile(outputImage)
        }
    }

    private fun getResult(result: String) {
        val foundResults = DatabaseUtil.foundResult(this, result, userData.currentPlace)
        if (foundResults.isNotEmpty()) {
            findViewById<TextView>(R.id.name).text = foundResults[0].name
            findViewById<TextView>(R.id.type).text = foundResults[0].type

            when (foundResults[0].type) {
                userData.Ltitles[0] -> {
                    findViewById<ImageView>(R.id.typeimage).setImageResource(R.drawable.trash2)
                    findViewById<TextView>(R.id.detail).text = """
                        投放要求：
                        1.轻投轻放
                        2.清洁干燥，避免污染
                        3.废纸尽量平整
                        4.立体包装物请清空内容物，清洁后压扁投放
                        5.有尖锐边角的，应包裹后投放
                    """.trimIndent()
                }
                userData.Ltitles[1] -> {
                    findViewById<ImageView>(R.id.typeimage).setImageResource(R.drawable.trash4)
                    findViewById<TextView>(R.id.detail).text = """
                        投放要求：
                        1.投放时请注意轻放
                        2.已破损的请连带包装或包裹后轻放
                        3.如易挥发，请密封后投放
                    """.trimIndent()
                }
                userData.Ltitles[2] -> {
                    findViewById<ImageView>(R.id.typeimage).setImageResource(R.drawable.trash3)
                    findViewById<TextView>(R.id.detail).text = """
                        投放要求：
                        1.流质的食物垃圾，如牛奶等，应直接倒进下水口
                        2.有包装物的湿垃圾应将包装物去除后分类投放
                        3.包装物请投放到对应的可回收物或干垃圾容器
                    """.trimIndent()
                }
                userData.Ltitles[3] -> {
                    findViewById<ImageView>(R.id.typeimage).setImageResource(R.drawable.trash1)
                    findViewById<TextView>(R.id.detail).text = """
                        投放要求：
                        1.投放时请注意轻放
                        2.已破损的请连带包装或包裹后轻放
                        3.如易挥发，请密封后投放
                    """.trimIndent()
                }
            }
        }
    }

    private fun showNoResult() {
        findViewById<CardView>(R.id.detect_result).visibility = View.GONE
        findViewById<TextView>(R.id.question).visibility = View.VISIBLE
        Toast.makeText(this, "不能识别该图像", Toast.LENGTH_SHORT).show()
    }

    private fun rotateIfRequired(bitmap: Bitmap, outputImage: File): Bitmap {
        return when (ExifInterface(outputImage.path).getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )) {
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180)
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270)
            else -> bitmap
        }
    }

    private fun rotateBitmap(bitmap: Bitmap, degree: Int): Bitmap {
        return Matrix().apply {
            postRotate(degree.toFloat())
        }.let { matrix ->
            Bitmap.createBitmap(
                bitmap, 0, 0,
                bitmap.width, bitmap.height,
                matrix, true
            ).also {
                bitmap.recycle()
            }
        }
    }
}