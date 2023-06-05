package com.example.note.ViewModel

import android.content.Context
import android.widget.Toast
import com.example.note.Model.Notes

fun findNote(query:String, oldItem:ArrayList<Notes>, context:Context): ArrayList<Notes> {
    val listData:ArrayList<Notes> = ArrayList()
    Toast.makeText(context,query,Toast.LENGTH_SHORT).show()
    if (query.isEmpty()){
        return oldItem
    }else{
        for (data in oldItem){
            if (data.title.contains(query) || data.content.contains(query)){
                listData.add(data)
            }
        }
        return listData
    }
}