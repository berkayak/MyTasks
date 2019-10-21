package net.berkayak.mytasks.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import net.berkayak.mytasks.R
import net.berkayak.mytasks.data.model.Todo
import net.berkayak.mytasks.utilities.RecyclerviewClickListener
import org.w3c.dom.Text

class TodoRecyclerviewAdapter(val context: Context, val todoList: List<Todo>, val listener: RecyclerviewClickListener): RecyclerView.Adapter<TodoRecyclerviewAdapter.TodoViewHolder>() {

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bindItems(todoList[position], listener, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        var v = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return TodoViewHolder(v)
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    class TodoViewHolder(val v: View): RecyclerView.ViewHolder(v) {


        fun bindItems(todo: Todo, listener: RecyclerviewClickListener, pos: Int){
            //set text
            v.findViewById<TextView>(R.id.todoNameTV).text = todo.name

            //organized the click events with my RecyclerviewClickListener
            v.findViewById<TextView>(R.id.todoNameTV).setOnClickListener { listener.onClick(R.id.todoNameTV, todo.ID, pos) }
            v.findViewById<ImageButton>(R.id.todoEditIB).setOnClickListener{ listener.onClick(R.id.todoEditIB, todo.ID, pos)}
            v.findViewById<ImageButton>(R.id.todoDeleteIB).setOnClickListener{ listener.onClick(R.id.todoDeleteIB, todo.ID, pos)}
        }

    }
}