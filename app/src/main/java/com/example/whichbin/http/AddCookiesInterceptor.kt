package com.example.whichbin.http

import android.content.Context
import kotlin.Metadata
import okhttp3.Interceptor
import kotlin.Throws
import java.io.IOException
import kotlin.jvm.internal.Intrinsics
import android.text.TextUtils
import okhttp3.Response

class AddCookiesInterceptor(context: Context) : Interceptor {
    private val mContext: Context
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        Intrinsics.checkNotNullParameter(chain, "chain")
        val newBuilder = chain.request().newBuilder()
        val string =
            mContext.getSharedPreferences("userInfo", 0).getString("cookies", null as String?)
        if (!TextUtils.isEmpty(string)) {
            newBuilder.removeHeader("cookie")
            newBuilder.addHeader("cookie", string)
        }
        val proceed = chain.proceed(newBuilder.build())
        Intrinsics.checkNotNullExpressionValue(proceed, "chain.proceed(builder.build())")
        return proceed
    }

    init {
        Intrinsics.checkNotNullParameter(context, "mContext")
        mContext = context
    }
}