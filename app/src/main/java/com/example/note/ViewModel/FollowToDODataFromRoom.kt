package com.example.note.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.note.Model.ToDo
import com.example.note.Model.ToDoDatabase

class FollowToDODataFromRoom(application: Application): AndroidViewModel(application) {
    lateinit var allToDo: LiveData<List<ToDo>>
    private lateinit var toDoDatabase: ToDoDatabase

    init {
        toDoDatabase = ToDoDatabase.getDatabase(application)
        allToDo = toDoDatabase.todoDao().getAllToDO()
    }
}