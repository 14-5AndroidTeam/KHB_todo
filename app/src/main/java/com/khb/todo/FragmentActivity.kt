package com.khb.todo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationBarView
import com.khb.todo.Data.DTO.User
import com.khb.todo.databinding.ActivityFragmentBinding

class FragmentActivity : AppCompatActivity() {
    private lateinit var binding:ActivityFragmentBinding
    lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = intent.getSerializableExtra("user") as User
        supportFragmentManager.beginTransaction()
            .replace(R.id.back_frag, ListFragment())
            .commit()

        binding.bottomNavigationView.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            when (it.itemId) {
                R.id.menu_list -> fragmentTransaction.replace(R.id.back_frag, ListFragment()).commit()
                R.id.menu_profile -> fragmentTransaction.replace(R.id.back_frag, ProfileFragment()).commit()
            }
            return@OnItemSelectedListener true
        })
    }
}