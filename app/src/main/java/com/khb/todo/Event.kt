package com.khb.todo

class Event<out T>(private val content: T) {
    fun peekContent(): T = content
}