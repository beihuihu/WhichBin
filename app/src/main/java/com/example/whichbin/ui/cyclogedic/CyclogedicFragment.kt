package com.example.whichbin.ui.cyclogedic

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.whichbin.databinding.FragmentCyclogedicBinding
//import android.R
import com.example.whichbin.R as R
import android.widget.*
import com.google.android.material.tabs.TabLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.whichbin.database.GarbageDatabase
import com.example.whichbin.http.ClassicService
import com.example.whichbin.http.ServiceCreater
import com.example.whichbin.http.LoginResult
import com.example.whichbin.userData
import kotlinx.android.synthetic.main.fragment_cyclogedic.*
import kotlinx.android.synthetic.main.items.*
import kotlinx.android.synthetic.main.popu_question.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Callback

data class Question(
    val name: String,
    val type: String,
    val id: Int
)
class CyclogedicFragment : Fragment() {

    private val ItemWhat = 0
    private val Lfragments = arrayOf(FragmentRecyclable(), FragmentKitchen(), FragmentOther(), FragmentHarmful())
    private val Limg = intArrayOf(R.drawable.trash2, R.drawable.trash1, R.drawable.trash4, R.drawable.trash3)
    var Ltitles=listOf("可回收物", "厨余垃圾", "其他垃圾", "有害垃圾")
    private var _binding: FragmentCyclogedicBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCyclogedicBinding.inflate(inflater, container, false);
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {
            viewpager.adapter =object : FragmentPagerAdapter(childFragmentManager) {
                override fun getItem(position: Int): Fragment = Lfragments[position]
                override fun getCount(): Int = Lfragments.size
                override fun getPageTitle(position: Int): CharSequence? = Ltitles[position]
            }
            tabs2.setupWithViewPager(viewpager)
            val tabAt =tabs2.getTabAt(ItemWhat)
            tabAt?.select()
            viewpager.offscreenPageLimit = 3
            for (i in 0 until  tabs2.tabCount) {
//
                val inflate = LayoutInflater.from(context).inflate(R.layout.items, null)
                val textView = inflate.findViewById<TextView>(R.id.item_text)
                val imageView = inflate.findViewById<ImageView>(R.id.item_img)

                textView.text = Ltitles[i]
                imageView.setImageResource(Limg[i])
                inflate.findViewById<LinearLayout>(R.id.item_view).isClickable = false

                if (i == ItemWhat) {
                    imageView.setImageResource(Limg[i])
                    textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
                }

                binding.tabs2.getTabAt(i)?.customView = inflate
            }
            tabs2.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    //选中时进入，改变样式
                    ItemSelect(tab)
                    //onTabselected方法里面调用了viewPager的setCurrentItem 所以要想自定义OnTabSelectedListener，也加上mViewPager.setCurrentItem(tab.getPosition())就可以了
                    viewpager.currentItem = tab.position
                }
                override fun onTabUnselected(tab: TabLayout.Tab) = ItemNoSelect(tab)
                override fun onTabReselected(tab: TabLayout.Tab) {}
            })

            questionBtn.setOnClickListener{popQuestion()}

        }

    }

    private fun popQuestion() {
        lifecycleScope.launch {
            try {
                // 1. 从数据库获取5道随机题目
                val questions = withContext(Dispatchers.IO) {
                    getRandomQuestionsFromDB(5)
                } ?: run {
                    Toast.makeText(context, "题库数据为空", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                // 2. 显示答题弹窗
                showQuestionPopup(questions)
            } catch (e: Exception) {
                Toast.makeText(context, "加载题目失败: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("popQuestion", "Error:", e)
            }
        }
    }

    // 从数据库随机获取题目
    private suspend fun getRandomQuestionsFromDB(count: Int): List<Question>? = withContext(Dispatchers.IO) {
        val db = GarbageDatabase(requireContext(), "RubDatabase.db", 1).readableDatabase
        val tableName = if (userData.currentPlace == "上海市") "ShanghaiTable" else "ChengduTable"
        val questions = mutableListOf<Question>()

        db.use { database ->
            // 随机获取5条记录
            val cursor = database.rawQuery(
                "SELECT * FROM $tableName ORDER BY RANDOM() LIMIT $count",
                null
            )

            cursor.use {
                while (it.moveToNext()) {
                    questions.add(Question(
                        name = it.getString(it.getColumnIndexOrThrow("name")),
                        type = it.getString(it.getColumnIndexOrThrow("type")),
                        id = it.getInt(it.getColumnIndexOrThrow("id"))
                    ))
                }
            }
        }

        return@withContext if (questions.isEmpty()) null else questions
    }

    // 显示答题弹窗
    private fun showQuestionPopup(questions: List<Question>) {
        val popupView = LayoutInflater.from(context).inflate(R.layout.popu_question, null)
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
            true
        ).apply {
            animationStyle = R.style.anim_menu_bottombar
            isOutsideTouchable = false
        }

        popupView.question_back.setOnClickListener {
                popupWindow.dismiss()
            }

        var currentQuestionIndex = 0
        var score = 0

        // 初始化第一题
        showQuestion(popupView, questions[currentQuestionIndex], currentQuestionIndex + 1)

        // 设置选项点击监听
        listOf(
            popupView.findViewById<Button>(R.id.questionBtn1),
            popupView.findViewById<Button>(R.id.questionBtn2),
            popupView.findViewById<Button>(R.id.questionBtn3),
            popupView.findViewById<Button>(R.id.questionBtn4)
        ).forEachIndexed { index, button ->
            button.setOnClickListener {
                val isCorrect = questions[currentQuestionIndex].type == Ltitles[index]
                handleAnswer(isCorrect, popupView, questions, ++currentQuestionIndex, score, popupWindow)
            }
        }

        // 显示弹窗
        popupWindow.showAtLocation(binding.root, Gravity.CENTER, 0, 0)
    }

    // 显示题目内容
    private fun showQuestion(view: View, question: Question, questionNumber: Int) {
        view.findViewById<TextView>(R.id.question_num).text = "第${questionNumber}题(共5题)"
        view.findViewById<TextView>(R.id.question_item).text = "${question.name}属于："

        // 随机打乱选项顺序
        val options = Ltitles.toMutableList().apply { shuffle() }
        listOf(
            view.findViewById<Button>(R.id.questionBtn1),
            view.findViewById<Button>(R.id.questionBtn2),
            view.findViewById<Button>(R.id.questionBtn3),
            view.findViewById<Button>(R.id.questionBtn4)
        ).forEachIndexed { i, button ->
            button.text = "${'A' + i}.${options[i]}"
            button.tag = options[i] // 存储选项对应的实际类型
        }
    }

    // 处理答案
    private fun handleAnswer(
        isCorrect: Boolean,
        popupView: View,
        questions: List<Question>,
        nextIndex: Int,
        currentScore: Int,
        popupWindow: PopupWindow
    ) {
        val newScore = if (isCorrect) currentScore + 1 else currentScore

        if (nextIndex < questions.size) {
            // 显示下一题
            showQuestion(popupView, questions[nextIndex], nextIndex + 1)
            Toast.makeText(context,
                if (isCorrect) "答对了！" else "答错了，正确答案是${questions[nextIndex - 1].type}",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            // 所有题目完成
            popupWindow.dismiss()
            if (newScore > 0) {
                getCredit(newScore)
            }
            Toast.makeText(context,
                "答题完成！得分：$newScore/5",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    private fun getCredit(score: Int) {
        if (userData.userName == "游客") {
            Toast.makeText(context, "游客身份无法累积积分", Toast.LENGTH_SHORT).show()
            return
        }

        val service = ServiceCreater.creat(ClassicService::class.java, requireContext())
        service?.getReward()?.enqueue(object : Callback<LoginResult?> {
        override fun onResponse(call: retrofit2.Call<LoginResult?>, response: retrofit2.Response<LoginResult?>) {
            response.body()?.let {
                userData.credits = it.credits
                Toast.makeText(context, "获得${score}积分！", Toast.LENGTH_SHORT).show()
            }
        }
        override fun onFailure(call: retrofit2.Call<LoginResult?>, t: Throwable) {
            Toast.makeText(context, "积分更新失败", Toast.LENGTH_SHORT).show()
        }
        })
    }

    private fun ItemNoSelect(tab: TabLayout.Tab) {
        item_text.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        val stitle = item_text.text.toString()
        for (i in Ltitles.indices) {
            if (Ltitles[i] == stitle) {
                item_img.setImageResource(Limg[i])
            }
        }
    }

    //某个项选中，改变其样式
    private fun ItemSelect(tab: TabLayout.Tab) {
       item_text.setTextColor(ContextCompat.getColor(requireContext(), R.color.holo_green_dark))
        val stitle = item_text.text.toString()
        for (i in Ltitles.indices) {
            if (Ltitles[i] == stitle) {
                 item_img.setImageResource(Limg[i])
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}