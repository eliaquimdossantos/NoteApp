package com.example.notesapp

interface NoteRepository {

    fun save(note: Note)
    fun remove(vararg notes: Note)
    fun messageById(id:Long, callback: (Note?) -> Unit)
    fun search(term: String, callback: (List<Note>) -> Unit)
}