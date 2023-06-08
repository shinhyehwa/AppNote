package com.example.note

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.note.Constant.constant
import com.example.note.Model.Notes
import com.example.note.ViewModel.AdapterRecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Date

class Home : Fragment() {

    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var fButton: FloatingActionButton
    private lateinit var adapterRecyclerView: AdapterRecyclerView
    private lateinit var listItem:ArrayList<Notes>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listItem = ArrayList()
        val date  = Date().time
        listItem.add(Notes(0,"Nội dung ôn tập","có văn bản",date))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        init(view)
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapterRecyclerView
        return view
    }

    private fun init(view:View) {
        searchView = view.findViewById(R.id.search_bar)
        recyclerView = view.findViewById(R.id.list_item)
        fButton = view.findViewById(R.id.fbutton_add)
        adapterRecyclerView = AdapterRecyclerView(listItem)
        adapterRecyclerView.onItemClick = {
            showFragmentNotes()
        }

        fButton.setOnClickListener {
            showFragmentNotes()
        }
    }

    private fun showFragmentNotes(){
        val newFragment = NewNotes()
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, newFragment)
        fragmentTransaction.addToBackStack(constant.HOME_FRAGMENT)
        fragmentTransaction.commit()
    }
}