package com.example.whichbin.ui.home

import android.content.Context
import androidx.appcompat.widget.AppCompatEditText
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import com.example.whichbin.R
import android.text.TextWatcher
import android.text.Editable
import android.view.MotionEvent
import android.graphics.Rect
import kotlin.Throws

/**
 * @author sunday
 * 2013-12-04
 */
class EditTextWithDel : AppCompatEditText {
    private var imgAble: Drawable? = null
    private var mContext: Context

    constructor(context: Context) : super(context) {
        mContext = context
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        mContext = context
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        mContext = context
        init()
    }

    private fun init() {
        imgAble = mContext.resources.getDrawable(R.drawable.delete)
       // imgAble.setBounds(0, 0, 40, 40)
        addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {
                setDrawable()
            }
        })
        setDrawable()
    }

    //设置删除图片
    private fun setDrawable() {
        if (length() < 1) setCompoundDrawablesWithIntrinsicBounds(
            null,
            null,
            null,
            null
        ) else setCompoundDrawablesWithIntrinsicBounds(null, null, imgAble, null)
    }

    // 处理删除事件
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (imgAble != null && event.action == MotionEvent.ACTION_UP) {
            val eventX = event.rawX.toInt()
            val eventY = event.rawY.toInt()
            val rect = Rect()
            getGlobalVisibleRect(rect)
            rect.left = rect.right - 50
            if (rect.contains(eventX, eventY)) setText("")
        }
        return super.onTouchEvent(event)
    }

    companion object {
        private const val TAG = "EditTextWithDel"
    }
}