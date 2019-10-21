package net.berkayak.mytasks.data.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TodoDAO {
    @Insert
    fun insert(obj: Todo)

    @Update
    fun update(obj: Todo)

    @Delete
    fun delete(obj: Todo)

    @Query("select * from TODO where userID = :id")
    fun getTodosByUserID(id: Int): LiveData<List<Todo>>
}