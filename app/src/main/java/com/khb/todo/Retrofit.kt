package com.khb.todo

import com.khb.todo.Data.DTO.ToDo


data class ReqGetMember(
    var id: Int,
    var name: String,
    var age: Int,
    var email: String,
    var todolist: List<ToDo>
)

data class ReqPutMember(
    var name: String,
    var age: Int
)

data class ReqLogin(
    var email: String,
    var password: String
)

data class ReqRegister(
    var name: String,
    var age: Int,
    var email: String,
    var password: String,
    var role: String
)