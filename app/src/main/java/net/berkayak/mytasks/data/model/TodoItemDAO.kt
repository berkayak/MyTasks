package net.berkayak.mytasks.data.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TodoItemDAO {
    @Insert
    fun insert(obj: TodoItem)

    @Update
    fun update(obj: TodoItem)

    @Delete
    fun delete(obj: TodoItem)

    @Query("Select * from TODO_ITEM where todoID = :id")
    fun getAllByTodoID(id: Int): LiveData<List<TodoItem>>
}