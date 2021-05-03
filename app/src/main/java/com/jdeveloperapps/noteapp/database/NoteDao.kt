package com.jdeveloperapps.noteapp.database

import androidx.room.*
import com.jdeveloperapps.noteapp.entities.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes WHERE title LIKE '%' || :search || '%' ORDER BY id DESC")
    fun getAll(search: String): Flow<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)
}