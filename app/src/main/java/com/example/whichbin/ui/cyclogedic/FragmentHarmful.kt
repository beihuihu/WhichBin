package com.example.whichbin.ui.cyclogedic

import android.view.View
import android.os.Bundle
import com.example.whichbin.R
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment.*
import kotlin.jvm.internal.Intrinsics

class FragmentHarmful : Fragment() {
    
    override fun onActivityCreated(bundle: Bundle?) {
        super.onActivityCreated(bundle)
       picture.setImageResource(R.drawable.frag4)
       title1.text ="有害垃圾"
       task1.text = "是指废电池、废灯泡、废药品、废油漆及其容器对人体健康或者自然环境造成直接或者潜在危害的生活废弃物"
       title2.text = "投放要求"
       task2.text = "1. 投放时请注意轻放\n2. 易破损的请连带包装或包裹后轻放\n3. 如易挥发，请密封后投放"
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