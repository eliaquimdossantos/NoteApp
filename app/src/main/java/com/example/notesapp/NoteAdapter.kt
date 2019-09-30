package com.example.notesapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_note.view.*

class NoteAdapter (
    private val notes: ArrayList <Note>,
    private val context: Context,
    private val callback:(Note) -> Unit,
    private val onEdit: OnEdit
): RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    var onItemClick: ((Note) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val noteview = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false)

        return ViewHolder(noteview, context)
    }

    override fun getItemCount(): Int {
        return notes.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notes[position]
        holder.title.text = note.title

    }

    inner class ViewHolder(itemView: View, private val context: Context) : RecyclerView.ViewHolder(itemView){

        val title = itemView.txtTitle

        init {
            itemView.setOnClickListener()
            {
                onEdit.editNote(layoutPosition)
            }
            itemView.setOnLongClickListener()
            {
                true
            }
        }
    }
}