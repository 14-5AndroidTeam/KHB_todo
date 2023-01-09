package com.khb.todo.Data

import android.widget.Toast
import com.khb.todo.Data.DTO.*
import com.khb.todo.FragmentActivity
import com.khb.todo.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository {
    private val dataResource = DataResource

    fun getMember(memberId: Int, param: GetDataCallBack<Member>) {
        val call = dataResource.service.getMember(memberId)
        call.enqueue(object: Callback<Member> {
            override fun onResponse(call: Call<Member>, response: Response<Member>) {
                response.body()?.let { param.onSuccess(it) }
            }

            override fun onFailure(call: Call<Member>, t: Throwable) {}
        })
    }

    fun postLogin(login: Login, param: GetDataCallBack<User>) {
        val call = dataResource.service.postLogin(login)
        call.enqueue(object: Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                val body = response.body()
                if(body == null) param.onFailure()
                else param.onSuccess(body)
            }

            override fun onFailure(call: Call<User>, t: Throwable) {}
        })
    }

    fun postRegister(register: Register, param: GetDataCallBack<Int>) {
        val call = dataResource.service.postRegister(register)
        call.enqueue(object: Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                val body = response.body()
                if(body == null) param.onFailure()
                else param.onSuccess(body)
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {}
        })
    }

    fun putTodo(todoId: Int, form: Form) {
        val call = dataResource.service.putTodo(todoId, form)
        call.enqueue(object: Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {}
            override fun onFailure(call: Call<Int>, t: Throwable) {}
        })
    }

    fun deleteTodo(todoId: Int) {
        val call = dataResource.service.deleteTodo(todoId)
        call.enqueue(object: Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {}
            override fun onFailure(call: Call<Int>, t: Throwable) {}
        })
    }

    fun postTodo(memberId: Int, form: Form, param: GetDataCallBack<Int>) {
        val call = dataResource.service.postTodo(memberId, form)
        call.enqueue(object: Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                val body = response.body()
                if(body == null) param.onFailure()
                else param.onSuccess(body)
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {}
        })
    }

    interface GetDataCallBack<T> {
        fun onSuccess(data: T)
        fun onFailure()
    }
}