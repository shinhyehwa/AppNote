package com.example.note.fragment

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.note.Adapter.AdapterToDoList
import com.example.note.Model.ToDo
import com.example.note.Model.ToDoDatabase
import com.example.note.R
import com.example.note.ViewModel.FollowToDODataFromRoom
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class TodoList : Fragment() {
    private lateinit var floatingActionButton: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapterToDoList: AdapterToDoList
    private lateinit var todoDatabase:ToDoDatabase
    private val _listToDo: ArrayList<ToDo> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        todoDatabase = context?.let { ToDoDatabase.getDatabase(it) }!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_todo_list, container, false)
        recyclerView = view.findViewById(R.id.ryc_todo)
        floatingActionButton = view.findViewById(R.id.fbutton_add_todo)
        adapterToDoList = AdapterToDoList(todoDatabase)
        recyclerView.adapter = adapterToDoList
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val removedItem = _listToDo[position]

                _listToDo.removeAt(position)
                adapterToDoList.update(_listToDo)

                Snackbar.make(recyclerView, "đã xóa", Snackbar.LENGTH_LONG)
                    .setAction("Hoàn tác") {
                        _listToDo.add(position, removedItem)
                        adapterToDoList.update(_listToDo)
                    }
                    .addCallback(object : Snackbar.Callback() {
                        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                            if (event == DISMISS_EVENT_TIMEOUT || event == DISMISS_EVENT_SWIPE ||
                                event == DISMISS_EVENT_CONSECUTIVE
                            ) {
                                _listToDo.remove(removedItem)
                                todoDatabase.todoDao().deleteToDo(removedItem)
                                adapterToDoList.update(_listToDo)
                            }
                        }
                    })
                    .show()
            }

        })
        itemTouchHelper.attachToRecyclerView(recyclerView)

        init()
        return view
    }

    private fun init(){
        val viewModel = ViewModelProvider(this)[FollowToDODataFromRoom::class.java]
        viewModel.allToDo.observe(viewLifecycleOwner, Observer {
            _listToDo.clear()
            _listToDo.addAll(it)
            adapterToDoList.update(_listToDo)
        })

        floatingActionButton.setOnClickListener {
            showDialog()
        }
    }

    fun showDialog(){
        val dialog = activity?.let { Dialog(it) }
        if (dialog != null) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_add_todo)
            dialog.setCancelable(false)
            val _Window: Window? = dialog.window
            if (_Window == null){
                return
            }else{
                _Window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT)
                val gravityAtributes = _Window.attributes
                gravityAtributes.gravity = Gravity.BOTTOM
                _Window.attributes = gravityAtributes
            }

            val content = dialog.findViewById(R.id.content_task) as EditText
            val btnDone = dialog.findViewById(R.id.newToDo) as Button
            val titleTask = dialog.findViewById(R.id.title_newTask) as TextView
            val btnColes = dialog.findViewById(R.id.btnClose) as Button
            btnDone.isEnabled = false
            content.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                   if(s.isNullOrBlank()){
                       titleTask.visibility = View.GONE
                       btnDone.setTextColor(resources.getColor(R.color.silver))
                       btnDone.setBackgroundColor(resources.getColor(R.color.white))
                       btnDone.isEnabled = false
                   }else{
                       titleTask.visibility = View.VISIBLE
                       btnDone.setTextColor(resources.getColor(R.color.amethyst))
                       btnDone.setBackgroundColor(resources.getColor(R.color.light_blue_ballerina))
                       btnDone.isEnabled = true
                   }
                }
            })
            btnDone.setOnClickListener {
                val data = ToDo(contentToDo = content.text.toString(), isDone = false)
                todoDatabase.todoDao().addNewToDo(data)
                dialog.dismiss()
            }
            btnColes.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }
}