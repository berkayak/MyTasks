package net.berkayak.mytasks.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import net.berkayak.mytasks.data.model.User
import net.berkayak.mytasks.data.repository.UserRepository

class UserViewModel(application: Application): AndroidViewModel(application){
    private val repo = UserRepository(application)
    private val users = repo.getAll()

    fun insert(user: User) {
        repo.insert(user)
    }

    fun update(user: User){
        repo.update(user)
    }

    fun delete(user: User){
        repo.delete(user)
    }

    fun getAll(): LiveData<List<User>>{
        return users
    }

    fun validateUser(user: User): Boolean{
        var temp = users.value?.find { it -> it.name == user.name && it.password == user.password }
        return (temp != null)
    }

    fun validateRegistration(username: String, pass: String, passApprove: String): Boolean {
        return ((users.value?.find { it -> it.name.toLowerCase() == username.toLowerCase() } == null)) && (pass == passApprove)
    }

    fun getUserByID(id: Int): User?{
        return users.value?.find { it -> it.ID == id }
    }

    fun getUserByName(name: String): User?{
        return users.value?.find { it -> it.name == name }
    }
}