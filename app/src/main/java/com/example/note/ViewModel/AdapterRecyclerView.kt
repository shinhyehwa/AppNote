package com.example.note.ViewModel

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.contentValuesOf
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.note.Constant.constant
import com.example.note.Model.Notes
import com.example.note.R
import java.util.Calendar
import java.util.Date

class AdapterRecyclerView(private val listItem:ArrayList<Notes>):RecyclerView.Adapter<AdapterRecyclerView.ViewHolder>() {
    var onItemClick : ((Notes) -> Unit)? = null
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val edt_title:TextView = itemView.findViewById(R.id.title_notes)
        val edt_contens:TextView = itemView.findViewById(R.id.note_content)
        val txt_noteTime:TextView = itemView.findViewById(R.id.note_time)
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

        holder.edt_title.text = data.title
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
            onItemClick?.invoke(data)
        }
    }

    fun update(newList:ArrayList<Notes>){
        val diff = DiffUtil.calculateDiff(ItemDiffCallBack(newList,listItem))
        diff.dispatchUpdatesTo(this)
        listItem.clear()
        listItem.addAll(newList)
    }
}