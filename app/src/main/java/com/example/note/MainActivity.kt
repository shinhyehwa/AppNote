package com.example.note

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.note.Adapter.ViewPagerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        viewPager2.adapter = ViewPagerAdapter(this)
        TabLayoutMediator(tabLayout,viewPager2){
            tab,position -> tab.setIcon(setIconTab(position))
        }.attach()
    }

    private fun init(){
        tabLayout = findViewById(R.id.tabMode)
        viewPager2 = findViewById(R.id.viewpager)
    }
    private fun setIconTab(position : Int): Int{
        return when(position){
            0 -> R.drawable.note
            1 -> R.drawable.checklist
            2 -> R.drawable.account_user
            else -> R.drawable.note
        }
    }
}

