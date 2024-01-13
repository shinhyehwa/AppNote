package com.example.note.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.note.fragment.Home
import com.example.note.fragment.TodoList
import com.example.note.fragment.UserFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> Home()
            1 -> TodoList()
            2 -> UserFragment()
            else -> Home()
        }
    }
}