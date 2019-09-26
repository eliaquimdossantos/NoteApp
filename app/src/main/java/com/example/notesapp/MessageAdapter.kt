package com.example.notesapp

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.item_message.view.*

class MessageAdapter (private val notes: ArrayList <Message>, private val context: Context): RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    var onItemClick: ((Message) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val noteview = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false)

        return ViewHolder(noteview, context)
    }

    override fun getItemCount(): Int {
        return notes.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notes[position]
        holder.title.text = note.title
        holder.description.text = note.text

    }

    inner class ViewHolder(itemView: View, private val context: Context) : RecyclerView.ViewHolder(itemView){

        val title = itemView.txtTitle
        val description = itemView.txtText
        init {
            itemView.setOnClickListener()
            {
//                editListener.editNote(layoutPosition, description.text.toString())
            }
            itemView.setOnLongClickListener()
            {
//                deleteListener.deleteNote(layoutPosition)
                true
            }
        }
    }
}