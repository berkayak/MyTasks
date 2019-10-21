package net.berkayak.mytasks.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import net.berkayak.mytasks.data.model.Todo
import net.berkayak.mytasks.data.repository.TodoRepository
import net.berkayak.mytasks.utilities.TodoHelper

class TodoViewModel(application: Application, userID: Int): AndroidViewModel(application) {
    private val repo = TodoRepository(application, userID)
    private val todos = repo.getAll()

    fun insert(todo: Todo){
        repo.insert(todo)
    }

    fun update(todo: Todo){
        todo.createDate = TodoHelper.getDateTimeAsLong(null)
        repo.update(todo)
    }

    fun delete(todo: Todo){
        repo.delete(todo)
    }

    fun getAll(): LiveData<List<Todo>> {
        return todos
    }

    //return false if there is same name record
    fun checkName(name: String): Boolean{
        return todos.value?.find { it -> it.name == name } == null
    }
}