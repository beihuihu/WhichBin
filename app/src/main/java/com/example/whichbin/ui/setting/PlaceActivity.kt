package com.example.whichbin.ui.setting

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.whichbin.BaseActivity
import com.example.whichbin.R
import com.example.whichbin.userData
import kotlinx.android.synthetic.main.activity_place.*
import kotlin.jvm.internal.Intrinsics

class PlaceActivity : BaseActivity() {
    private val data = listOf("上海市", "成都市", "北京市", "广州市", "深圳市", "杭州市", "武汉市")

    /* access modifiers changed from: protected */
    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_place)
        getSupportActionBar()?.hide()
        listView.adapter=ArrayAdapter(this,android.R.layout.simple_list_item_1,data)

        listView.setOnItemClickListener{ parent,view,position,id->
            val str: String = data[position]
            val edit: SharedPreferences.Editor = getSharedPreferences("userInfo", 0).edit()
            edit.putString("place", str)
            edit.apply()
            userData.currentPlace=str
            Toast.makeText(this, Intrinsics.stringPlus("已成功修改为", str), Toast.LENGTH_SHORT).show()
            if (str=="上海市" ) {
                userData.Ltitles=arrayOf("可回收物", "湿垃圾", "干垃圾", "有害垃圾")
            } else {
                userData.Ltitles=arrayOf("可回收物", "厨余垃圾", "其他垃圾", "有害垃圾")
            }
            val intent = Intent()
            intent.putExtra("place", str)
            setResult(RESULT_OK, intent);
            finish()
        }
        place_back.setOnClickListener{
            finish()
        }
    }

}