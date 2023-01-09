package com.khb.todo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.khb.todo.Data.DTO.User

class FragmentActivity : AppCompatActivity() {
    lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment)
        user = intent.getSerializableExtra("user") as User
        supportFragmentManager.beginTransaction()
            .replace(R.id.back_frag, ListFragment())
            .commit()
    }
}