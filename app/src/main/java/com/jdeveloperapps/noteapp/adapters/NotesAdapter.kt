package com.jdeveloperapps.noteapp.adapters

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.jdeveloperapps.noteapp.databinding.ItemContainerNoteBinding
import com.jdeveloperapps.noteapp.entities.Note
import java.io.File

class NotesAdapter(private val listener: OnItemClickListener) :
    ListAdapter<Note, NotesAdapter.NotesViewHolder>(DiffCallback()) {

    inner class NotesViewHolder(private val binding: ItemContainerNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val note = getItem(position)
                    listener.onItemClick(note)
                }
            }
        }

        fun bind(note: Note) {
            binding.apply {
                val gradientDrawable = layoutNote.background as GradientDrawable
                gradientDrawable.setColor(Color.parseColor(note.color))

                imageNote.load(Uri.fromFile(File(note.imagePath)))
                textTitle.text = note.title
                textSubtitle.text = note.subtitle
                textDateTime.text = note.createDateFormattedString
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(note: Note)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val binding = ItemContainerNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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