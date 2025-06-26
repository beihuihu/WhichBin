package com.example.whichbin

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.whichbin.database.DatabaseUtil
import com.example.whichbin.database.GarbageDatabase
import com.example.whichbin.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private val dbHelper = GarbageDatabase(this,"RubDatabase.db",1)

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
    }
}