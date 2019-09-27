package com.example.notesapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor

class SQLiteRepository (context: Context) : NoteRepository{
    private val helper = NoteSqlHelper(context)

    private fun insert(note : Note){
        val db = helper.writableDatabase

        val cv = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_TEXT, note.text)
        }

        val id = db.insert(TABLE_NAME, null, cv)
        if(id != -1L){
            note.id = id
        }

        db.close()
    }

    override fun save(note: Note) {
        if (note.id == 0L){
            insert(note)
        } else {
            update(note)
        }
    }

    override fun remove(vararg notes: Note) {
        val db = helper.writableDatabase
        for (message in notes){
            db.delete(
                TABLE_NAME,
                "$COLUMN_ID = ?",
                arrayOf(message.id.toString())
            )
        }
        db.close()
    }

    override fun messageById(id: Long, callback: (Note?) -> Unit) {
        val sql = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = ? "
        val db = helper.writableDatabase
        val cursor = db.rawQuery(sql, arrayOf(id.toString()))
        val message = if (cursor.moveToNext())messageFromCursor(cursor) else null

        callback(message)
    }

    override fun search(term: String, callback: (List<Note>) -> Unit) {
        var sql = "SELECT * FROM $TABLE_NAME"
        var args: Array<String>? = null

        if (term.isNotEmpty()){
            sql += "WHERE $COLUMN_TITLE LIKE ?"
            args = arrayOf("%$term%")
        }

        sql += " ORDER BY $COLUMN_TITLE"
        val db = helper.readableDatabase
        val cursor = db.rawQuery(sql, args)
        val messages = ArrayList<Note>()
        while (cursor.moveToNext()){
            val message = messageFromCursor(cursor)
            messages.add(message)
        }
    }

    private fun update(note: Note){
        val db = helper.writableDatabase

        val cv = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_TEXT, note.text)
        }

        db.update(
            TABLE_NAME,
            cv,
            "$COLUMN_ID = ?",
            arrayOf(note.id.toString())
        )

        db.close()
    }

    private fun messageFromCursor(cursor: Cursor): Note {
        val id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID))
        val title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE))
        val text = cursor.getString(cursor.getColumnIndex(COLUMN_TEXT))

        return Note(title, text, id)
    }
}