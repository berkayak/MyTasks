package net.berkayak.mytasks.viewmodels

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import net.berkayak.mytasks.data.model.TodoItem
import net.berkayak.mytasks.data.repository.TodoItemRepository
import net.berkayak.mytasks.utilities.Consts
import net.berkayak.mytasks.utilities.TodoHelper
import org.json.JSONArray
import org.json.JSONObject

class TodoItemViewModel(application: Application, todoID: Int): AndroidViewModel(application) {
    private val repo = TodoItemRepository(application, todoID)
    private val todoItems = repo.getAll()
    private var filterBundle = Bundle()

    fun insert(todoItem: TodoItem){
        repo.insert(todoItem)
    }

    fun update(todoItem: TodoItem){
        repo.update(todoItem)
    }

    fun delete(todoItem: TodoItem){
        repo.delete(todoItem)
    }

    fun getAll(): LiveData<List<TodoItem>>{
        return todoItems
    }

    fun getByID(id: Int): TodoItem? {
        return todoItems.value?.find { it.ID == id }
    }

    fun changeCompleted(todoItem: TodoItem?){
        if (todoItem != null){
            todoItem.completed = todoItem.completed.not()
            update(todoItem)
        }
    }

    fun setFilter(bnd: Bundle): List<TodoItem>?{
        filterBundle = bnd
        return getByFilter()
    }

    fun getByFilter(): List<TodoItem>?{
        var filteredList = todoItems.value?.toMutableList()
        if (filterBundle.getBoolean(Consts.FILTER_DONE, false))
            filteredList = filteredList?.filter { it.completed }?.toMutableList()
        if (filterBundle.getBoolean(Consts.FILTER_UNDONE, false))
            filteredList = filteredList?.filter { !it.completed }?.toMutableList()
        if (filterBundle.getBoolean(Consts.FILTER_EXPIRED, false))
            filteredList = filteredList?.filter { it.deadLine < TodoHelper.getDateTimeAsLong(null) }?.toMutableList()
        if (filterBundle.getBoolean(Consts.FILTER_NOT_EXPIRED, false))
            filteredList = filteredList?.filter { it.deadLine >= TodoHelper.getDateTimeAsLong(null) }?.toMutableList()
        if (!filterBundle.getString(Consts.FILTER_NAME, "").isNullOrEmpty()){
            var constraint = filterBundle.getString(Consts.FILTER_NAME, "")
            filteredList = filteredList?.filter { it.name.toLowerCase().contains(constraint) }?.toMutableList()
        }
        return filteredList?.toList()
    }

    fun getJsonList(): JSONArray{
        var li = todoItems.value
        var jList = JSONArray()
        li?.forEach {
            var obj = JSONObject()
            obj.put("name", it.name)
            obj.put("description", it.description)
            obj.put("createDate", TodoHelper.getDateTime(it.createDate))
            obj.put("expireDate", TodoHelper.getDateTime(it.deadLine))
            obj.put("completed", it.completed)
            jList.put(obj)
        }
        return jList
    }

}