package com.example.notesapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor

class SQLiteRepository (context: Context) : MessageRepository{
    private val helper = MessageSqlHelper(context)

    private fun insert(message : Message){
        val db = helper.writableDatabase

        val cv = ContentValues().apply {
            put(COLUMN_TITLE, message.title)
            put(COLUMN_TEXT, message.text)
        }

        val id = db.insert(TABLE_NAME, null, cv)
        if(id != -1L){
            message.id = id
        }

        db.close()
    }

    override fun save(message: Message) {
        if (message.id == 0L){
            insert(message)
        } else {
            update(message)
        }
    }

    override fun remove(vararg messages: Message) {
        val db = helper.writableDatabase
        for (message in messages){
            db.delete(
                TABLE_NAME,
                "$COLUMN_ID = ?",
                arrayOf(message.id.toString())
            )
        }
        db.close()
    }

    override fun messageById(id: Long, callback: (Message?) -> Unit) {
        val sql = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = ? "
        val db = helper.writableDatabase
        val cursor = db.rawQuery(sql, arrayOf(id.toString()))
        val message = if (cursor.moveToNext())messageFromCursor(cursor) else null

        callback(message)
    }

    override fun search(term: String, callback: (List<Message>) -> Unit) {
        var sql = "SELECT * FROM $TABLE_NAME"
        var args: Array<String>? = null

        if (term.isNotEmpty()){
            sql += "WHERE $COLUMN_TITLE LIKE ?"
            args = arrayOf("%$term%")
        }

        sql += " ORDER BY $COLUMN_TITLE"
        val db = helper.readableDatabase
        val cursor = db.rawQuery(sql, args)
        val messages = ArrayList<Message>()
        while (cursor.moveToNext()){
            val message = messageFromCursor(cursor)
            messages.add(message)
        }
    }

    private fun update(message: Message){
        val db = helper.writableDatabase

        val cv = ContentValues().apply {
            put(COLUMN_TITLE, message.title)
            put(COLUMN_TEXT, message.text)
        }

        db.update(
            TABLE_NAME,
            cv,
            "$COLUMN_ID = ?",
            arrayOf(message.id.toString())
        )

        db.close()
    }

    private fun messageFromCursor(cursor: Cursor): Message {
        val id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID))
        val title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE))
        val text = cursor.getString(cursor.getColumnIndex(COLUMN_TEXT))

        return Message(id, title, text)
    }
}