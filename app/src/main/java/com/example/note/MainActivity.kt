package com.example.note

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.note.Model.Notes
import com.example.note.ViewModel.AdapterRecyclerView
import com.example.note.ViewModel.findNote
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var searchView:SearchView
    private lateinit var recyclerView:RecyclerView
    private lateinit var fButton:FloatingActionButton
    private lateinit var adapterRecyclerView:AdapterRecyclerView
    private lateinit var listItem:ArrayList<Notes>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        setData()
        adapterRecyclerView = AdapterRecyclerView(listItem)
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapterRecyclerView
    }
    private fun init() {
        searchView = findViewById(R.id.search_bar)
        recyclerView = findViewById(R.id.list_item)
        fButton = findViewById(R.id.fbutton_add)

        searchView.setOnQueryTextListener(object : OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                adapterRecyclerView.update(findNote(query,listItem, this@MainActivity))
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

        })

        fButton.setOnClickListener {
            Toast.makeText(this,"bạn đã bấm và đây", Toast.LENGTH_SHORT).show()
        }

    }

    private fun setData(){
        listItem = ArrayList()
        listItem.add(Notes(0,"Nội dung ôn tập","có văn bản"))
    }
}

