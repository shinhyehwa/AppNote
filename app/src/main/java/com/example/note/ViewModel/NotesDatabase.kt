package com.example.note.ViewModel

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.note.Constant.constant
import com.example.note.Model.Notes
import com.example.note.Model.NotesDao

@Database(entities = [Notes::class], version = 1, exportSchema = false)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun noteDao():NotesDao
    companion object{
        @Volatile
        private var Instance : NotesDatabase? = null

        fun getDatabase(context: Context) : NotesDatabase {
            return Instance?: synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext,
                    NotesDatabase::class.java,
                    constant.NAME_DATABASE
                ).fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()

                Instance = instance
                return instance
            }
        }
    }
}