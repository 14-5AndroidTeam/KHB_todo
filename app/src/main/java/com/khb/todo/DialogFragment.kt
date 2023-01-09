package com.khb.todo

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.khb.todo.Data.DTO.Form
import com.khb.todo.Data.DTO.User
import com.khb.todo.Data.Repository
import com.khb.todo.databinding.FragmentDialogBinding

class DialogFragment : Fragment() {
    private lateinit var binding: FragmentDialogBinding
    private lateinit var handler: Handler
    private lateinit var user: User

    override fun onAttach(context: Context) {
        super.onAttach(context)
        user = (context as FragmentActivity).user
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        animation()
        with(binding) {
            btnAdd.setOnClickListener {
                if (contents.text.isBlank()) {
                    makeToast("빈칸으로 저장할 수 없어요")
                    return@setOnClickListener
                }
                val form = Form(contents.text.toString(), false)
                Repository().postTodo(
                    user.id, form, object : Repository.GetDataCallBack<Int> {
                        override fun onSuccess(data: Int) {
                            makeToast("To do - \""+contents.text+"\"가 추가되었어요.")
                            btnCancel.performClick()
                        }

                        override fun onFailure() = makeToast("인터넷 연결 상태를 확인해 주세요.")
                    })
            }
            btnCancel.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.back_frag, ListFragment())
                    .commit()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
    }

    @SuppressLint("Recycle")
    private fun animation() {
        handler = Handler(Looper.getMainLooper())
        with(binding.contents) {
            val valueAnimator1 = ValueAnimator.ofFloat(0F, 1F).setDuration(500)
            valueAnimator1.addUpdateListener { this.alpha = it.animatedValue as Float }
            valueAnimator1.start()
            val valueAnimator2 = ValueAnimator.ofFloat(100F, 0F).setDuration(500)
            valueAnimator2.addUpdateListener { this.translationY = it.animatedValue as Float }
            valueAnimator2.start()
            handler.postDelayed({ hint = "ㅁ" }, 25)
            handler.postDelayed({ hint = "무" }, 50)
            handler.postDelayed({ hint = "무ㅇ" }, 75)
            handler.postDelayed({ hint = "무어" }, 100)
            handler.postDelayed({ hint = "무엇" }, 125)
            handler.postDelayed({ hint = "무엇ㅇ" }, 150)
            handler.postDelayed({ hint = "무엇으" }, 175)
            handler.postDelayed({ hint = "무엇을" }, 200)
            handler.postDelayed({ hint = "무엇을 " }, 225)
            handler.postDelayed({ hint = "무엇을 ㅎ" }, 250)
            handler.postDelayed({ hint = "무엇을 해" }, 275)
            handler.postDelayed({ hint = "무엇을 해ㅇ" }, 300)
            handler.postDelayed({ hint = "무엇을 해야" }, 325)
            handler.postDelayed({ hint = "무엇을 해야ㅎ" }, 350)
            handler.postDelayed({ hint = "무엇을 해야하" }, 375)
            handler.postDelayed({ hint = "무엇을 해야하ㄴ" }, 400)
            handler.postDelayed({ hint = "무엇을 해야하나" }, 425)
            handler.postDelayed({ hint = "무엇을 해야하나ㅇ" }, 450)
            handler.postDelayed({ hint = "무엇을 해야하나요" }, 475)
            handler.postDelayed({ hint = "무엇을 해야하나요?" }, 500)
        }
    }

    fun makeToast(message: String) = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}