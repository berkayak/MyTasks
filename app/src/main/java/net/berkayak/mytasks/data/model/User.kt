package net.berkayak.mytasks.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "USER")
data class User(
    @PrimaryKey(autoGenerate = true) val ID: Int,
    val name: String,
    val password: String
): Serializable