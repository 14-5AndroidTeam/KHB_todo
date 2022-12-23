package com.khb.todo

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.text.trimmedLength

class MainActivity : AppCompatActivity() {

    private lateinit var edit_email:AppCompatEditText
    private lateinit var edit_password:AppCompatEditText
    private lateinit var edit_name:AppCompatEditText
    private lateinit var edit_age:AppCompatEditText

    private lateinit var btn1:LinearLayout
    private lateinit var btn1_img:ImageView
    private lateinit var btn1_txt:TextView

    private lateinit var btn2:LinearLayout
    private lateinit var btn2_img:ImageView
    private lateinit var btn2_txt:TextView

    private lateinit var register_option:LinearLayout
    private lateinit var btns:LinearLayout

    private var status = true

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edit_email = findViewById(R.id.edit_email)
        edit_password = findViewById(R.id.edit_password)
        edit_name = findViewById(R.id.edit_name)
        edit_age = findViewById(R.id.edit_age)

        btn1 = findViewById(R.id.btn1)
        btn1_img = findViewById(R.id.btn1_img)
        btn1_txt = findViewById(R.id.btn1_txt)

        btn2 = findViewById(R.id.btn2)
        btn2_img = findViewById(R.id.btn2_img)
        btn2_txt = findViewById(R.id.btn2_txt)

        register_option = findViewById(R.id.register_option)
        btns = findViewById(R.id.btns)

        btn1.setOnClickListener {
            val valueAnimator: ValueAnimator = ValueAnimator.ofFloat(1F, 0F, 1F)
            valueAnimator.duration = 150
            valueAnimator.addUpdateListener { btns.scaleY = it.animatedValue as Float }
            valueAnimator.start()
            Handler(Looper.getMainLooper()).postDelayed({
                edit_name.setText("")
                edit_age.setText("")
                if (status) {
                    status = false
                    btn1_img.setImageDrawable(getDrawable(R.drawable.icon_cancel))
                    btn2_img.setImageDrawable(getDrawable(R.drawable.icon_signup))
                    register_option.visibility = View.VISIBLE
                    btn1_txt.text = "취소"
                    btn2_txt.text = "회원가입"
                } else {
                    status = true
                    btn1_img.setImageDrawable(getDrawable(R.drawable.icon_signup))
                    btn2_img.setImageDrawable(getDrawable(R.drawable.icon_signin))
                    register_option.visibility = View.INVISIBLE
                    btn1_txt.text = "회원가입"
                    btn2_txt.text = "로그인"
                }
            }, 75)
        }

        btn2.setOnClickListener {
            if(edit_email.text?.trimmedLength() == 0) {
                edit_email.error = "이메일을 입력해주세요"
            }
        }
    }
}