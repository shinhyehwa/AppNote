package com.example.note.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.note.Constant.constant
import com.example.note.Model.Notes
import com.example.note.ScreenNewNotes.NewNotes
import com.example.note.Adapter.AdapterRecyclerView
import com.example.note.Model.NotesDatabase
import com.example.note.R
import com.example.note.ViewModel.FollowDataFromRoom
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Home : Fragment() {
    private lateinit var viewModel: FollowDataFromRoom
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var fButton: FloatingActionButton
    private lateinit var adapterRecyclerView: AdapterRecyclerView
    private val listItem: ArrayList<Notes> = ArrayList()
    private lateinit var listSearch: ArrayList<Notes>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        searchView = view.findViewById(R.id.search_bar)
        recyclerView = view.findViewById(R.id.list_item)
        fButton = view.findViewById(R.id.fbutton_add)
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = layoutManager
        adapterRecyclerView = AdapterRecyclerView(fButton, requireActivity() as AppCompatActivity)
        recyclerView.adapter = adapterRecyclerView
        adapterRecyclerView.update(listItem)
        init()
        return view
    }

    private fun init() {
        adapterRecyclerView.onItemClick = {
            showFragmentNotes(it.id)
        }
        searchNote()

        fButton.setOnClickListener {
            showFragmentNotes(0.toLong())
        }
        viewModel = ViewModelProvider(this)[FollowDataFromRoom::class.java]

        viewModel.allNotes.observe(viewLifecycleOwner, Observer {
            listItem.clear()
            listItem.addAll(it)
            adapterRecyclerView.update(listItem)
        })
    }

    private fun showFragmentNotes(id : Long){
        val bundle = Bundle()
        bundle.putLong("id",id)
        val newFragment = NewNotes()
        newFragment.arguments = bundle
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, newFragment)
        fragmentTransaction.addToBackStack(constant.HOME_FRAGMENT)
        fragmentTransaction.commit()
    }

    private fun searchNote(){
        listSearch = ArrayList()
        searchView.setOnQueryTextListener(object : OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                listSearch.clear()
                if (newText != "") {
                    for (i in listItem) {
                        if (newText?.let { i.title.contains(it) } == true || newText?.let {
                                i.content.contains(
                                    it
                                )
                            } == true) {
                            listSearch.add(i)
                        }
                    }
                    adapterRecyclerView.update(listSearch)
                } else {
                    listSearch.clear()
                    adapterRecyclerView.update(listItem)
                }

                return false
            }

        })
    }
}