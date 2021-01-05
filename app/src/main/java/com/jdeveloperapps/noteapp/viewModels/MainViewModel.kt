package com.jdeveloperapps.noteapp.viewModels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.jdeveloperapps.noteapp.entities.Note
import com.jdeveloperapps.noteapp.repositories.MainRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    private val repository: MainRepository
) : ViewModel() {

    val searchQuery = MutableStateFlow("")

    private val notesFlow = searchQuery.flatMapLatest {
        repository.getNotes(it)
    }

    val notes = notesFlow.asLiveData()

    fun saveNote(note: Note) = viewModelScope.launch {
        repository.saveNote(note)
    }

    fun deleteNote(note: Note) = viewModelScope.launch {
        repository.deleteNote(note)
    }

}