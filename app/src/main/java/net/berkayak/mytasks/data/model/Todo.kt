package net.berkayak.mytasks.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "TODO")
data class Todo(
    @PrimaryKey(autoGenerate = true) val ID: Int,
    var name: String,
    var createDate: Long,
    val userID: Int
): Serializable