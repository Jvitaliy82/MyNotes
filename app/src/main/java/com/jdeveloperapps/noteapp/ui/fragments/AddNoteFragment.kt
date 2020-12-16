package com.jdeveloperapps.noteapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.jdeveloperapps.noteapp.R
import com.jdeveloperapps.noteapp.databinding.FragmentAddNoteBinding
import com.jdeveloperapps.noteapp.entities.Note
import com.jdeveloperapps.noteapp.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_add_note.*
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddNoteFragment : Fragment() {

    private var _binding: FragmentAddNoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        binding.imageBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.textDateTime.text = SimpleDateFormat("EEE, dd MMMM yyyy HH:hh a", Locale.getDefault())
            .format(Date())

        binding.imageSave.setOnClickListener {
            saveNote()
            findNavController().popBackStack()
        }
    }

    fun saveNote() {
        if (inputNoteTitle.text.toString().trim().isEmpty()) {
            Toast.makeText(requireContext(), resources.getString(R.string.title_is_empty), Toast.LENGTH_SHORT).show()
            return
        } else if (inputNodeSubtitle.text.toString().trim().isEmpty() &&
            inputNote.text.toString().trim().isEmpty()) {
            Toast.makeText(requireContext(), resources.getString(R.string.title_is_empty), Toast.LENGTH_SHORT).show()
            return
        }

        val note = Note(
            title = binding.inputNoteTitle.text.toString(),
            subtitle = binding.inputNodeSubtitle.text.toString(),
            noteText = binding.inputNote.text.toString(),
            dateTime = binding.textDateTime.text.toString()
        )

        viewModel.saveNote(note = note)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}