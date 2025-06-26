package com.example.whichbin

import android.content.Intent
import android.Manifest
import pub.devrel.easypermissions.EasyPermissions
import java.util.concurrent.TimeUnit
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorListener
import com.example.whichbin.Login.LoginActivity
import com.example.whichbin.R
import kotlinx.android.synthetic.main.activity_welcome.*
import java.util.*
import java.util.function.Consumer

class WelcomeActivity : AppCompatActivity() ,EasyPermissions.PermissionCallbacks {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_welcome)
            supportActionBar?.hide()

            //设置图片动画
            ViewCompat.animate(imageView).apply {
                //缩放，变成1.0倍
                scaleX(1.0f)
                scaleY(1.0f)
                //动画时常1秒
                duration = 1000
                //动画监听
                setListener(object : ViewPropertyAnimatorListener {
                    override fun onAnimationEnd(view: View) { //动画结束
                        //进入主界面，并结束掉该页面
                        checkPermission()
                    }

                    override fun onAnimationCancel(view: View) {
                    }

                    override fun onAnimationStart(view: View) {
                    }

                })


            }
        }

        private fun checkPermission() {
            val perms = arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            if (EasyPermissions.hasPermissions(this, *perms)) {
                startLogin()
            } else {
                EasyPermissions.requestPermissions(this, "软件运行需要以下权限，请允许", 0, *perms)
            }
        }


        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
        }

        override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        }

        override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
            if (requestCode == 0) {
                if (perms.isNotEmpty()) {
                    if (
                        perms.contains(Manifest.permission.CAMERA)
                        && perms.contains(Manifest.permission.INTERNET)
                        && perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        && perms.contains(Manifest.permission.READ_EXTERNAL_STORAGE)
                    ) {
                        startLogin()
                    }
                }
            }
        }

        fun startLogin() {
            startActivity(Intent(this@WelcomeActivity, LoginActivity::class.java))
            finish()
        }

        override fun onDestroy() {
            super.onDestroy()
        }
    }