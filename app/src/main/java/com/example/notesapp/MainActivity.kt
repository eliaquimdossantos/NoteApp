package com.example.notesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnEdit, OnDelete {
    private var notes = ArrayList<Note>()
    private lateinit var adapter: RecyclerView.Adapter<*>
    private var database = SQLiteRepository(this)
    private var editIndex = -1

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action,menu)

        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadAnnotations()

        notes = database.selectAll()

        adapter = NoteAdapter(notes, this, this::onNoteItemClick, this, this)

        initRecyclerView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if(id == R.id.actionAdd){
            addAnnotation()
            return true
        } else{
            return super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == 10){
            var title = data!!.getStringExtra("title")
            var text = data!!.getStringExtra("text")
            val message = Note(title, text)
            notes.add(message)
            database.save(notes[notes.size-1])
            adapter.notifyItemInserted(notes.lastIndex)
        } else if(resultCode == 11){
            var text = data!!.getStringExtra("text")
            notes[editIndex].text = text
            database.save(notes[editIndex])
            adapter.notifyItemChanged(editIndex)
        }
    }

    private fun toast(msg: String){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show()
    }

    fun loadAnnotations(){
        database.search("", notes)
    }

    fun initSwipeDelete(){
        val swipe = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                notes.removeAt(position)
                adapter.notifyItemRemoved(position)
            }

        }

        val itemTouchHelper = ItemTouchHelper(swipe)
        itemTouchHelper.attachToRecyclerView(rvMessages)
    }

    fun initRecyclerView(){
        rvMessages.adapter = adapter

        val layoutMAnager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvMessages.layoutManager = layoutMAnager
    }

    fun addAnnotation(){
        var intent = Intent(this, EditAnnotationActivity::class.java)
        intent.putExtra("title", "")
        startActivityForResult(intent, 10)
    }

    override fun editNote(pos: Int) {
        editIndex = pos
        var intent = Intent(this, EditAnnotationActivity::class.java)
        intent.putExtra("title", notes[pos].title)
        intent.putExtra("text", notes[pos].text)
        startActivityForResult(intent, 11)
    }

    fun onLoadData(note: Note){
        notes.add(note)
        adapter.notifyItemInserted(notes.lastIndex)
    }

    override fun deleteNote(pos: Int){
        database.remove(notes[pos])
        notes.removeAt(pos)
        adapter.notifyItemRemoved(pos)
    }

    fun onNoteItemClick(note: Note){
        val s = "${note.title}\n${note.text}"
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }
}
