package net.berkayak.mytasks.ui

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_todo_item_detail.*
import net.berkayak.mytasks.R
import net.berkayak.mytasks.data.model.TodoItem
import net.berkayak.mytasks.utilities.Consts
import net.berkayak.mytasks.utilities.Consts.KEY_TODO_ITEM
import net.berkayak.mytasks.utilities.Consts.RESULT_CODE_INSERT
import net.berkayak.mytasks.utilities.Consts.RESULT_CODE_UPDATE
import net.berkayak.mytasks.utilities.TodoHelper
import java.util.*

class TodoItemDetailActivity : AppCompatActivity() {
    var todoItem: TodoItem? = null
    lateinit var datePickerDialog: DatePickerDialog
    var isDateSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_item_detail)

        init()
    }

    private fun init() {
        todoItem = intent.extras?.getSerializable(KEY_TODO_ITEM) as TodoItem?
        if (todoItem != null) {
            todoItemNameET.setText(todoItem!!.name)
            todoItemDescriptionET.setText(todoItem!!.description)
            todoItemDeadlineTV.text = TodoHelper.getDateTime(todoItem!!.deadLine)
            isDateSelected = true
        }

        //ASSING DATEPICKERDIALOG
        datePickerDialog = DatePickerDialog(this,
            onDateSetListener,
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH))

        selectDateIB.setOnClickListener(datePickerClickListener)
        saveBTN.setOnClickListener(onSaveClickListener)
    }

    //OPEN DATEPICKER DIALOG
    var datePickerClickListener = View.OnClickListener {
        datePickerDialog.show()
    }

    //DATE SELECTED
    var onDateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
        todoItemDeadlineTV.text = "$dayOfMonth-${month+1}-$year"
        isDateSelected = true
    }

    //SAVE
    var onSaveClickListener = View.OnClickListener {
        //VALIDATION
        if (todoItemNameET.text.toString().isNullOrEmpty() || todoItemDescriptionET.text.toString().isNullOrEmpty() || !isDateSelected){
           Snackbar.make(findViewById(android.R.id.content), R.string.check_fields, Snackbar.LENGTH_LONG).show()
            return@OnClickListener
        }
        var i = Intent()
        var resultCode = 0

        //CREATE NEW TODO_ITEM OPERATION
        if(todoItem == null){
            todoItem = TodoItem(0,
                todoItemNameET.text.toString(),
                todoItemDescriptionET.text.toString(),
                TodoHelper.getDateTimeAsLong(todoItemDeadlineTV.text.toString()),
                TodoHelper.getDateTimeAsLong(null),
                false,
                intent.getIntExtra(Consts.KEY_TODO_ID, -1))
            resultCode = RESULT_CODE_INSERT
        } else {//UPDATE OPERATION
            todoItem!!.name = todoItemNameET.text.toString()
            todoItem!!.description = todoItemDescriptionET.text.toString()
            todoItem!!.deadLine = TodoHelper.getDateTimeAsLong(todoItemDeadlineTV.text.toString())
            resultCode = RESULT_CODE_UPDATE
        }
        i.putExtra(KEY_TODO_ITEM, todoItem)
        setResult(resultCode, i)
        finish()
    }
}
