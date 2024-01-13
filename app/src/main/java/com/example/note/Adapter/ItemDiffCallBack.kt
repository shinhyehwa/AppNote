package com.example.note.Adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.note.Model.Notes

class ItemDiffCallBack(private val newItem:List<Notes>, private val oldItem:List<Notes>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldItem.size
    }

    override fun getNewListSize(): Int {
        return newItem.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItem[oldItemPosition].id == newItem[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when{
            oldItem[oldItemPosition].id != newItem[newItemPosition].id -> false
            oldItem[oldItemPosition].title != newItem[newItemPosition].title -> false
            oldItem[oldItemPosition].content != newItem[newItemPosition].content -> false
            oldItem[oldItemPosition].date != newItem[newItemPosition].date -> false
            else -> true
        }
    }
}