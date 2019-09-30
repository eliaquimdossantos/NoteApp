package com.example.notesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_edit_annotation.*

class EditAnnotationActivity : AppCompatActivity() {

    private var title : String = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_annotation)
        noteText.setText(intent.getStringExtra("text"))
    }

    fun editOrCreate(view: View){
        title = intent.getStringExtra("title")
        println("title is" + title)
        if (title == ""){
            createNote()
        } else {
            editNote()
        }
    }

    fun editNote(){
        var text = noteText.text.toString()
        intent.putExtra("text", text)
        setResult(11,intent)
        finish()
    }

    fun createNote(){
        // Text of annotation
        var text = noteText.text.toString()
        // Show dialog from save
        DialogSave.show(supportFragmentManager, object : DialogSave.OnTextListener {
            override fun onSetTExt(title: String) {
                intent.putExtra("title", title)
                intent.putExtra("text", text)
                setResult(10, intent)
                finish()
            }
        })

    }
}
