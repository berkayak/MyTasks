package net.berkayak.mytasks.ui

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_todo_item.*
import net.berkayak.mytasks.R
import net.berkayak.mytasks.data.model.Todo
import net.berkayak.mytasks.data.model.TodoItem
import net.berkayak.mytasks.data.model.User
import net.berkayak.mytasks.ui.adapter.TodoItemRecyclerviewAdapter
import net.berkayak.mytasks.utilities.Consts
import net.berkayak.mytasks.utilities.Consts.KEY_TODO_ID
import net.berkayak.mytasks.utilities.Consts.KEY_TODO_ITEM
import net.berkayak.mytasks.utilities.Consts.REQUEST_CODE_DETAIL
import net.berkayak.mytasks.utilities.Consts.REQUEST_CODE_FILTER
import net.berkayak.mytasks.utilities.Consts.RESULT_CODE_DONE
import net.berkayak.mytasks.utilities.Consts.RESULT_CODE_INSERT
import net.berkayak.mytasks.utilities.Consts.RESULT_CODE_UPDATE
import net.berkayak.mytasks.utilities.RecyclerviewClickListener
import net.berkayak.mytasks.viewmodels.TodoItemViewModel

class TodoItemActivity : AppCompatActivity() {

    lateinit var todoItemVM: TodoItemViewModel
    lateinit var todo: Todo
    lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_item)

        init()
    }

    private fun init() {
        user = intent.getSerializableExtra(Consts.KEY_USER) as User
        todo = intent.getSerializableExtra(Consts.KEY_TODO) as Todo

        //SET RECYCLERVIEW
        todoItemRV.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

        //SET VIEWMODEL
        todoItemVM = TodoItemViewModel(application, todo.ID)
        todoItemVM.getAll().observe(this, Observer {
            if (todoItemRV.adapter != null)
                (todoItemRV.adapter as TodoItemRecyclerviewAdapter).setDataSource(todoItemVM.getByFilter())
            todoItemRV.adapter = TodoItemRecyclerviewAdapter(applicationContext, todoItemVM.getByFilter()!!, rvClickListener)
        })

    }

    private fun startDetailActivity(item: TodoItem?){
        var i = Intent(this, TodoItemDetailActivity::class.java)
        if (item != null)
            i.putExtra(Consts.KEY_TODO_ITEM, item)
        i.putExtra(KEY_TODO_ID, todo.ID)
        startActivityForResult(i, REQUEST_CODE_DETAIL)
    }

    private fun startDetailSearchActivity() {
        var i = Intent(this, DetailSearchActivity::class.java)
        startActivityForResult(i, REQUEST_CODE_FILTER)
    }

    //OPEN SORT DIALOG AND SORTS BY CHOICE
    private fun openSortDialog(){
        var dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle(R.string.choice_criterion)
        dialogBuilder.setCancelable(true)
        dialogBuilder.setSingleChoiceItems(R.array.sort_choice, 0, DialogInterface.OnClickListener{ dialog, which ->
            when(which){
                0 -> { //order by name
                    (todoItemRV.adapter as TodoItemRecyclerviewAdapter).setDataSource(todoItemVM.getByFilter()?.sortedBy { it -> it.name })
                }
                1 -> {
                    (todoItemRV.adapter as TodoItemRecyclerviewAdapter).setDataSource(todoItemVM.getByFilter()?.sortedBy { it -> it.createDate })
                }
                2 -> {
                    (todoItemRV.adapter as TodoItemRecyclerviewAdapter).setDataSource(todoItemVM.getByFilter()?.sortedBy { it -> it.deadLine })
                }
                3 -> {
                    (todoItemRV.adapter as TodoItemRecyclerviewAdapter).setDataSource(todoItemVM.getByFilter()?.sortedBy { it -> it.completed })
                }
            }
            dialog.dismiss()
        })
        dialogBuilder.show()
    }

    private fun sendEmail(){
        var emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.setData(Uri.parse("mailto:"))
        emailIntent.setType("text/plain")
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "TODO LIST")
        emailIntent.putExtra(Intent.EXTRA_TEXT, todoItemVM.getJsonList().toString())
        emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // this will make such that when user returns to your app, your app is displayed, instead of the email app.
        startActivity(emailIntent)
    }

    private var rvClickListener = object : RecyclerviewClickListener {
        override fun onClick(viewId: Int, itemId: Int, pos: Int) {
            when(viewId){
                R.layout.item_todo_item -> {
                    todoItemVM.changeCompleted(todoItemVM.getByID(itemId))
                }
                R.id.todoItemEditIB -> {
                    startDetailActivity(todoItemVM.getByID(itemId))
                }
                R.id.todoItemDeleteIB -> {
                    todoItemVM.delete(todoItemVM.getByID(itemId)!!)
                }
            }
        }
    }

    //SEARCHVIEW LISTENER
    private var searchListener = object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            //DO NOT ANY CHANGE ON TEXTCHANGE
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            var bnd = Bundle()
            bnd.putString(Consts.FILTER_NAME, newText?.toLowerCase())
            if (todoItemVM.setFilter(bnd) != null)
                (todoItemRV.adapter as TodoItemRecyclerviewAdapter).setDataSource(todoItemVM.getByFilter()!!)
            return false
        }
    }

    //CREATE MENU
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.todo_item_activity_menu, menu)

        var searchItem = menu!!.findItem(R.id.searchItemBTN)
        var searchView = searchItem.actionView as androidx.appcompat.widget.SearchView?
        searchView?.setOnQueryTextListener(searchListener)

        return true
    }

    //MENU ITEMS CLICK EVENT
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.newItemBTN -> {
                startDetailActivity(null)
            }
            R.id.detailSearchBTN -> {
                startDetailSearchActivity()
            }
            R.id.itemSortBTN -> {
                openSortDialog()
            }
            R.id.sendEmailBTN -> {
                sendEmail()
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //INSERT OR UPDATE OPERATIONS
        if (requestCode == REQUEST_CODE_DETAIL && (resultCode == RESULT_CODE_UPDATE || resultCode == RESULT_CODE_INSERT)){
            var todoItem = data?.getSerializableExtra(KEY_TODO_ITEM) as TodoItem?
            if (todoItem == null) return
            if (resultCode == RESULT_CODE_INSERT)
                todoItemVM.insert(todoItem)
            else if (resultCode == RESULT_CODE_UPDATE)
                todoItemVM.update(todoItem)
        }
        //DETAIL FILTER OPERATIONS
        if (requestCode == REQUEST_CODE_FILTER && resultCode == RESULT_CODE_DONE){
            (todoItemRV.adapter as TodoItemRecyclerviewAdapter).setDataSource(todoItemVM.setFilter(data!!.getBundleExtra(Consts.FILTER_BUNDLE)!!))
        }
    }
}
