package com.example.note

import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Note
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.note.Constant.constant
import com.example.note.Model.Notes
import com.example.note.ScreenNewNotes.NewNotes
import com.example.note.ViewModel.AdapterRecyclerView
import com.example.note.ViewModel.NotesDatabase
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Date

class Home : Fragment() {
    private lateinit var noteDatabase:NotesDatabase
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var fButton: FloatingActionButton
    private lateinit var adapterRecyclerView: AdapterRecyclerView
    private lateinit var listItem:ArrayList<Notes>
    private lateinit var listSearch : ArrayList<Notes>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listItem = ArrayList()
        noteDatabase = context?.let { NotesDatabase.getDatabase(it)}!!
        listItem.addAll(noteDatabase.noteDao().getALlNotes())
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

        adapterRecyclerView.update(listItem)
        return view
    }

    private fun init(view:View) {
        searchView = view.findViewById(R.id.search_bar)
        recyclerView = view.findViewById(R.id.list_item)
        fButton = view.findViewById(R.id.fbutton_add)
        adapterRecyclerView = AdapterRecyclerView(fButton, requireActivity() as AppCompatActivity)

        adapterRecyclerView.onItemClick = {
            showFragmentNotes(it.id)
        }
        searchNote()
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
                val text = newText
                if(text != ""){
                    for (i in listItem){
                        if (text?.let { i.title.contains(it) } == true || text?.let {i.content.contains(it)} == true){
                            listSearch.add(i)
                        }
                    }
                    adapterRecyclerView.update(listSearch)
                }else{
                    listSearch.clear()
                    adapterRecyclerView.update(listItem)
                }

                return false
            }

        })
    }

    override fun onResume() {
        super.onResume()
        listItem.clear()
        listItem.addAll(noteDatabase.noteDao().getALlNotes())
        adapterRecyclerView.update(listItem)
        Log.e("onResume","onResume")
    }
}