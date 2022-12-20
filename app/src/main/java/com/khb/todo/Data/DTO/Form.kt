package com.khb.todo.Data.DTO

data class Member(
    var id: Int,
    var name: String,
    var age: Int,
    var email: String,
    var todolist: List<ToDo>
)

data class ToDo(
    var contents: String,
    var createdAt: String,
    var id: Int,
    var isCompleted: Boolean,
    var updatedAt: String
)

data class User(
    var id: Int,
    var token: String
)