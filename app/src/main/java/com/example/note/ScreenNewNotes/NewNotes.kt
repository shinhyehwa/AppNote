package com.example.note.ScreenNewNotes

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.note.Home
import com.example.note.Model.Notes
import com.example.note.R
import com.example.note.ViewModel.NotesDatabase
import java.util.Date

class NewNotes : Fragment() {
    private lateinit var noteDatabase:NotesDatabase
    private lateinit var txt_timeNotes : TextView
    private lateinit var edt_NoteTitle : EditText
    private lateinit var edt_NoteContent : EditText
    private lateinit var ibtn_back : ImageButton
    private lateinit var ibtn_undo : ImageButton
    private lateinit var ibtn_forward : ImageButton
    private lateinit var ibtn_tick : ImageButton
    private var canUndo = false
    private var canForward = false
    private var canUpdate = false
    private var isHaveText = true
    private var haveText = ""
    private lateinit var undoContent: ArrayDeque<String>
    private lateinit var forwardContent: ArrayDeque<String>
    private lateinit var note:Notes
    var idNote : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteDatabase = context?.let { NotesDatabase.getDatabase(it)}!!
        val bundle = arguments
        idNote = bundle?.getLong("id")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_new_notes, container, false)
        txt_timeNotes = view.findViewById(R.id.fgmt_notes_time)
        edt_NoteTitle = view.findViewById(R.id.fgmt_notes_title)
        edt_NoteContent = view.findViewById(R.id.fgmt_notes_content)
        ibtn_back = view.findViewById(R.id.img_btn_back)
        ibtn_undo = view.findViewById(R.id.img_btn_undo)
        ibtn_forward = view.findViewById(R.id.img_btn_forward)
        ibtn_tick = view.findViewById(R.id.img_btn_tick)
        undoContent = ArrayDeque()
        forwardContent = ArrayDeque()

        if (idNote != 0.toLong()){
            canUpdate = true
            note = noteDatabase.noteDao().getNote(idNote)
            showTimeToNewNote(txt_timeNotes, Date(note.date))
            edt_NoteTitle.setText(note.title)
            edt_NoteContent.setText(note.content)
        }
        else{
            showTimeToNewNote(txt_timeNotes)
        }



        setUp() // làm mở các button khi vừa tạo note mới
        backHomeFragment() // trở về màn hình home
        changTile() // thay đổi khi thực hiện focus vào title
        changContent()
        return view
    }

    private fun backHomeFragment(){
        ibtn_back.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.remove(this)
            fragmentTransaction.commit()
            fragmentManager.popBackStack()
        }

    }

    private fun changTile(){
        edt_NoteTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    ibtn_tick.alpha = 0.5f
                    ibtn_tick.isEnabled = false
                } else {
                    ibtn_tick.alpha = 1.0f
                    ibtn_tick.isEnabled = true
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

    private fun changContent(){
        edt_NoteContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if(isHaveText){
                    undoContent.add(s.toString())
                    haveText = s.toString()
                    isHaveText = false
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s.toString()
                if(undoContent.lastOrNull() != text && text != ""){
                    undoContent.add(text)
                    canUndo = true
                }

                updateButtonStates()
            }

            override fun afterTextChanged(s: Editable?) {
                if(s.isNullOrEmpty()){
                    ibtn_tick.alpha = 0.5f
                    ibtn_tick.isEnabled = false
                }else{
                    ibtn_tick.alpha = 1.0f
                    ibtn_tick.isEnabled = true
                }
            }
        })
    }
    private fun setUp(){
        ibtn_tick.alpha = 0.5f
        ibtn_tick.isEnabled = false
        updateButtonStates()
        edt_NoteTitle.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus){
                ibtn_undo.visibility = View.INVISIBLE
                ibtn_forward.visibility = View.INVISIBLE
            }else{
                ibtn_undo.visibility = View.VISIBLE
                ibtn_forward.visibility = View.VISIBLE
            }
        }

        ibtn_undo.setOnClickListener {
            undo()
            if (undoContent.isEmpty()){
                canUndo = false
                updateButtonStates()
            }
        }

        ibtn_forward.setOnClickListener {
            forward()
            if(forwardContent.isEmpty()){
                canForward = false
                updateButtonStates()
            }
        }

        saveNotes()
    }

    private fun undo(){
        if (canUndo){
            forwardContent.addLast(undoContent.removeLast())
            var text : String = ""
            if (undoContent.isEmpty()){
                canUndo = false
            }else{
                text = undoContent.last()
            }
            if (!isHaveText){
                text = haveText
                canUndo = false
                edt_NoteContent.setText(text)
                undoContent.removeLast()
            }else{
                edt_NoteContent.setText(text)
            }
            edt_NoteContent.setSelection(edt_NoteContent.text.length)
            canForward = true
        }
    }
    private fun forward(){
        if (forwardContent.isNotEmpty()) {
            val text = forwardContent.removeLast()
            edt_NoteContent.setText(text)
            edt_NoteContent.setSelection(edt_NoteContent.text.length)
        }
    }


    private fun updateButtonStates() {
        if (canUndo) {
            ibtn_undo.alpha = 1.0f
            ibtn_undo.isEnabled = true
        } else {
            ibtn_undo.alpha = 0.5f
            ibtn_undo.isEnabled = false
        }

        if (canForward) {
            ibtn_forward.alpha = 1.0f
            ibtn_forward.isEnabled = true
        } else {
            ibtn_forward.alpha = 0.5f
            ibtn_forward.isEnabled = false
        }
    }

    private fun saveNotes(){
        ibtn_tick.setOnClickListener {
            val date:Long = Date().time
            val title = edt_NoteTitle.text.toString()
            val content = edt_NoteContent.text.toString()
            if (!canUpdate){
                idNote = noteDatabase.noteDao().addNewNote(Notes(title = title, content = content,date = date))
                val inputMethodManager = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
                canUpdate = true
            }else{
                noteDatabase.noteDao().changeNote(Notes(id = idNote,title = title, content = content, date = date))
                val inputMethodManager = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
            }
        }
    }

}