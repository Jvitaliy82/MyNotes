package com.jdeveloperapps.noteapp.ui.fragments.addEditNoteFragment

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdeveloperapps.noteapp.R
import com.jdeveloperapps.noteapp.entities.Note
import com.jdeveloperapps.noteapp.repositories.MainRepository
import com.jdeveloperapps.noteapp.ui.activities.ADD_NOTE_RESULT_OK
import com.jdeveloperapps.noteapp.ui.activities.EDIT_NOTE_RESULT_OK
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddNoteViewModel @ViewModelInject constructor(
    @ApplicationContext private val context: Context,
    private val repository: MainRepository,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {
    val note = state.get<Note>("note")

    private val addEditNoteEventChannel = Channel<AddEditNoteEvent>()
    val addEditNoteEvent = addEditNoteEventChannel.receiveAsFlow()

    var noteTitle = state.get<String>("noteTitle") ?: note?.title ?: ""
        set(value) {
            field = value
            state.set("noteTitle", value)
        }

    var noteSubtitle = state.get<String>("noteSubtitle") ?: note?.subtitle ?: ""
        set(value) {
            field = value
            state.set("noteSubtitle", value)
        }

    var noteColorBackground = state.get<String>("noteColorBackground") ?: note?.color ?: "#333333"
        set(value) {
            field = value
            state.set("noteColorBackground", value)
        }

    var imagePath = state.get<String>("imagePath") ?: note?.imagePath ?: ""
        set(value) {
            field = value
            state.set("imagePath", value)
        }

    var webLink = state.get<String>("webLink") ?: note?.webLink ?: ""
        set(value) {
            field = value
            state.set("webLink", value)
        }

    var noteText = state.get<String>("noteText") ?: note?.noteText ?: ""
        set(value) {
            field = value
            state.set("noteText", value)
        }

    fun onBackClicked() = viewModelScope.launch {
        addEditNoteEventChannel.send(AddEditNoteEvent.NavigateBack)
    }

    fun onRemoveImageClick() = viewModelScope.launch {
        imagePath = ""
        addEditNoteEventChannel.send(AddEditNoteEvent.RemoveImage)
    }

    fun onSaveClicked() {
        if (noteTitle.trim().isEmpty()) {
            showInvalidInputMessage(context.resources.getString(R.string.title_is_empty))
            return
        } else if (noteSubtitle.trim().isEmpty() && noteText.trim().isEmpty()) {
            showInvalidInputMessage(context.resources.getString(R.string.note_is_empty))
            return
        }

        if (note != null) {
            val updatedNote = note.copy(
                title = noteTitle,
                subtitle = noteSubtitle,
                webLink = webLink,
                imagePath = imagePath,
                noteText = noteText,
                color = noteColorBackground
            )
            updateNote(updatedNote)
        } else {
            val newNote = Note(
                title = noteTitle,
                subtitle = noteSubtitle,
                webLink = webLink,
                imagePath = imagePath,
                noteText = noteText,
                color = noteColorBackground
            )
            createNote(newNote)
        }

    }

    private fun createNote(note: Note) = viewModelScope.launch {
        repository.insetrNote(note)
        addEditNoteEventChannel.send(AddEditNoteEvent.NavigateBackWithResult(ADD_NOTE_RESULT_OK))
    }

    private fun updateNote(note: Note) = viewModelScope.launch {
        repository.updateNote(note)
        addEditNoteEventChannel.send(AddEditNoteEvent.NavigateBackWithResult(EDIT_NOTE_RESULT_OK))
    }

    fun onRemoveUrlClicked() = viewModelScope.launch {
        webLink = ""
        addEditNoteEventChannel.send(AddEditNoteEvent.RemoveUrl)
    }

    private fun showInvalidInputMessage(msg: String) = viewModelScope.launch {
        addEditNoteEventChannel.send(AddEditNoteEvent.ShowInvalidateInputMessage(msg))
    }

    sealed class AddEditNoteEvent {
        object NavigateBack : AddEditNoteEvent()
        object RemoveImage : AddEditNoteEvent()
        object RemoveUrl : AddEditNoteEvent()
        data class ShowInvalidateInputMessage(val msg: String) : AddEditNoteEvent()
        data class NavigateBackWithResult(val result: Int) : AddEditNoteEvent()
    }

}