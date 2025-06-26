package com.example.whichbin.http

import android.content.Context
import kotlin.Metadata
import okhttp3.Interceptor
import kotlin.Throws
import java.io.IOException
import kotlin.jvm.internal.Intrinsics
import android.content.SharedPreferences
import okhttp3.Response


class SaveCookiesInterceptor(context: Context) : Interceptor {
    private val mContext: Context
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        Intrinsics.checkNotNullParameter(chain, "chain")
        val proceed = chain.proceed(chain.request())
        if (!proceed.headers("set-cookie").isEmpty()) {
            val headers = proceed.headers("set-cookie")
            val edit = mContext.getSharedPreferences("userInfo", 0).edit()
            edit.putString("cookies", headers[0])
            edit.apply()
        }
        Intrinsics.checkNotNullExpressionValue(proceed, "response")
        return proceed
    }

    init {
        Intrinsics.checkNotNullParameter(context, "mContext")
        mContext = context
    }
}