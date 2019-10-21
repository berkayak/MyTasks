package net.berkayak.mytasks.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.berkayak.mytasks.data.AppDatabase
import net.berkayak.mytasks.data.model.User
import net.berkayak.mytasks.data.model.UserDAO

class UserRepository{
    private var userDao: UserDAO
    private var users: LiveData<List<User>>

    constructor(application: Application){
        val db = AppDatabase.getInstance(application.applicationContext)
        userDao = db!!.User()
        users = db!!.User().getAllUser()
    }

    fun insert(user: User) {
        Thread {
            userDao.insert(user)
        }.start()
    }

    fun update(user: User){
        Thread{
            userDao.update(user)
        }.start()
    }

    fun delete(user: User){
        Thread {
            userDao.delete(user)
        }.start()
    }

    fun getAll(): LiveData<List<User>> {
        return users
    }


}