package com.jdeveloperapps.noteapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.jdeveloperapps.noteapp.R
import com.jdeveloperapps.noteapp.adapters.NotesAdapter
import com.jdeveloperapps.noteapp.databinding.FragmentListBinding
import com.jdeveloperapps.noteapp.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_list.*

@AndroidEntryPoint
class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    private val notesAdapter = NotesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        imageAddNoteMain.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addNoteFragment)
        }

        binding.notesRecyclerView.apply {
            setHasFixedSize(true)
            adapter = notesAdapter
        }

        viewModel.getSaveNotes().observe(viewLifecycleOwner, {
            notesAdapter.differ.submitList(it)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}