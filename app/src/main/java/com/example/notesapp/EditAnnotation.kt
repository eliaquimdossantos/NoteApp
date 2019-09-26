package com.example.notesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_edit_annotation.*

class EditAnnotation : AppCompatActivity() {

    private var title : String = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_annotation)

//        title = intent.getStringExtra("title")
    }

    fun editOrCreate(view: View){
        if (title == ""){
            createNote()
        } else {
            editNote()
        }
    }

    fun editNote(){
        var text = noteText.text.toString()
//        intent.putExtra("editNote", text)
    }

    fun createNote(){

        var text = noteText.text.toString()
        noteText.text.clear()

        var infos : Array<String> ?= null

        DialogSave.show(supportFragmentManager, object : DialogSave.OnTextListener {
            override fun onSetTExt(title: String) {
                intent.putExtra("title", title)
                intent.putExtra("text", text)
                setResult(10, intent)
                finish()
            }
        })

//        intent.putExtra("newNote", infos)
    }
}
