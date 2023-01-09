package com.khb.todo

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.Toast
import com.khb.todo.Data.DTO.Form
import com.khb.todo.Data.DTO.ToDo
import com.khb.todo.Data.Repository
import com.khb.todo.databinding.DialogFormBinding
import com.khb.todo.databinding.ListFormBinding

class ListviewAdapter(private val context: Context) : BaseAdapter() {
    private var selected = -1
    private var selected_old = -1
    private var byUser = false
    private var width = intArrayOf(0, 0)

    private var todoList = arrayListOf<ToDo>()

    fun setData(todoList: ArrayList<ToDo>) { this.todoList = todoList }

    override fun getCount(): Int { return todoList.size }

    override fun getItem(p0: Int): ToDo { return todoList[p0] }

    override fun getItemId(p0: Int): Long { return p0.toLong() }

    override fun registerDataSetObserver(observer: DataSetObserver?) { super.registerDataSetObserver(observer) }

    @SuppressLint("InflateParams", "ViewHolder")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val binding = ListFormBinding.inflate(LayoutInflater.from(context))
        val toDo = todoList[p0]
        with(binding) {
            if (width[0] == 0) btn.post { width = intArrayOf(backTxt.width, backTxt.width - btn.width) }

            txtContent.text = toDo.contents
            txtUpdatedAt.text = toDo.updatedAt.replace("T", " ").split(".")[0]
            if (toDo.isCompleted) backBtn.setBackgroundResource(R.drawable.list_back_green)
            else backBtn.setBackgroundResource(R.drawable.list_back_red)

            if (selected == p0 || selected_old == p0) if (byUser) {
                val valueAnimator = ValueAnimator.ofInt(width[if (selected_old == p0) 1 else 0], width[if (selected == p0) 1 else 0]).setDuration(100)
                valueAnimator.addUpdateListener { backTxt.layoutParams = LinearLayout.LayoutParams(it.animatedValue as Int, LinearLayout.LayoutParams.MATCH_PARENT) }
                valueAnimator.start()
            } else if (selected == p0) backTxt.layoutParams = LinearLayout.LayoutParams(width[1], LinearLayout.LayoutParams.MATCH_PARENT)

            backTxt.setOnClickListener {
                selected_old = selected
                selected = if (selected == p0) -1 else p0
                byUser = true
                notifyDataSetChanged()
                btn.post { byUser = false }
            }

            btn.setOnClickListener {
                val dialog = AlertDialog.Builder(context)
                val binding = DialogFormBinding.inflate(LayoutInflater.from(context))
                binding.contents.setText(txtContent.text.toString())

                dialog.setTitle("Todo Edit")
                dialog.setView(binding.root)
                dialog.setPositiveButton("수정하기") { _, _ ->
                    if(binding.contents.text.isBlank()) {
                        Toast.makeText(context, "빈칸으로 저장할 수 없어요", Toast.LENGTH_SHORT).show()
                        backTxt.performClick()
                        return@setPositiveButton
                    }
                    toDo.contents = binding.contents.text.toString()
                    Repository().putTodo(toDo.id, Form(toDo.contents, toDo.isCompleted))
                    backTxt.performClick()
                }
                dialog.setNegativeButton(if(toDo.isCompleted) "취소하기" else "완료하기") { _, _ ->
                    toDo.isCompleted = !toDo.isCompleted
                    Repository().putTodo(toDo.id, Form(toDo.contents, toDo.isCompleted))
                    backTxt.performClick()
                }
                dialog.setNeutralButton("삭제하기") { _, _ ->
                    todoList.removeAt(p0)
                    Repository().deleteTodo(toDo.id)
                    notifyDataSetChanged()
                }
                dialog.create().show()
            }
        }
        return binding.root
    }
}