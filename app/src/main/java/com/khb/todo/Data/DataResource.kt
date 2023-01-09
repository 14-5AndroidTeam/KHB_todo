package com.khb.todo.Data

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DataResource {
    val baseUrl = "https://jw-todo.inuappcenter.kr"

    var gson = GsonBuilder().setLenient().create()
    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    val service = retrofit.create(RetrofitService::class.java)
}