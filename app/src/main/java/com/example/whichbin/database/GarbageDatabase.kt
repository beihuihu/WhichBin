package com.example.whichbin.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class GarbageDatabase (val context: Context,name:String,version:Int):
    SQLiteOpenHelper(context,name,null,version){

    private val createShanghai="create table ShanghaiTable ("+
            " name text primary key,"+
            "id INTERGER,"+
            "type text)"

    private val createChengdu="create table ChengduTable ("+
            " name text primary key,"+
            "id INTERGER,"+
            "type text)"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(createShanghai)
        db?.execSQL(createChengdu)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}