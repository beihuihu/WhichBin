package com.example.whichbin

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.whichbin.database.DatabaseUtil
import com.example.whichbin.database.GarbageDatabase
import com.example.whichbin.databinding.ActivityMainBinding
import com.example.whichbin.http.ClassicService
import com.example.whichbin.http.LatestModel
import com.example.whichbin.http.ServiceCreater
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.Executors

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private val dbHelper = GarbageDatabase(this,"RubDatabase.db",1)
    private val handler = MainHandler(this)
    private val findViewCache = LinkedHashMap<Int, View>()
    inner class MainHandler(private val activity: MainActivity) : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == 1) {
                Toast.makeText(this@MainActivity, "模型加载完成", Toast.LENGTH_SHORT).show()
                getSharedPreferences("userInfo", Context.MODE_PRIVATE).edit().apply {
                    putInt("modelVersion", 0)
                    apply()
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getSupportActionBar()?.hide()
        dbHelper.writableDatabase
        try {
            DatabaseUtil.addDataChengdu(this)
            DatabaseUtil.addDataShanghai(this)
        } catch (e: SQLiteConstraintException) {
            Log.w("Database", "重复数据")
        }

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        // 绑定底部导航栏（确保 menu 的 id 与导航图匹配）
        binding.navView.setupWithNavController(navController)
        initAssets(this@MainActivity)
    }
    private fun initAssets(context: Context) {
        val binFile = File(filesDir, "mobilenetv2.bin")
        val paramFile = File(filesDir, "mobilenetv2.param")
        val synsetFile = File(filesDir, "synset.txt")

        if (!binFile.exists() || !paramFile.exists() || !synsetFile.exists()) {
            Toast.makeText(context, "正在加载模型", Toast.LENGTH_SHORT).show()
            Executors.newSingleThreadExecutor().execute {
                try {
                    binFile.createNewFile()
                    paramFile.createNewFile()
                    synsetFile.createNewFile()

                    copyAssetToFile("mobilenetv2.bin", binFile)
                    copyAssetToFile("mobilenetv2.param", paramFile)
                    copyAssetToFile("synset.txt", synsetFile)

                    handler.sendEmptyMessage(1)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else if (userData.userName != "游客") {
            getLatestVersion()
        }
    }

    private fun copyAssetToFile(assetName: String, destFile: File) {
        assets.open(assetName).use { input ->
            FileOutputStream(destFile).use { output ->
                val buffer = ByteArray(1024)
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    output.write(buffer, 0, read)
                }
            }
        }
    }

    private fun getLatestVersion() {
        ServiceCreater.creat(ClassicService::class.java, this)
            .getModelVersion()
            .enqueue(object : Callback<LatestModel> {
                override fun onResponse(call: Call<LatestModel>, response: Response<LatestModel>) {
                    response.body()?.let { model ->
                        if (model.status == "ok") {
                            val sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE)
                            if (sharedPref.getInt("modelVersion", 0) < model.version) {
                                AlertDialog.Builder(this@MainActivity).apply {
                                    setMessage("有新的模型文件，是否需要更新")
                                    setTitle("提示")
                                    setPositiveButton("需要更新") { _, _ ->
                                        Toast.makeText(
                                            context,
                                            "正在下载新模型,请耐心等待，先不退出此界面",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        loadModel(model.param_url, "mobilenetv2.param")
                                        loadModel(model.bin_url, "mobilenetv2.bin")
                                        sharedPref.edit().putInt("modelVersion", model.version).apply()
                                        model.sys?.let { sysUrl ->
                                            loadModel(sysUrl, "synset.txt")
                                        }
                                    }
                                    setNeutralButton("跳过该版本") { _, _ ->
                                        Toast.makeText(
                                            context,
                                            "用户选择跳过该版本",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        sharedPref.edit().putInt("modelVersion", model.version).apply()
                                    }
                                    create().show()
                                }
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<LatestModel>, t: Throwable) {
                    // 处理失败情况
                }
            })
    }

    private fun loadModel(url: String, fileName: String) {
        ServiceCreater.creatModel(ClassicService::class.java, this)
            .loadModel(url)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    response.body()?.let { body ->
                        val file = File(filesDir, fileName).apply {
                            delete()
                            createNewFile()
                        }

                        try {
                            body.byteStream().use { input ->
                                FileOutputStream(file).use { output ->
                                    val buffer = ByteArray(4096)
                                    var read: Int
                                    while (input.read(buffer).also { read = it } != -1) {
                                        output.write(buffer, 0, read)
                                    }
                                }
                            }
                            if (fileName == "mobilenetv2.bin") {
                                Toast.makeText(this@MainActivity, "下载成功", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            error()
                            Toast.makeText(this@MainActivity, "下载失败，请检查网络", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "无法连接服务器", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun error() {
        Executors.newSingleThreadExecutor().execute {
            try {
                val binFile = File(filesDir, "mobilenetv2.bin")
                val paramFile = File(filesDir, "mobilenetv2.param")
                val synsetFile = File(filesDir, "synset.txt")

                binFile.delete()
                binFile.createNewFile()
                paramFile.delete()
                paramFile.createNewFile()
                synsetFile.delete()
                synsetFile.createNewFile()

                copyAssetToFile("mobilenetv2.bin", binFile)
                copyAssetToFile("mobilenetv2.param", paramFile)
                copyAssetToFile("synset.txt", synsetFile)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}