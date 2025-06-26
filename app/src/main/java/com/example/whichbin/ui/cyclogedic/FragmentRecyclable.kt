package com.example.whichbin.ui.cyclogedic

import android.view.View
import android.os.Bundle
import com.example.whichbin.R
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment.*
import kotlin.jvm.internal.Intrinsics
 /* compiled from: Fragment0.kt */
class FragmentRecyclable : Fragment() {


   override fun onDestroyView() {
        super.onDestroyView()

    }

    override fun onActivityCreated(bundle: Bundle?) {
        super.onActivityCreated(bundle)
        picture.setImageResource(R.drawable.frag1)
        title1.text ="可回收物"
        task1.text = "是指废纸张、废塑料、废玻璃制品、废金属、废织物等适宜回收、可循环利用的生活废弃品。"
        title2.text = "投放要求"
        task2.text = "1. 轻投轻放\n2. 清洁干燥，避免污染\n3. 废纸尽量平整\n4. 立体包装物请清空内容物，清洁后压扁投放\n5. 有尖锐边角的，应包裹后投放"
    }

    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View? {
        Intrinsics.checkNotNullParameter(layoutInflater, "inflater")
        return layoutInflater.inflate(R.layout.fragment, viewGroup, false)
    }
}