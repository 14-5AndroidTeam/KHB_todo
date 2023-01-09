package com.khb.todo.Data

import com.khb.todo.Data.DTO.*
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {
    @GET("/members/{memberId}")
    fun getMember(
        @Path("memberId") memberId: Int
    ): Call<Member>

    @POST("/members/login")
    fun postLogin(
        @Body login: Login
    ): Call<User>

    @POST("/members/register")
    fun postRegister(
        @Body register: Register
    ): Call<Int>



    @PUT("/todos/{todoId}")
    fun putTodo(
        @Path("todoId") todoId: Int,
        @Body form: Form
    ): Call<Int>

    @DELETE("/todos/{todoId}")
    fun deleteTodo(
        @Path("todoId") todoId: Int
    ): Call<Int>

    @POST("/todos/memberId/{memberId}")
    fun postTodo(
        @Path("memberId") memberId: Int,
        @Body form: Form
    ): Call<Int>
}