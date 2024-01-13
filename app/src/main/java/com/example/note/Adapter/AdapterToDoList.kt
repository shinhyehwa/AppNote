package com.example.note.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.note.Model.Notes
import com.example.note.Model.ToDo
import com.example.note.Model.ToDoDatabase
import com.example.note.R
import java.util.Objects

class AdapterToDoList(private val todoDatabase: ToDoDatabase) : RecyclerView.Adapter<AdapterToDoList.ViewHolderToDo>() {
    private val listToDO: ArrayList<ToDo> = ArrayList()
    class ViewHolderToDo(itemVIew: View): RecyclerView.ViewHolder(itemVIew) {
        val checkBox: CheckBox = itemVIew.findViewById<CheckBox>(R.id.checkbox_done_todo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderToDo {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.custom_todo_list,parent,false)
        return ViewHolderToDo(view)
    }

    override fun getItemCount(): Int {
        return listToDO.size
    }

    override fun onBindViewHolder(holder: ViewHolderToDo, position: Int) {
        val data = listToDO[position]
        holder.checkBox.isChecked = data.isDone
        holder.checkBox.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, isChecked ->
            run {
                data.isDone = isChecked
                todoDatabase.todoDao().updateToDo(data)
                if (isChecked) {
                    holder.checkBox.alpha = 0.5f
                    holder.checkBox.paint.isStrikeThruText = isChecked
                }else{
                    holder.checkBox.alpha = 1f
                    holder.checkBox.paint.isStrikeThruText = isChecked
                }
            }
        })
        if (data.isDone) {
            holder.checkBox.alpha = 0.5f
            holder.checkBox.paint.isStrikeThruText = true
        }else{
            holder.checkBox.alpha = 1f
            holder.checkBox.paint.isStrikeThruText = false
        }
        holder.checkBox.text = data.contentToDo
    }
    fun update(newToDo:ArrayList<ToDo>){
        val diff = DiffUtil.calculateDiff(ToDoDiffCallBack(newToDo,listToDO))
        diff.dispatchUpdatesTo(this)
        listToDO.clear()
        listToDO.addAll(newToDo)
    }
}
