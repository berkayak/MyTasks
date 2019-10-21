package net.berkayak.mytasks.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import net.berkayak.mytasks.data.AppDatabase
import net.berkayak.mytasks.data.model.Todo
import net.berkayak.mytasks.data.model.TodoDAO

class TodoRepository {
    private var todoDao: TodoDAO
    private var todos: LiveData<List<Todo>>

    constructor(application: Application, userID: Int){
        val database = AppDatabase.getInstance(application.applicationContext)
        todoDao = database!!.Todo()
        todos = todoDao.getTodosByUserID(userID)
    }

    fun insert(todo: Todo){
        Thread{
            todoDao.insert(todo)
        }.start()
    }

    fun update(todo: Todo){
        Thread {
            todoDao.update(todo)
        }.start()
    }

    fun delete(todo: Todo){
        Thread {
            todoDao.delete(todo)
        }.start()
    }

    fun getAll(): LiveData<List<Todo>> {
        return todos
    }
}