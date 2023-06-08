package com.example.note.ViewModel

import android.app.Activity
import android.content.Context
import android.net.eap.EapSessionConfig.EapMsChapV2Config
import android.util.Log
import android.widget.Toast
import com.example.note.Constant.constant
import com.example.note.Model.Notes
import com.example.note.NewNotes
import com.example.note.R

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

fun makeToast(ms:String, context:Activity){
    Toast.makeText(context,ms,Toast.LENGTH_SHORT).show()
}

fun makeLog(ms:String, tag:String){
    Log.e(tag,ms)
}