package net.berkayak.mytasks.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import net.berkayak.mytasks.R
import net.berkayak.mytasks.data.model.TodoItem
import net.berkayak.mytasks.utilities.RecyclerviewClickListener
import net.berkayak.mytasks.utilities.TodoHelper
import java.util.*

class TodoItemRecyclerviewAdapter(private val context: Context,private var todoItemList: List<TodoItem>, private val listener: RecyclerviewClickListener):
    RecyclerView.Adapter<TodoItemRecyclerviewAdapter.TodoItemViewHolder>(){

    override fun onBindViewHolder(holder: TodoItemViewHolder, position: Int) {
        holder.bindItems(todoItemList[position], listener, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoItemViewHolder {
        var v = LayoutInflater.from(context).inflate(R.layout.item_todo_item, parent, false)
        return TodoItemViewHolder(v)
    }


    override fun getItemCount(): Int {
        return todoItemList.size
    }

    fun setDataSource(newList: List<TodoItem>?){
        if (newList == null)
            todoItemList = listOf<TodoItem>()
        else
            todoItemList = newList
        notifyDataSetChanged()

    }

    class TodoItemViewHolder(val v: View): RecyclerView.ViewHolder(v) {

        fun bindItems(todoItem: TodoItem, listener: RecyclerviewClickListener, pos: Int){
            //set Texts
            v.findViewById<TextView>(R.id.todoItemNameTV).text = todoItem.name
            v.findViewById<TextView>(R.id.todoItemDescriptionTV).text = todoItem.description
            v.findViewById<TextView>(R.id.todoItemStatusTV).text = if(todoItem.completed) "DONE" else "NOT DONE"
            v.findViewById<TextView>(R.id.todoItemCreateDateTV).text = TodoHelper.getDateTime(todoItem.createDate)
            v.findViewById<TextView>(R.id.todoItemDeadlineTV).text = TodoHelper.getDateTime(todoItem.deadLine)

            //organized the click events with my RecyclerviewClickListener
            v.findViewById<ImageButton>(R.id.todoItemEditIB).setOnClickListener{ listener.onClick(R.id.todoItemEditIB, todoItem.ID, pos)}
            v.findViewById<ImageButton>(R.id.todoItemDeleteIB).setOnClickListener{ listener.onClick(R.id.todoItemDeleteIB, todoItem.ID, pos)}
            itemView.setOnClickListener { listener.onClick(R.layout.item_todo_item, todoItem.ID, pos) }

        }
    }

}