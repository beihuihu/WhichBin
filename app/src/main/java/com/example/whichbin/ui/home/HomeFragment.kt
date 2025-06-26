package com.example.whichbin.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupWindow
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.whichbin.classification.PhotoActivity
import com.example.whichbin.R
import com.example.whichbin.classification.RealTimeActivity
import com.example.whichbin.database.DatabaseUtil
import com.example.whichbin.databinding.FragmentHomeBinding
import com.example.whichbin.database.search_Adapter
import com.example.whichbin.userData

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var Ltitles: Array<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false);
        val root: View = binding.root
        return root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = requireContext()
        Ltitles = userData.Ltitles

        view.findViewById<TextView>(R.id.middle1).text = Ltitles[0]
        view.findViewById<TextView>(R.id.middle2).text = Ltitles[1]
        view.findViewById<TextView>(R.id.middle3).text = Ltitles[2]
        view.findViewById<TextView>(R.id.middle4).text = Ltitles[3]

        view.findViewById<EditTextWithDel>(R.id.target).addTextChangedListener(
            HomeFragmentTextWatcher(context, this)
        )

        view.findViewById<ImageButton>(R.id.detectBtn).setOnClickListener {
            showPopupMenu(context)
        }

        view.findViewById<ImageButton>(R.id.middle_Btn1).setOnClickListener {
            showPopupWindow(Ltitles[0])
        }

        view.findViewById<ImageButton>(R.id.middle_Btn2).setOnClickListener {
            showPopupWindow(Ltitles[1])
        }

        view.findViewById<ImageButton>(R.id.middle_Btn3).setOnClickListener {
            showPopupWindow(Ltitles[2])
        }

        view.findViewById<ImageButton>(R.id.middle_Btn4).setOnClickListener {
            showPopupWindow(Ltitles[3])
        }


    }

    private fun showPopupMenu(context: Context) {
        val popupView = LayoutInflater.from(context).inflate(R.layout.popu_layout, null)
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
            true
        ).apply {
//            backgroundDrawable = ColorDrawable(0x22000000)
            animationStyle = R.style.anim_menu_bottombar
            isTouchable = true
        }

        popupView.findViewById<Button>(R.id.pop_realtime).setOnClickListener {
            popupWindow.dismiss()
            startActivity(Intent(context, RealTimeActivity::class.java))
        }

        popupView.findViewById<Button>(R.id.pop_cancel).setOnClickListener {
            popupWindow.dismiss()
        }

        popupView.findViewById<Button>(R.id.pop_photo).setOnClickListener {
            popupWindow.dismiss()
            startActivity(Intent(context, PhotoActivity::class.java))
        }

        popupWindow.showAtLocation(
            LayoutInflater.from(context).inflate(R.layout.fragment_home, null),
            android.view.Gravity.BOTTOM,
            0,
            0
        )
    }

    private fun showPopupWindow(className: String) {
        val popupView = LayoutInflater.from(requireContext()).inflate(R.layout.popu_garbage, null)
        val recyclerView = popupView.findViewById<RecyclerView>(R.id.pop_recy).apply {
            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            adapter = search_Adapter(
                DatabaseUtil.foundGarbage(requireContext(), className)
            )
        }

        PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
            true
        ).apply {
            contentView = popupView
            animationStyle = R.style.anim_menu_bottombar
//            backgroundDrawable = ColorDrawable(0x22000000)
            isFocusable = true

            popupView.findViewById<TextView>(R.id.pop_className).text = className
            popupView.findViewById<View>(R.id.popu_back).setOnClickListener {
                dismiss()
            }

            showAtLocation(
                LayoutInflater.from(context).inflate(R.layout.fragment_home, null),
                android.view.Gravity.BOTTOM,
                0,
                0
            )
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// Separate class for text watcher
class HomeFragmentTextWatcher(
    private val context: Context,
    private val fragment: HomeFragment
) : TextWatcher {
    // Implement text watcher methods
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(s: Editable?) {}
}