package com.jdeveloperapps.noteapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jdeveloperapps.noteapp.R
import com.jdeveloperapps.noteapp.databinding.ItemContainerNoteBinding
import com.jdeveloperapps.noteapp.entities.Note

class NotesAdapter : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    class NotesViewHolder(val binding: ItemContainerNoteBinding) : RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemContainerNoteBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_container_note, parent, false)
        return NotesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        holder.binding.apply {
            noteItem = currentItem
        }
    }

    override fun getItemCount() = differ.currentList.size

}