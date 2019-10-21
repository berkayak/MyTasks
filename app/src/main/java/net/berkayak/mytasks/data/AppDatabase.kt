package net.berkayak.mytasks.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import net.berkayak.mytasks.data.model.*

@Database(entities = [User::class, Todo::class, TodoItem::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){

    abstract fun User(): UserDAO

    abstract fun Todo(): TodoDAO

    abstract fun TodoItem(): TodoItemDAO

    //singleton pattern for access single instance of db
    companion object{
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (instance == null){
                synchronized(AppDatabase::class){
                    instance = Room.databaseBuilder(context, AppDatabase::class.java, "AppDatabase").build()
                }
            }
            return instance
        }
    }

}