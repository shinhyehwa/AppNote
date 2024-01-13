package com.example.note.Model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface NotesDao {
    @Insert
    fun addNewNote(note:Notes) : Long

    @Insert
    fun addNewNotes(notes:List<Notes>)

    @Update
    fun changeNote(note: Notes)

    @Delete
    fun deleteNote(notes : Notes)


    @Query("SElECT * FROM Notes")
    fun getAllNotes() : LiveData<List<Notes>>

    @Query("SElECT * FROM Notes")
    fun getAllData() : List<Notes>

    @Query("SELECT * FROM Notes WHERE id = :idNotes")
    fun getNote(idNotes:Long) : Notes
}