package com.example.note.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
@Entity
data class Notes(
    @PrimaryKey(autoGenerate = true) val id: Int,
     val title: String,
    val content: String,
    val date:Long
)
