package com.example.whichbin.ui.cyclogedic

import android.view.View
import android.os.Bundle
import com.example.whichbin.R
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment.*
import kotlin.jvm.internal.Intrinsics
/* compiled from: Fragment2.kt */
class FragmentOther : Fragment() {

    override fun onActivityCreated(bundle: Bundle?) {
        super.onActivityCreated(bundle)
        picture.setImageResource(R.drawable.frag3)
        title1.text ="其他垃圾"
        task1.text = "是指可回收物、有害垃圾、湿垃圾（厨余垃圾）之外的其他生活废弃物，如普通无汞电池、烟蒂、园林绿化垃圾等"
        title2.text = "投放要求"
        task2.text = "1. 尽量沥干水分\n2. 难以辨别类别的生活垃圾投入干垃圾容器内"
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