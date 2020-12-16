package com.jdeveloperapps.noteapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jdeveloperapps.noteapp.entities.Note

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NotesDatabase : RoomDatabase() {

    abstract fun getNoteDao(): NoteDao

}