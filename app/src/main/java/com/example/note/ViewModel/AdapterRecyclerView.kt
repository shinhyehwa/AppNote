package com.example.note.ViewModel

import android.os.Bundle
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.isEmpty
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.note.Constant.constant
import com.example.note.Model.Notes
import com.example.note.R
import com.example.note.ScreenNewNotes.NewNotes
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Calendar
import java.util.Date

class AdapterRecyclerView(private val floatingActionButton: FloatingActionButton, private val activity: AppCompatActivity):RecyclerView.Adapter<AdapterRecyclerView.ViewHolder>() {
    var listItem: ArrayList<Notes> = ArrayList()
    var onItemClick : ((Notes) -> Unit)? = null
    private var isEnable = false
    private val selectedItems = SparseBooleanArray()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val edt_title:TextView = itemView.findViewById(R.id.title_notes)
        val edt_contens:TextView = itemView.findViewById(R.id.note_content)
        val txt_noteTime:TextView = itemView.findViewById(R.id.note_time)
        val imageView : ImageView = itemView.findViewById(R.id.delete_checkbox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.row_item_recy_main,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listItem.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data:Notes = listItem[position]

        holder.edt_title.text = data.title.ifEmpty {
            constant.NO_TITLE
        }
        val content = data.content.ifEmpty {
            constant.DEFAUL_NOTES
        }
        holder.edt_contens.text = content
        val calender = Calendar.getInstance()
        calender.time = Date(data.date)
        val day = calender.get(Calendar.DAY_OF_MONTH)
        val month = calender.get(Calendar.MONTH) + 1
        val hour = calender.get(Calendar.HOUR)
        val minute = calender.get(Calendar.MINUTE)
        holder.txt_noteTime.text = "$day tháng $month, $hour:$minute"

        holder.itemView.setOnClickListener {
            if (isEnable) {
                val isSelected = selectedItems.get(position, false)
                if (isSelected) {
                    selectedItems.delete(position)
                    if (selectedItems.isEmpty()){
                        isEnable = false
                        floatingActionButton.setImageResource(R.drawable.baseline_add_24)
                    }
                } else {
                    selectedItems.put(position, true)
                }
                holder.imageView.visibility =
                    if (selectedItems.get(position, false)) View.VISIBLE else View.INVISIBLE
            } else {
                onItemClick?.invoke(data)
            }
        }

        holder.itemView.setOnLongClickListener {
            holder.itemView.isSelected = true
            selectedItems.put(position, true)
            holder.imageView.visibility = View.VISIBLE
            isEnable = true
            floatingActionButton.setImageResource(R.drawable.deleteitem)
            true
        }

        holder.imageView.visibility =
            if (selectedItems.get(position, false)) View.VISIBLE else View.INVISIBLE

        floatingActionButton.setOnClickListener {
            if (isEnable) {
                val positionsToRemove = ArrayList<Int>()
                for (i in 0 until selectedItems.size()) {
                    val position = selectedItems.keyAt(i)
                    if (position >= 0 && position < listItem.size) {
                        positionsToRemove.add(position)
                    }
                }

                for (i in positionsToRemove.indices.reversed()) {
                    val position = positionsToRemove[i]
                    listItem.removeAt(position)
                    selectedItems.delete(position)
                }

                notifyDataSetChanged()
                isEnable = false
                floatingActionButton.setImageResource(R.drawable.baseline_add_24)
            } else {
                showFragmentNotes(0.toLong())
            }
        }

    }

    fun update(newList:ArrayList<Notes>){
        val diff = DiffUtil.calculateDiff(ItemDiffCallBack(newList,listItem))
        diff.dispatchUpdatesTo(this)
        listItem.clear()
        listItem.addAll(newList)
    }
    private fun showFragmentNotes(id : Long){
        val bundle = Bundle()
        bundle.putLong("id",id)
        val newFragment = NewNotes()
        newFragment.arguments = bundle
        val fragmentManager = activity.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, newFragment)
        fragmentTransaction.addToBackStack(constant.HOME_FRAGMENT)
        fragmentTransaction.commit()
    }
}