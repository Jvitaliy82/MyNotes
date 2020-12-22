package com.jdeveloperapps.noteapp.ui.fragments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.jdeveloperapps.noteapp.R
import com.jdeveloperapps.noteapp.databinding.FragmentAddNoteBinding
import com.jdeveloperapps.noteapp.entities.Note
import com.jdeveloperapps.noteapp.utilites.Constants
import com.jdeveloperapps.noteapp.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_add_note.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class AddNoteFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private val GALLERY_REQUEST = 1
    private val CURRENT_NOTE = "currentNote"

    private var _binding: FragmentAddNoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var selectedNoteColor: String

    private var dialogAddUrl: AlertDialog? = null
    private var dialogDeleteNote: AlertDialog? = null

    private val args: AddNoteFragmentArgs by navArgs()
    private lateinit var currentNote: Note

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)


        currentNote = args.note ?: Note().apply {
            dateTime = SimpleDateFormat(
                "EEE, dd MMMM yyyy HH:mm",
                Locale.getDefault()
            ).format(Date())
        }

        savedInstanceState?.let {
            currentNote = it.getSerializable(CURRENT_NOTE) as Note
        }

        binding.note = currentNote

        binding.imageBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.imageSave.setOnClickListener {
            if (saveNote()) {
                findNavController().popBackStack()
            }
        }

        binding.imageRemoveWebUrl.setOnClickListener {
            currentNote.webLink = ""
            binding.invalidateAll()
        }

        binding.imageRemoveImage.setOnClickListener {
            currentNote.imagePath = ""
            binding.invalidateAll()
        }

        selectedNoteColor = currentNote.color

        initMiscellaneous()
    }

    private fun saveNote(): Boolean {
        if (inputNoteTitle.text.toString().trim().isEmpty()) {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.title_is_empty),
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (inputNodeSubtitle.text.toString().trim().isEmpty() &&
            inputNote.text.toString().trim().isEmpty()
        ) {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.title_is_empty),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        updateCurrentNote()

        viewModel.saveNote(note = currentNote)
        return true

    }

    private fun updateCurrentNote() {
        currentNote.apply {
            title = binding.inputNoteTitle.text.toString()
            subtitle = binding.inputNodeSubtitle.text.toString()
            noteText = binding.inputNote.text.toString()
            dateTime = binding.textDateTime.text.toString()
            color = selectedNoteColor
            webLink = binding.textWebUrl.text.toString()
        }
        binding.invalidateAll()
    }

    private fun initMiscellaneous() {
        val layoutMiscellaneous = binding.includeMiscellaneous.layoutMiscellaneous
        bottomSheetBehavior = BottomSheetBehavior.from(layoutMiscellaneous)
        binding.includeMiscellaneous.textMiscellaneous.setOnClickListener {
            if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                when (currentNote.color) {
                    "#FDBE3B" -> binding.includeMiscellaneous.imageColor2.performClick()
                    "#FF4842" -> binding.includeMiscellaneous.imageColor3.performClick()
                    "#3A52FC" -> binding.includeMiscellaneous.imageColor4.performClick()
                    "#000000" -> binding.includeMiscellaneous.imageColor5.performClick()
                }
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        binding.includeMiscellaneous.imageColor1.setOnClickListener {
            selectedNoteColor = "#333333"
            binding.includeMiscellaneous.apply {
                imageColor1.setImageResource(R.drawable.ic_done)
                imageColor2.setImageResource(0)
                imageColor3.setImageResource(0)
                imageColor4.setImageResource(0)
                imageColor5.setImageResource(0)
            }
            setSubtitleIndicatorColor()
        }

        binding.includeMiscellaneous.imageColor2.setOnClickListener {
            selectedNoteColor = "#FDBE3B"
            binding.includeMiscellaneous.apply {
                imageColor1.setImageResource(0)
                imageColor2.setImageResource(R.drawable.ic_done)
                imageColor3.setImageResource(0)
                imageColor4.setImageResource(0)
                imageColor5.setImageResource(0)
            }
            setSubtitleIndicatorColor()
        }

        binding.includeMiscellaneous.imageColor3.setOnClickListener {
            selectedNoteColor = "#FF4842"
            binding.includeMiscellaneous.apply {
                imageColor1.setImageResource(0)
                imageColor2.setImageResource(0)
                imageColor3.setImageResource(R.drawable.ic_done)
                imageColor4.setImageResource(0)
                imageColor5.setImageResource(0)
            }
            setSubtitleIndicatorColor()
        }

        binding.includeMiscellaneous.imageColor4.setOnClickListener {
            selectedNoteColor = "#3A52FC"
            binding.includeMiscellaneous.apply {
                imageColor1.setImageResource(0)
                imageColor2.setImageResource(0)
                imageColor3.setImageResource(0)
                imageColor4.setImageResource(R.drawable.ic_done)
                imageColor5.setImageResource(0)
            }
            setSubtitleIndicatorColor()
        }

        binding.includeMiscellaneous.imageColor5.setOnClickListener {
            selectedNoteColor = "#000000"
            binding.includeMiscellaneous.apply {
                imageColor1.setImageResource(0)
                imageColor2.setImageResource(0)
                imageColor3.setImageResource(0)
                imageColor4.setImageResource(0)
                imageColor5.setImageResource(R.drawable.ic_done)
            }
            setSubtitleIndicatorColor()
        }

        binding.includeMiscellaneous.layoutAddImage.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            getImageForNote()
        }

        binding.includeMiscellaneous.layoutAddUrl.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            showAddUrlDialog()
        }

        binding.includeMiscellaneous.layoutDeleteNote.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            showDeleteNoteDialog()
        }


    }

    private fun setSubtitleIndicatorColor() {
        updateCurrentNote()
        binding.invalidateAll()
    }

    private fun getImageForNote() {
        if (hasPermissions()) {
            val photoPickerIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activity?.let {
                if (photoPickerIntent.resolveActivity(it.packageManager) != null) {
                    startActivityForResult(photoPickerIntent, GALLERY_REQUEST)
                }
            }
        } else {
            requestPermission()
        }
    }

    private fun hasPermissions(): Boolean {
        return EasyPermissions.hasPermissions(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    private fun requestPermission() {
        EasyPermissions.requestPermissions(
            this,
            requireContext().resources.getString(R.string.require_permission_read_storage),
            Constants.REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSIONS,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImage: Uri? = data.data
            selectedImage?.let {
                currentNote.imagePath = getPathFromUri(it)
                binding.invalidateAll()
            }
        }
    }

    private fun getPathFromUri(contentUri: Uri): String {
        var filePath = ""
        var cursor = activity?.contentResolver?.query(contentUri, null, null, null, null)
        if (cursor == null) {
            filePath = contentUri.path.toString()
        } else {
            cursor.moveToFirst()
            val index = cursor.getColumnIndex("_data")
            filePath = cursor.getString(index)
            cursor.close()
        }
        return filePath
    }

    private fun showAddUrlDialog() {
        if (dialogAddUrl == null) {
            val builder = AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme)
            val view = LayoutInflater.from(requireContext())
                .inflate(
                    R.layout.layout_add_url_dialog,
                    activity?.findViewById<ConstraintLayout>(R.id.layoutAddUrlContainer)
                )
            builder.setView(view)
            dialogAddUrl = builder.create()
            dialogAddUrl?.let { dialog ->
                dialog.window?.setBackgroundDrawable(ColorDrawable(0))

                val inputUrl = view.findViewById<EditText>(R.id.inputUrl)
                inputUrl.requestFocus()

                view.findViewById<TextView>(R.id.textAdd).setOnClickListener {
                    if (inputUrl.text.toString().trim().isEmpty()) {
                        Toast.makeText(
                            requireContext(),
                            resources.getString(R.string.enter_url),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (!Patterns.WEB_URL.matcher(inputUrl.text.toString()).matches()) {
                        Toast.makeText(
                            requireContext(),
                            resources.getString(R.string.enter_valid_url),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        textWebUrl.text = inputUrl.text.toString()
                        layoutWebUrl.visibility = View.VISIBLE
                        dialog.dismiss()
                    }
                }

                view.findViewById<TextView>(R.id.textCancel).setOnClickListener {
                    dialog.dismiss()
                }

            }
        }
        dialogAddUrl?.show()
    }

    private fun showDeleteNoteDialog() {
        if (dialogDeleteNote == null) {
            val builder = AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme)
            val view = LayoutInflater.from(requireContext())
                .inflate(
                    R.layout.layout_delete_note_dialog,
                    activity?.findViewById<ConstraintLayout>(R.id.layoutDeleteNoteContainer)
                )
            builder.setView(view)
            dialogDeleteNote = builder.create()
            dialogDeleteNote?.let { dialog ->
                dialog.window?.setBackgroundDrawable(ColorDrawable(0))

                view.findViewById<TextView>(R.id.textDelete).setOnClickListener {
                    viewModel.deleteNote(currentNote)
                    dialog.dismiss()
                    findNavController().popBackStack()
                }

                view.findViewById<TextView>(R.id.textCancel).setOnClickListener {
                    dialog.dismiss()
                }

            }
        }
        dialogDeleteNote?.show()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        getImageForNote()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
        Toast.makeText(requireContext(), "Извините дальше никак", Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        updateCurrentNote()
        outState.putSerializable(CURRENT_NOTE, currentNote)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
