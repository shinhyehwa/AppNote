package com.example.note.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.note.Login
import com.example.note.Model.Notes
import com.example.note.Model.NotesDatabase
import com.example.note.Model.ToDo
import com.example.note.Model.ToDoDatabase
import com.example.note.R
import com.google.firebase.firestore.FirebaseFirestore


class UserFragment : Fragment() {
    private lateinit var accountUser: LinearLayout
    private lateinit var syncData: LinearLayout
    private lateinit var uploadData: LinearLayout
    private lateinit var accountName: TextView
    private lateinit var btnLogout: Button
    var noAccount:String = "Chưa đăng nhập tài khoản"
    private lateinit var nameAccount:String
    private lateinit var dbNotes: NotesDatabase
    private lateinit var dbToDo: ToDoDatabase
    private lateinit var db : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val sharePref = activity?.getSharedPreferences("PREFERENCE_LOGIN",Context.MODE_PRIVATE)
        nameAccount = sharePref?.getString("GMAIL",noAccount).toString()
        val view = inflater.inflate(R.layout.fragment_user, container, false)
        accountUser = view.findViewById(R.id.account_user)
        syncData = view.findViewById(R.id.syncData)
        uploadData = view.findViewById(R.id.uploadData)
        accountName = view.findViewById(R.id.accountName)
        btnLogout = view.findViewById(R.id.btn_logout)
        if(nameAccount == noAccount){
            accountName.text = noAccount
            accountUser.isEnabled = true
            btnLogout.visibility = View.GONE
        }else{
            accountName.text = nameAccount
            accountUser.isEnabled = false
            btnLogout.visibility = View.VISIBLE
        }
        action()
        return view
    }

    private fun action(){
        accountUser.setOnClickListener{
            activity?.let {
                val ScreenLogin = Intent (it, Login::class.java)
                it.startActivity(ScreenLogin)
            }
        }

        syncData.setOnClickListener{
            if(nameAccount == noAccount){
                Toast.makeText(context,"Bạn cần đăng nhập để đồng bộ dữ liệu",Toast.LENGTH_LONG).show()
            }else{
                val newListNote: ArrayList<Notes> = ArrayList()
                val newListToDO: ArrayList<ToDo> = ArrayList()
                context?.let {
                    dbToDo = ToDoDatabase.getDatabase(it)
                    dbNotes = NotesDatabase.getDatabase(it)
                }
                db = FirebaseFirestore.getInstance()
                db.collection("users").document(nameAccount).collection("Notes").get().addOnCompleteListener {
                    if(it.isSuccessful){
                        val document = it.result
                        for(doc in document){
                            val data = doc.data
                            newListNote.add(Notes(
                                title = data["title"] as String, content = data["content"] as String, date = data["date"] as Long
                            ))
                        }
                        dbNotes.noteDao().addNewNotes(newListNote)
                    }
                }
                db.collection("users").document(nameAccount).collection("todolist").get().addOnCompleteListener {
                    if(it.isSuccessful){
                        val document = it.result
                        for(doc in document){
                            val data = doc.data
                            newListToDO.add(
                                ToDo(contentToDo = data["contentToDo"].toString(),
                                   isDone = data.getValue("done") as Boolean
                                )
                            )
                        }
                        dbToDo.todoDao().addNewListToDo(newListToDO)
                    }
                }
            }
        }

        uploadData.setOnClickListener {
            if (nameAccount != noAccount){
                context?.let {
                    dbToDo = ToDoDatabase.getDatabase(it)
                    dbNotes = NotesDatabase.getDatabase(it)
                }

                val ListNote: ArrayList<Notes> = ArrayList()
                val ListToDO: ArrayList<ToDo> = ArrayList()

                ListNote.addAll(dbNotes.noteDao().getAllData())
                ListToDO.addAll(dbToDo.todoDao().getAllData())

                if(ListNote.isNotEmpty()){
                    for (i in ListNote){
                        db.collection("users").document(nameAccount).collection("Notes").document(i.id.toString()).set(i)
                    }
                }
                if(ListToDO.isNotEmpty()){
                    for (i in ListToDO){
                        db.collection("users").document(nameAccount).collection("todolist").document(i.id.toString()).set(i)
                    }
                }

            }
        }

        btnLogout.setOnClickListener{
            val sharePref = activity?.getSharedPreferences("PREFERENCE_LOGIN",Context.MODE_PRIVATE)
            val edit = sharePref?.edit()
            if (edit != null) {
                edit.clear()
                edit.apply()
            }
            accountName.text = "Chưa đăng nhập"
            accountUser.isEnabled = true
            it.visibility = View.GONE
        }
    }

}