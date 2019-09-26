package com.example.notesapp

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

class DialogSave : DialogFragment() {

    private var editText: EditText? = null
    private var listener: OnTextListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity!!)
        builder.setTitle("Nome da nota")
        builder.setPositiveButton("OK") { dialogInterface, i ->
            if (listener != null) {
                val text = editText!!.text.toString()
                listener!!.onSetTExt(text)
            }
        }

        builder.setNegativeButton("Cancelar") { dialogInterface, i -> dismiss() }

        val view = activity!!.layoutInflater.inflate(R.layout.fragment_dialog_save, null)
        editText = view.findViewById(R.id.editNome)
        builder.setView(view)
        return builder.create()
    }

    interface OnTextListener {
        fun onSetTExt(text: String)
    }

    companion object {
        fun show(fm: FragmentManager, listener: OnTextListener) {

            val dialog = DialogSave()
            dialog.listener = listener
            dialog.show(fm, "textDialog")

        }
    }
}
