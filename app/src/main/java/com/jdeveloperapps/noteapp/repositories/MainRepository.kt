package com.jdeveloperapps.noteapp.repositories

import com.jdeveloperapps.noteapp.database.NoteDao
import com.jdeveloperapps.noteapp.entities.Note
import javax.inject.Inject

class MainRepository @Inject constructor(private val db: NoteDao) {

    suspend fun insetrNote(note: Note) {
        db.insertNote(note)
    }

    fun getNotes(search: String) = db.getAll(search)

    suspend fun deleteNote(note: Note) {
        db.deleteNote(note)
    }

    suspend fun updateNote(note: Note) {
        db.updateNote(note)
    }

}