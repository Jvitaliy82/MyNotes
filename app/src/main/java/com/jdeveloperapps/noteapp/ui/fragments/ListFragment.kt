package com.jdeveloperapps.noteapp.ui.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.jdeveloperapps.noteapp.R
import com.jdeveloperapps.noteapp.adapters.NotesAdapter
import com.jdeveloperapps.noteapp.databinding.FragmentListBinding
import com.jdeveloperapps.noteapp.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

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
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        binding.imageAddNoteMain.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addNoteFragment)
        }

        binding.notesRecyclerView.apply {
            setHasFixedSize(true)
            adapter = notesAdapter.apply {
                setOnClickListener { note ->
                    val bundle = Bundle().apply {
                        putSerializable("note", note)
                    }
                    findNavController().navigate(
                        R.id.action_listFragment_to_addNoteFragment,
                        bundle
                    )
                }
            }
        }

        viewModel.notes.observe(viewLifecycleOwner, {
            notesAdapter.submitList(it)
        })

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

        hideKeyboard()
    }

    fun hideKeyboard() {
        val imm: InputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireActivity().window.decorView.windowToken, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}