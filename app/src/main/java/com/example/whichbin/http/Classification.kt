package com.example.whichbin.http

import com.google.gson.annotations.SerializedName

data class Classification (@SerializedName("class")val garbageClass:String, val infer_time:Float, val score:Float){
}