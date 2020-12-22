package com.jdeveloperapps.noteapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jdeveloperapps.noteapp.R
import com.jdeveloperapps.noteapp.databinding.ItemContainerNoteBinding
import com.jdeveloperapps.noteapp.entities.Note

class NotesAdapter : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>(), Filterable {

    private var currentList = listOf<Note>()
    private var fullList = mutableListOf<Note>()

    private var onItemClickListener: ((note: Note) -> Unit)? = null

    class NotesViewHolder(val binding: ItemContainerNoteBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun setOnClickListener(listener: (note: Note) -> Unit) {
        onItemClickListener = listener
    }

    fun submitList(listUserItem: List<Note>) {
        currentList = listUserItem
        fullList.clear()
        fullList.addAll(listUserItem)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemContainerNoteBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_container_note, parent, false)
        return NotesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val currentItem = currentList[position]
        holder.binding.apply {
            noteItem = currentItem
            root.setOnClickListener {
                onItemClickListener?.let {
                    it(currentItem)
                }
            }
        }
    }

    override fun getItemCount() = currentList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                var filteredList = mutableListOf<Note>()
                if (charSequence.isNullOrEmpty()) {
                    filteredList.addAll(fullList)
                } else {
                    val filterPattern = charSequence.toString().trim()
                    filteredList = fullList.filter { note ->
                        note.title.contains(filterPattern, ignoreCase = true) ||
                                note.subtitle.contains(filterPattern, ignoreCase = true) ||
                                note.noteText.contains(filterPattern, ignoreCase = true)
                    }.toMutableList()
                }

                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, filteredResults: FilterResults?) {
                filteredResults?.let {
                    currentList = filteredResults.values as List<Note>
                }
                notifyDataSetChanged()
            }

        }
    }

}