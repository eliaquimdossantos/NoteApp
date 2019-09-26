package com.example.notesapp

interface MessageRepository {

    fun save(message: Message)
    fun remove(vararg messages: Message)
    fun messageById(id:Long, callback: (Message?) -> Unit)
    fun search(term: String, callback: (List<Message>) -> Unit)
}