package net.berkayak.mytasks.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_todo.*
import net.berkayak.mytasks.R
import net.berkayak.mytasks.data.model.Todo
import net.berkayak.mytasks.data.model.User
import net.berkayak.mytasks.ui.adapter.TodoRecyclerviewAdapter
import net.berkayak.mytasks.utilities.Consts
import net.berkayak.mytasks.utilities.RecyclerviewClickListener
import net.berkayak.mytasks.utilities.TodoHelper
import net.berkayak.mytasks.viewmodels.TodoViewModel

class TodoActivity : AppCompatActivity() {
    lateinit var user: User
    var todo: Todo? = null
    lateinit var todoVM: TodoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)
        init()
    }

    fun init(){
        user = intent.getSerializableExtra(Consts.KEY_USER) as User

        //set the recyclerview
        todoRV.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        //SET VIEWMODEL
        todoVM = TodoViewModel(application, user.ID)
        todoVM.getAll().observe(this, Observer {
            todoRV.adapter = TodoRecyclerviewAdapter(applicationContext, it, rvClickListener)
        })
        saveIB.setOnClickListener(saveClicklistener)
    }

    //STARTS TODOITEMACTIVITY
    fun startTodoItemActivity(pos: Int){
        var i = Intent(this, TodoItemActivity::class.java)
        i.putExtra(Consts.KEY_TODO, todoVM.getAll().value!![pos])
        i.putExtra(Consts.KEY_USER, user)
        startActivity(i)
    }

    fun clearFields(){
        todoItemNameET.setText("")
    }

    //RECYCLERVIEW CLICK OPERATIONS
    var rvClickListener = object : RecyclerviewClickListener {
        override fun onClick(viewId: Int, itemId: Int, pos: Int) {
            when(viewId){
                R.id.todoNameTV -> { //redirect to todoItems activity
                    startTodoItemActivity(pos)
                }
                R.id.todoEditIB -> { //fill the todoNameET
                    todo = todoVM.getAll().value?.get(pos)!!
                    if (todo != null) todoItemNameET.setText(todo!!.name)
                }
                R.id.todoDeleteIB -> { //Delete action
                    todoVM.delete(todoVM.getAll().value!![pos])
                }
            }
        }
    }

    //SAVE OPERATION
    var saveClicklistener =  View.OnClickListener {
        var newName = todoItemNameET.text.toString()

        //VALIDATION
        if (newName.isNullOrEmpty() || !todoVM.checkName(newName)){
            Snackbar.make(findViewById(android.R.id.content), R.string.empty_fields, Snackbar.LENGTH_LONG).show()
            return@OnClickListener
        }

        if(todo == null) { //INSERT PROCESS
            todoVM.insert(Todo(0, newName, TodoHelper.getDateTimeAsLong(null), user.ID))
        } else { //UPDATE PROCESS
            todo!!.name = newName
            todoVM.update(todo!!)
        }
        clearFields()
    }
}
