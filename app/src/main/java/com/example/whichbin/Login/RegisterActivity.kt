package com.example.whichbin.Login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.whichbin.BaseActivity
import com.example.whichbin.R
import com.example.whichbin.http.ClassicService
import com.example.whichbin.http.RegisterResult
import com.example.whichbin.http.ServiceCreater
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class RegisterActivity : BaseActivity() {
    private val accountList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()

        register.setOnClickListener() {
            val username  =accountEdit.text.toString()
            val password = passwordEdit.text.toString()
            val confirmPassword = passwordCheckEdit.text.toString().trim()
            when {
                username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() -> {
                    showToast("请正确输入信息")
                    passwordEdit.text = null
                    passwordCheckEdit.text = null
                }
                password != confirmPassword -> {
                    showToast("两次密码不一致，请重新输入")
                    passwordEdit.text = null
                    passwordCheckEdit.text = null
                }
                else -> {
                    val requestBody = hashMapOf(
                        "username" to username,
                        "password" to password
                    )

                    ServiceCreater.creatRegister(ClassicService::class.java)
                        .register(requestBody)
                        .enqueue(object : Callback<RegisterResult> {
                            override fun onResponse(call: Call<RegisterResult>, response: Response<RegisterResult>) {
                                val result = response.body()
                                if (result?.status == "ok") {
                                    showToast("注册成功")
                                } else {
                                    val show = AlertDialog.Builder(this@RegisterActivity)
                                        .setTitle("注册失败")
                                        .setMessage("请重新检查用户名与密码。\n或者联系管理员。")
                                        .setCancelable(false)
                                        .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                                        .show()
                                    passwordEdit.text = null
                                    passwordCheckEdit.text = null
                                }
                            }

                            override fun onFailure(call: Call<RegisterResult>, t: Throwable) {
                                showToast("无法连接服务器")
                            }
                        })
                    finish()
                }
            }
        }
        }

    private fun showToast(message: String) {
        Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_SHORT).show()
    }
}
