package com.jdeveloperapps.noteapp.ui.fragments.listFragment

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.jdeveloperapps.noteapp.R
import com.jdeveloperapps.noteapp.entities.Note
import com.jdeveloperapps.noteapp.repositories.MainRepository
import com.jdeveloperapps.noteapp.ui.activities.ADD_NOTE_RESULT_OK
import com.jdeveloperapps.noteapp.ui.activities.EDIT_NOTE_RESULT_OK
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ListFragmentViewModel @ViewModelInject constructor(
    @ApplicationContext private val context: Context,
    private val repository: MainRepository
) : ViewModel() {

    val searchQuery = MutableStateFlow("")

    private val noteEventChannel = Channel<NoteEvent>()
    val noteEvent = noteEventChannel.receiveAsFlow()

    private val notesFlow = searchQuery.flatMapLatest {
        repository.getNotes(it)
    }

    val notes = notesFlow.asLiveData()

    fun saveNote(note: Note) = viewModelScope.launch {
        repository.insetrNote(note)
    }

    fun deleteNote(note: Note) = viewModelScope.launch {
        repository.deleteNote(note)
    }

    fun onNoteSelected(note: Note) = viewModelScope.launch {
        noteEventChannel.send(NoteEvent.NavigateToEditTaskScreen(note))
    }

    fun swipedItem(note: Note) = viewModelScope.launch {
        repository.deleteNote(note)
        noteEventChannel.send(NoteEvent.ShowUndoDeleteNote(note))
    }

    fun undoDeleteClick(note: Note) = viewModelScope.launch {
        repository.insetrNote(note)
    }

    fun onAddNewNoteClick() = viewModelScope.launch {
        noteEventChannel.send(NoteEvent.NavigateToAddNoteScreen)
    }

    fun onAddEditResult(result: Int) {
        when (result) {
            ADD_NOTE_RESULT_OK -> showTaskSavedConfirmationMessage(
                context.resources.getString(R.string.note_added)
            )
            EDIT_NOTE_RESULT_OK -> showTaskSavedConfirmationMessage(
                context.resources.getString(R.string.note_updated)
            )
        }
    }

    private fun showTaskSavedConfirmationMessage(message: String) = viewModelScope.launch {
        noteEventChannel.send(NoteEvent.ShowNoteSavedConfirmationMessage(message))
    }

    sealed class NoteEvent {
        object NavigateToAddNoteScreen : NoteEvent()
        data class NavigateToEditTaskScreen(val note: Note) : NoteEvent()
        data class ShowUndoDeleteNote(val note: Note) : NoteEvent()
        data class ShowNoteSavedConfirmationMessage(val msg: String) : NoteEvent()
    }
}