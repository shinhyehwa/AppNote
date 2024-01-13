package com.example.note.Model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ToDoDao {
    @Insert
    fun addNewToDo(todo: ToDo)

    @Delete
    fun deleteToDo(todo: ToDo)

    @Update
    fun updateToDo(todo: ToDo)

    @Insert
    fun addNewListToDo(data: List<ToDo>)

    @Query("SELECT * FROM ToDo")
    fun getAllToDO(): LiveData<List<ToDo>>

    @Query("SELECT * FROM ToDo")
    fun getAllData(): List<ToDo>
}