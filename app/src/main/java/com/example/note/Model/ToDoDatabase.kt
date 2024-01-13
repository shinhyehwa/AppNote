package com.example.note.Model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.note.Constant.constant
import com.example.note.fragment.TodoList

@Database(entities = [ToDo::class], version = 2)
abstract class ToDoDatabase: RoomDatabase() {
    abstract fun todoDao(): ToDoDao

    companion object{
        @Volatile
        private var Instance2: ToDoDatabase? = null

        fun getDatabase(context: Context): ToDoDatabase {
            return Instance2 ?: synchronized(this) {
                val instance2 = Room.databaseBuilder(
                    context.applicationContext,
                    ToDoDatabase::class.java,
                    constant.TODO_DATABASE
                ).fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()

                Instance2 = instance2
                instance2
            }
        }
    }
}
