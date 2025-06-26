package com.example.whichbin.ui.cyclogedic

import android.view.View
import android.os.Bundle
import com.example.whichbin.R
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment.*
import kotlin.jvm.internal.Intrinsics
 /* compiled from: Fragment1.kt */
class FragmentKitchen : Fragment() {


    /* synthetic */  override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onActivityCreated(bundle: Bundle?) {
        super.onActivityCreated(bundle)
        picture.setImageResource(R.drawable.frag2)
        title1.text = "厨余垃圾"
        task1.text = "即易腐垃圾，是指食材废料、剩饭剩菜、过期食品、瓜皮果核、花卉绿植、中药药渣等生物质、生活废弃物"
        title2.text = "投放要求"
        task2.text = "1. 流质的食物垃圾，如牛奶等，应直接倒进下水口\n2. 有包装物的湿垃圾应将包装物去除后分类投放\n3. 包装物请投放到对应的可回收物或干垃圾容器"
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