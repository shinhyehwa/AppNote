package com.example.note.Adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.note.Model.ToDo

 class ToDoDiffCallBack(val newToDO: List<ToDo>, val oldTodo: List<ToDo>) : DiffUtil.Callback() {
     override fun getOldListSize(): Int {
         return oldTodo.size
     }

     override fun getNewListSize(): Int {
         return newToDO.size
     }

     override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
         return  newToDO[newItemPosition].id== oldTodo[oldItemPosition].id
     }

     override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
         return when{
             newToDO[newItemPosition].id != oldTodo[oldItemPosition].id ->false
             newToDO[newItemPosition].contentToDo != oldTodo[oldItemPosition].contentToDo -> false
             newToDO[newItemPosition].isDone != oldTodo[oldItemPosition].isDone -> false
             else -> true
         }
     }
 }