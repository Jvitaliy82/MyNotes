package com.jdeveloperapps.noteapp.ui.fragments.listFragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.jdeveloperapps.noteapp.R
import com.jdeveloperapps.noteapp.adapters.NotesAdapter
import com.jdeveloperapps.noteapp.databinding.FragmentListBinding
import com.jdeveloperapps.noteapp.entities.Note
import com.jdeveloperapps.noteapp.utilites.exhaustive
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ListFragment : Fragment(R.layout.fragment_list), NotesAdapter.OnItemClickListener {

    private val viewModel: ListFragmentViewModel by viewModels()
    private val notesAdapter = NotesAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentListBinding.bind(view)

        binding.imageAddNoteMain.setOnClickListener {
            viewModel.onAddNewNoteClick()
        }

        binding.notesRecyclerView.apply {
            setHasFixedSize(true)
            adapter = notesAdapter
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val note = notesAdapter.currentList[viewHolder.adapterPosition]
                viewModel.swipedItem(note)
            }
        }).attachToRecyclerView(binding.notesRecyclerView)

        viewModel.notes.observe(viewLifecycleOwner, {
            notesAdapter.submitList(it)
        })

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.noteEvent.collect { event ->
                when (event) {
                    is ListFragmentViewModel.NoteEvent.NavigateToEditTaskScreen -> {
                        val action =
                            ListFragmentDirections.actionListFragmentToAddNoteFragment(event.note)
                        findNavController().navigate(action)
                    }
                    is ListFragmentViewModel.NoteEvent.ShowUndoDeleteNote -> {
                        Snackbar.make(requireView(), "Note deleted", Snackbar.LENGTH_LONG)
                            .setAction("UNDO") {
                                viewModel.undoDeleteClick(event.note)
                            }.show()
                    }
                    ListFragmentViewModel.NoteEvent.NavigateToAddNoteScreen -> {
                        val action = ListFragmentDirections.actionListFragmentToAddNoteFragment()
                        findNavController().navigate(action)
                    }
                    is ListFragmentViewModel.NoteEvent.ShowNoteSavedConfirmationMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                }.exhaustive
            }
        }

        binding.inputSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                charSequence?.let {
                    viewModel.searchQuery.value = it.toString()
                }
            }

            override fun afterTextChanged(searchText: Editable?) {

            }
        })

        setFragmentResultListener("add_edit_request") { _, bundle ->
            val result = bundle.getInt("add_edit_result")
            viewModel.onAddEditResult(result)
        }

        hideKeyboard()
    }

    override fun onItemClick(note: Note) {
        viewModel.onNoteSelected(note)
    }

    fun hideKeyboard() {
        val imm: InputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireActivity().window.decorView.windowToken, 0)
    }

}