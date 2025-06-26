package com.example.whichbin.classification

import android.content.res.AssetManager
import android.graphics.Bitmap

class MobileNet {
    external fun init(assetManager: AssetManager):Boolean
    external fun detect(bitmap: Bitmap):FloatArray

    init {
        System.loadLibrary("mobileNet")
    }

}