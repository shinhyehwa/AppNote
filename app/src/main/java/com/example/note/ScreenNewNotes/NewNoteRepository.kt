package com.example.note.ScreenNewNotes

import android.widget.TextView
import java.util.Calendar
import java.util.Date

fun showTimeToNewNote(textView: TextView, date: Date = Date()){
    val calender = Calendar.getInstance()
    calender.time = date
    val day = calender.get(Calendar.DAY_OF_MONTH)
    val month = calender.get(Calendar.MONTH) + 1
    val hour = calender.get(Calendar.HOUR)
    val minute = calender.get(Calendar.MINUTE)
    textView.text = "$day tháng $month, $hour:$minute"
}

fun addTextToStack(text:String, stack : ArrayDeque<String>){
    val maxSize = 5
    val currentSize = stack.size
    if(maxSize - currentSize > 0){
        stack.add(text)
    }else{
        stack.removeFirst()
        stack.add(text)
    }
}

