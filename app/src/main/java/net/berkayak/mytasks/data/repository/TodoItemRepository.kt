package net.berkayak.mytasks.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import net.berkayak.mytasks.data.AppDatabase
import net.berkayak.mytasks.data.model.TodoItem
import net.berkayak.mytasks.data.model.TodoItemDAO

class TodoItemRepository {
    private val todoItemDao: TodoItemDAO
    private val todoItems: LiveData<List<TodoItem>>

    constructor(application: Application, todoID: Int){
        val database = AppDatabase.getInstance(application)
        todoItemDao = database!!.TodoItem()
        todoItems = todoItemDao.getAllByTodoID(todoID)
    }

    fun insert(todoItem: TodoItem){
        Thread{
            todoItemDao.insert(todoItem)
        }.start()
    }

    fun update(todoItem: TodoItem){
        Thread {
            todoItemDao.update(todoItem)
        }.start()
    }

    fun delete(todoItem: TodoItem){
        Thread {
            todoItemDao.delete(todoItem)
        }.start()
    }

    fun getAll(): LiveData<List<TodoItem>>{
        return todoItems
    }
}