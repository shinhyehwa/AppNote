package com.example.note.ViewModel

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
        return oldItem[oldItemPosition] == newItem[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItem[oldItemPosition] == newItem[newItemPosition]
    }
}