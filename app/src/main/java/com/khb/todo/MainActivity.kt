package com.khb.todo

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.text.trimmedLength
import com.khb.todo.Data.DTO.Login
import com.khb.todo.Data.DTO.Register
import com.khb.todo.Data.DTO.User
import com.khb.todo.Data.Repository
import com.khb.todo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var option = false
    //option == false -> register
    //option == true  -> login
    private lateinit var pref: SharedPreferences
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pref = getSharedPreferences("save", MODE_PRIVATE)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            saveAccount.isChecked = pref.getBoolean("save", false)
            if (saveAccount.isChecked) {
                editEmail.setText(pref.getString("email", ""))
                editPassword.setText(pref.getString("password", ""))
            }

            btn1.setOnClickListener {
                editEmail.error = null
                editPassword.error = null
                editName.error = null
                editAge.error = null
                val valueAnimator: ValueAnimator = ValueAnimator.ofFloat(1F, 0F, 1F)
                valueAnimator.duration = 150
                valueAnimator.addUpdateListener { btns.scaleY = it.animatedValue as Float }
                valueAnimator.start()
                Handler(Looper.getMainLooper()).postDelayed({
                    editName.setText("")
                    editAge.setText("")
                    option = !option
                    if (option) applyOption(R.drawable.icon_cancel, R.drawable.icon_signup, View.VISIBLE, "??????", "????????????")
                    else applyOption(R.drawable.icon_signup, R.drawable.icon_signin, View.INVISIBLE, "????????????", "?????????")
                }, valueAnimator.duration / 2)
            }

            btn2.setOnClickListener {
                if (censorship()) return@setOnClickListener

                val repo = Repository()
                if (option) {
                    val register = Register(
                        editName.text.toString().trim(),
                        editAge.text.toString().toInt(),
                        editEmail.text.toString().trim(),
                        editPassword.text.toString().trim(),
                        "USER"
                    )
                    repo.postRegister(register, object: Repository.GetDataCallBack<Int> {
                        override fun onSuccess(data: Int) {
                            makeToast("??????????????? ??????????????????.\n???????????? ????????? ?????????.")
                            btn1.performClick()
                        }
                        override fun onFailure() = with(editEmail) {
                            error = "?????? ???????????? ????????? ?????????."
                            requestFocus()
                            selectAll()
                        }
                    })
                } else {
                    val login = Login(
                        editEmail.text.toString().trim(),
                        editPassword.text.toString().trim()
                    )
                    repo.postLogin(login, object: Repository.GetDataCallBack<User> {
                        override fun onSuccess(data: User) {
                            pref.edit {
                                if (saveAccount.isChecked) {
                                    putBoolean("save", true)
                                    putString("email", editEmail.text.toString().trim())
                                    putString("password", editPassword.text.toString().trim())
                                } else {
                                    putBoolean("save", false)
                                    putString("email", "")
                                    putString("password", "")
                                }
                            }
                            val intent = Intent(applicationContext, FragmentActivity::class.java)
                            intent.putExtra("user", data)
                            startActivity(intent)
                            finish()
                        }
                        override fun onFailure() = makeToast("???????????? ??????????????? ???????????? ????????????.")
                    })
                }
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun applyOption(img1: Int, img2: Int, visibility: Int, txt1: String, txt2: String) = with(binding) {
        btn1Img.setImageDrawable(getDrawable(img1))
        btn2Img.setImageDrawable(getDrawable(img2))
        registerOption.visibility = visibility
        btn1Txt.text = txt1
        btn2Txt.text = txt2
    }

    private fun censorship(): Boolean {
        if (binding.editEmail.text?.isBlank()!!) {
            binding.editEmail.error = "???????????? ??????????????????"
            binding.editEmail.requestFocus()
            return true
        } //????????? ?????? ??????
        if (binding.editPassword.text?.isBlank()!!) {
            binding.editPassword.error = "??????????????? ??????????????????"
            binding.editPassword.requestFocus()
            return true
        } //???????????? ?????? ??????
        if (option && binding.editEmail.text?.contains(" ")!!) {
            binding.editEmail.error = "???????????? ????????? ????????? ??? ????????????"
            return true
        } //??????????????? ????????? ????????? ??????
        if (option && !binding.editEmail.text?.trim()?.matches(Regex("[^@]+@[^@]+\\.[^@]+"))!!) {
            binding.editEmail.error = "???????????? ??????????????????"
            binding.editEmail.requestFocus()
            return true
        } //????????? ????????? ?????? ??????
        if (option && binding.editPassword.text?.contains(" ")!!) {
            binding.editPassword.error = "??????????????? ????????? ????????? ??? ????????????"
            return true
        } //??????????????? ????????? ???????????? ??????
        if (option && binding.editPassword.text?.trimmedLength()!! < 6) {
            binding.editPassword.error = "??????????????? 6??? ?????? ??????????????????"
            binding.editPassword.requestFocus()
            return true
        } //???????????? 6??? ?????? ??????
        if (option && binding.editName.text?.isBlank()!!) {
            binding.editName.error = "????????? ??????????????????"
            binding.editName.requestFocus()
            return true
        } //?????? ?????? ??????
        if (option && binding.editAge.text?.isBlank()!!) {
            binding.editAge.error = "????????? ??????????????????"
            binding.editAge.requestFocus()
            return true
        } //?????? ?????? ??????
        return false
    }

    fun makeToast(message: String) = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
}