package com.example.whichbin.ui.setting

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.whichbin.R
import com.example.whichbin.databinding.FragmentSettingBinding
import com.example.whichbin.http.*
import com.example.whichbin.userData
import kotlinx.android.synthetic.main.fragment_setting.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Math.ceil

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingBinding.inflate(inflater, container, false);
        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentplace.text=userData.currentPlace
        textView.text=userData.userName
        setting_credit.text=userData.credits.toString()

        placeBtn.setOnClickListener {
            val intent = Intent(requireContext(), PlaceActivity::class.java)
            startActivity(intent)
            currentplace.text=userData.currentPlace
        }
        setting_exit.setOnClickListener{
            requireActivity().sendBroadcast(
                Intent("com.example.experiment3.FORCE_OFFLINE").apply {
                    `package` = requireActivity().packageName
                }
            )
        }
        creditBtn.setOnClickListener {
            if (userData.userName == "游客") {
                Toast.makeText(context, "登录才有积分哦", Toast.LENGTH_SHORT).show()
            } else {
                ServiceCreater.creat(ClassicService::class.java, requireContext())
                    ?.getRanking()
                    ?.enqueue(object : Callback<RankingResult> {
                        override fun onResponse(
                            call: Call<RankingResult>,
                            response: Response<RankingResult>
                        ) {
                            response.body()?.let { result  ->
                                showRankingPopup(result.ranking)
                            }
                        }

                        override fun onFailure(call: Call<RankingResult>, t: Throwable) {
                            Toast.makeText(context, "获取排名失败", Toast.LENGTH_SHORT).show()
                        }
                    })
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == PLACE_REQUEST_CODE) {
            binding.currentplace.text = userData.currentPlace
        }
    }

    private fun showRankingPopup(ranking: List<RankingItem>) {
        val popupView = LayoutInflater.from(context).inflate(R.layout.popu_ranking, null)
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
            true
        ).apply {
            animationStyle = R.style.anim_menu_bottombar
            setBackgroundDrawable(ColorDrawable(34))
            isFocusable = true
        }

        // Set ranking data
        with(popupView) {
            findViewById<TextView>(R.id.username1).text = ranking[0].username
            findViewById<TextView>(R.id.username2).text = ranking[1].username
            findViewById<TextView>(R.id.username3).text = ranking[2].username
            findViewById<TextView>(R.id.username4).text = ranking[3].username
            findViewById<TextView>(R.id.username5).text = ranking[4].username

            findViewById<TextView>(R.id.credit1).text = ranking[0].credits.toString()
            findViewById<TextView>(R.id.credit2).text = ranking[1].credits.toString()
            findViewById<TextView>(R.id.credit3).text = ranking[2].credits.toString()
            findViewById<TextView>(R.id.credit4).text = ranking[3].credits.toString()
            findViewById<TextView>(R.id.credit5).text = ranking[4].credits.toString()
        }

        popupView.findViewById<View>(R.id.rank_back).setOnClickListener {
            popupWindow.dismiss()
        }

        popupWindow.showAtLocation(binding.root, Gravity.TOP, 0, getStatusBarHeight(requireContext()).toInt())
    }

    private fun getStatusBarHeight(context: Context): Double {
        return ceil(25.0 * context.resources.displayMetrics.density)
    }

    companion object {
        private const val PLACE_REQUEST_CODE = 1
        private const val RESULT_OK = -1
    }
}