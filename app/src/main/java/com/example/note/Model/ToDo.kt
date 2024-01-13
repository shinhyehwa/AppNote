package com.example.note.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ToDo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val contentToDo: String,
    var isDone: Boolean
)