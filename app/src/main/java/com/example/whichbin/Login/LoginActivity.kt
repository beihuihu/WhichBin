package com.example.whichbin.Login

import android.app.AlertDialog
import android.os.Bundle
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.whichbin.R
import com.example.whichbin.BaseActivity
import com.example.whichbin.MainActivity
import com.example.whichbin.http.ClassicService
import com.example.whichbin.http.LoginResult
import com.example.whichbin.http.ServiceCreater
import com.example.whichbin.ui.setting.PlaceActivity
import com.example.whichbin.userData
import kotlinx.android.synthetic.main.activity_login.*
import java.io.*
import java.security.AccessController.getContext
import kotlin.jvm.internal.Intrinsics

class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        //记住密码功能
        val prefs = getSharedPreferences("userInfo",Context.MODE_PRIVATE)
        val isRemember = prefs.getBoolean("remember_password", false)
        if (isRemember) {
            val account = prefs.getString("account", "")
            val password = prefs.getString("password", "")

            accountEdit.setText(account)
            passwordEdit.setText(password)
            rememberPass.isChecked = true
        }

        visiterBtn.setOnClickListener() {
            userData.userName="游客"
            userData.credits=0
            val string: String? = prefs.getString("place", null as String?)
            if (string == null) {
                Toast.makeText(this, "请选择城市", Toast.LENGTH_SHORT).show()
                startActivityForResult(Intent(this, PlaceActivity::class.java), 1)
            }
            else
            {
                userData.currentPlace=string
                if (string== "上海市" ) {
                    userData.Ltitles=arrayOf("可回收物", "湿垃圾", "干垃圾", "有害垃圾")
                } else {
                    userData.Ltitles=arrayOf("可回收物", "厨余垃圾", "其他垃圾", "有害垃圾")
                }
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        login.setOnClickListener{
            val account = accountEdit.text.toString()
            val password = passwordEdit.text.toString()
            val map = hashMapOf("username" to account, "password" to password)
            ServiceCreater.creatLogin(ClassicService::class.java, this@LoginActivity)
                .login(map)
                .enqueue(object : retrofit2.Callback<LoginResult> {
                    override fun onResponse(call: retrofit2.Call<LoginResult>, response: retrofit2.Response<LoginResult>) {
                        val result = response.body()
                        if (result != null) {
                            if (result.status == "ok") {
                                with(prefs.edit()) {
                                    userData.userName = account
                                    userData.credits = result.credits

                                    if (rememberPass.isChecked) {
                                        putBoolean("remember_password", true)
                                        putString("account", account)
                                        putString("password", password)
                                    } else {
                                        remove("account")
                                        remove("password")
                                    }
                                    apply()
                                }

                                val place = prefs.getString("place", null)
                                if (place == null) {
                                    Toast.makeText(this@LoginActivity, "登陆成功，请选择城市", Toast.LENGTH_SHORT).show()
                                    startActivityForResult(Intent(this@LoginActivity, PlaceActivity::class.java), 1)
                                } else {
                                    Toast.makeText(this@LoginActivity, "登陆成功", Toast.LENGTH_SHORT).show()
                                    userData.currentPlace = place
                                    userData.Ltitles = if (place == "上海市") {
                                        arrayOf("可回收物", "湿垃圾", "干垃圾", "有害垃圾")
                                    } else {
                                        arrayOf("可回收物", "厨余垃圾", "其他垃圾", "有害垃圾")
                                    }
                                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            } else {
                                AlertDialog.Builder(this@LoginActivity)
                                    .setTitle("登陆失败")
                                    .setMessage("请重新检查用户名与密码。\n或者联系管理员。")
                                    .setCancelable(false)
                                    .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                                    .show()
                            }
                        }
                    }

                    override fun onFailure(call: retrofit2.Call<LoginResult>, t: Throwable) {
                        Toast.makeText(this@LoginActivity, "无法连接服务器", Toast.LENGTH_SHORT).show()
                    }
                })


        register.setOnClickListener() {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) {
            Toast.makeText(this@LoginActivity, "不选择城市，无法进入app", Toast.LENGTH_SHORT).show()
        } else if (data != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}