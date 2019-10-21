package net.berkayak.mytasks.data.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDAO {
    @Insert
    fun insert(obj: User)

    @Update
    fun update(obj: User)

    @Delete
    fun delete(obj: User)

    @Query("select * from USER ORDER BY ID DESC")
    fun getAllUser() : LiveData<List<User>>
}