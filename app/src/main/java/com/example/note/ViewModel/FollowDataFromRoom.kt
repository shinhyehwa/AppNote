package com.example.note.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.note.Model.Notes
import com.example.note.Model.NotesDatabase
import com.example.note.Model.ToDo

class FollowDataFromRoom(application: Application): AndroidViewModel(application) {
    lateinit var allNotes: LiveData<List<Notes>>
    private lateinit var notesDatabase: NotesDatabase
    init {
        notesDatabase = NotesDatabase.getDatabase(application)
        allNotes = notesDatabase.noteDao().getAllNotes()
    }
}