package com.jdeveloperapps.noteapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.jdeveloperapps.noteapp.entities.Note

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes ORDER BY id DESC")
    fun getAll(): LiveData<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)
}