package com.jdeveloperapps.noteapp.repositories

import com.jdeveloperapps.noteapp.database.NoteDao
import com.jdeveloperapps.noteapp.entities.Note
import javax.inject.Inject

class MainRepository @Inject constructor(val db: NoteDao) {

    suspend fun saveNote(note: Note) {
        db.insertNote(note)
    }

    fun getSaveNotes() = db.getAll()

    suspend fun deleteNote(note: Note) {
        db.deleteNote(note)
    }

}