package com.jdeveloperapps.noteapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jdeveloperapps.noteapp.R
import com.jdeveloperapps.noteapp.databinding.ItemContainerNoteBinding
import com.jdeveloperapps.noteapp.entities.Note

class NotesAdapter : ListAdapter<Note, NotesAdapter.NotesViewHolder>(DiffCallback()) {

    private var onItemClickListener: ((note: Note) -> Unit)? = null

    inner class NotesViewHolder(val binding: ItemContainerNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.let {
                        val note = getItem(position)
                        it(note)
                    }
                }
            }
        }

        fun bind(note: Note) {
            binding.noteItem = note
        }
    }

    fun setOnClickListener(listener: (note: Note) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemContainerNoteBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_container_note, parent, false)
        return NotesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val currentItem = currentList[position]
        holder.bind(currentItem)
    }

    class DiffCallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note) =
            oldItem.id == newItem.id


        override fun areContentsTheSame(oldItem: Note, newItem: Note) =
            oldItem == newItem

    }
}