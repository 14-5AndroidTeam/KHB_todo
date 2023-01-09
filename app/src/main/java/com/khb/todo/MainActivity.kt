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
                    if (option) applyOption(R.drawable.icon_cancel, R.drawable.icon_signup, View.VISIBLE, "취소", "회원가입")
                    else applyOption(R.drawable.icon_signup, R.drawable.icon_signin, View.INVISIBLE, "회원가입", "로그인")
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
                            makeToast("회원가입에 성공했습니다.\n로그인을 진행해 주세요.")
                            btn1.performClick()
                        }
                        override fun onFailure() = with(editEmail) {
                            error = "이미 사용중인 이메일 입니다."
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
                        override fun onFailure() = makeToast("이메일과 비밀번호가 일치하지 않습니다.")
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
            binding.editEmail.error = "이메일을 입력해주세요"
            binding.editEmail.requestFocus()
            return true
        } //이메일 공백 검열
        if (binding.editPassword.text?.isBlank()!!) {
            binding.editPassword.error = "비밀번호를 입력해주세요"
            binding.editPassword.requestFocus()
            return true
        } //비밀번호 공백 검열
        if (option && binding.editEmail.text?.contains(" ")!!) {
            binding.editEmail.error = "이메일에 공백을 포함할 수 없습니다"
            return true
        } //띄어쓰기가 포함된 이메일 검열
        if (option && !binding.editEmail.text?.trim()?.matches(Regex("[^@]+@[^@]+\\.[^@]+"))!!) {
            binding.editEmail.error = "이메일을 입력해주세요"
            binding.editEmail.requestFocus()
            return true
        } //잘못된 이메일 형태 검열
        if (option && binding.editPassword.text?.contains(" ")!!) {
            binding.editPassword.error = "비밀번호에 공백을 포함할 수 없습니다"
            return true
        } //띄어쓰기가 포함된 비밀번호 검열
        if (option && binding.editPassword.text?.trimmedLength()!! < 6) {
            binding.editPassword.error = "비밀번호를 6자 이상 입력해주세요"
            binding.editPassword.requestFocus()
            return true
        } //비밀번호 6자 미만 검열
        if (option && binding.editName.text?.isBlank()!!) {
            binding.editName.error = "이름을 입력해주세요"
            binding.editName.requestFocus()
            return true
        } //이름 공백 검열
        if (option && binding.editAge.text?.isBlank()!!) {
            binding.editAge.error = "나이를 입력해주세요"
            binding.editAge.requestFocus()
            return true
        } //나이 공백 검열
        return false
    }

    fun makeToast(message: String) = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
}