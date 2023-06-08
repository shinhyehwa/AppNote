package com.example.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.example.note.Model.Notes
import com.example.note.ViewModel.NotesDatabase

class NewNotes : Fragment() {
    private lateinit var noteDatabase:NotesDatabase
    private lateinit var txt_timeNotes : TextView
    private lateinit var edt_NoteTitle : EditText
    private lateinit var edt_NoteContent : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteDatabase = context?.let { NotesDatabase.getDatabase(it)}!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_new_notes, container, false)
        txt_timeNotes = view.findViewById(R.id.fgmt_notes_time)
        edt_NoteTitle = view.findViewById(R.id.fgmt_notes_title)
        edt_NoteContent = view.findViewById(R.id.fgmt_notes_content)

        return view
    }


}