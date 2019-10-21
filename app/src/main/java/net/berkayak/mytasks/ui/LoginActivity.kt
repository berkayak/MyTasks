package net.berkayak.mytasks.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.room.Room
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*
import net.berkayak.mytasks.R
import net.berkayak.mytasks.data.AppDatabase
import net.berkayak.mytasks.data.model.User
import net.berkayak.mytasks.utilities.Consts
import net.berkayak.mytasks.utilities.Consts.APP_TAG
import net.berkayak.mytasks.viewmodels.UserViewModel

class LoginActivity : AppCompatActivity() {

    lateinit var userVM: UserViewModel
    lateinit var users: LiveData<List<User>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initDB()
        init()

        //Login Button clicked
        loginBtn.setOnClickListener {
            if (!userVM.validateUser(User(0, userNameET.text.toString(), passwordET.text.toString())))
                Snackbar.make(it.rootView, R.string.check_fields, Snackbar.LENGTH_LONG).show()
            else{
                var ob = userVM.getUserByName(userNameET.text.toString())
                startTodoActivity(ob)
                finish()
            }
        }

        //Register click
        registerTV.setOnClickListener {
            startRegisterActivity()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Consts.REQUEST_CODE_REGISTER && resultCode == Consts.RESULT_CODE_DONE){ //data from register activity and register proccess
            val username = data?.getStringExtra(Consts.USERNAME)
            val password = data?.getStringExtra(Consts.PASSWORD)
            val passwordApprove = data?.getStringExtra(Consts.PASSWORD_APPROVE)
            if (userVM.validateRegistration(username!!, password!!, passwordApprove!!))
                userVM.insert(User(0, username, password))
            else
                Snackbar.make(findViewById(android.R.id.content), R.string.check_fields, Snackbar.LENGTH_LONG).show()
        }

    }


    //initialize db for first time
    private fun initDB(){
        Thread {
            AppDatabase.getInstance(this)
        }.start()
    }

    //initialize other object
    private fun init(){
        //SET VIEWMODEL
        userVM = ViewModelProviders.of(this).get(UserViewModel::class.java)
        userVM.getAll().observe(this, Observer<List<User>> {
            Log.i(Consts.APP_TAG, "users observer: ${it.size.toString()}")
        })
    }

    private fun startRegisterActivity(){
        var i = Intent(this, RegisterActivity::class.java)
        startActivityForResult(i, Consts.REQUEST_CODE_REGISTER)
    }

    private fun startTodoActivity(user: User?){
        if (user == null){
            Log.i(APP_TAG, "startTodoActivity: user null")
            return
        }
        var i = Intent(this, TodoActivity::class.java)
        i.putExtra(Consts.KEY_USER, user)
        startActivity(i)
    }
}
