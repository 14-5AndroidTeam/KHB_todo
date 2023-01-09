package com.khb.todo.Data.DTO

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Member(
    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("age")
    var age: Int,
    @SerializedName("email")
    var email: String,
    @SerializedName("todolist")
    var todolist: ArrayList<ToDo>
)

data class ToDo(
    @SerializedName("contents")
    var contents: String,
    @SerializedName("createdAt")
    var createdAt: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("isCompleted")
    var isCompleted: Boolean,
    @SerializedName("updatedAt")
    var updatedAt: String
)

data class User(
    @SerializedName("id")
    var id: Int,
    @SerializedName("token")
    var token: String
): Serializable

data class Login(
    var email: String,
    var password: String
)

data class Register(
    var name: String,
    var age: Int,
    var email: String,
    var password: String,
    var role: String
)

data class Form(
    var contents: String,
    var isCompleted: Boolean
)