package com.jdeveloperapps.noteapp.viewModels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdeveloperapps.noteapp.entities.Note
import com.jdeveloperapps.noteapp.repositories.MainRepository
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    val repository: MainRepository
) : ViewModel() {

    fun saveNote(note: Note) = viewModelScope.launch {
        repository.saveNote(note)
    }

    fun getSaveNotes() = repository.getSaveNotes()

    fun deleteNote(note: Note) = viewModelScope.launch {
        repository.deleteNote(note)
    }

}