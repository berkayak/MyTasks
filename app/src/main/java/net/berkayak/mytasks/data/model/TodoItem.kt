package net.berkayak.mytasks.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "TODO_ITEM")
data class TodoItem(
    @PrimaryKey(autoGenerate = true) val ID: Int,
    var name: String,
    var description: String,
    var deadLine: Long,
    var createDate: Long,
    var completed: Boolean,
    val todoID: Int
): Serializable