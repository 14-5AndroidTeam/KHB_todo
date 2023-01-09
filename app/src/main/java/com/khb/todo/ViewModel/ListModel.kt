package com.khb.todo.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.khb.todo.Data.DTO.Member
import com.khb.todo.Data.DTO.ToDo
import com.khb.todo.Data.DTO.User
import com.khb.todo.Data.Repository
import com.khb.todo.Event

class ListModel: androidx.lifecycle.ViewModel() {
    private var member = MutableLiveData<Event<ArrayList<ToDo>>>()
    val setList: LiveData<Event<ArrayList<ToDo>>> = member

    fun getList(user: User) {
        Repository().getMember(user.id, object: Repository.GetDataCallBack<Member> {
            override fun onSuccess(data: Member) = member.postValue(Event(data.todolist))
            override fun onFailure() {}
        })
    }
}