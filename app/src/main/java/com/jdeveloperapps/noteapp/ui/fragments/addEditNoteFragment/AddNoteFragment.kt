package com.jdeveloperapps.noteapp.ui.fragments.addEditNoteFragment

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
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.jdeveloperapps.noteapp.R
import com.jdeveloperapps.noteapp.databinding.FragmentAddNoteBinding
import com.jdeveloperapps.noteapp.utilites.Constants
import com.jdeveloperapps.noteapp.utilites.exhaustive
import com.jdeveloperapps.noteapp.utilites.setColorBackground
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_add_note.*
import kotlinx.coroutines.flow.collect
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.io.File


@AndroidEntryPoint
class AddNoteFragment : Fragment(R.layout.fragment_add_note), EasyPermissions.PermissionCallbacks {

    private val GALLERY_REQUEST = 1

    private lateinit var binding : FragmentAddNoteBinding

    private val viewModel: AddNoteViewModel by viewModels()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    private var dialogAddUrl: AlertDialog? = null
    private var dialogDeleteNote: AlertDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAddNoteBinding.bind(view)

        binding.apply {
            inputNoteTitle.setText(viewModel.noteTitle)
            inputNodeSubtitle.setText(viewModel.noteSubtitle)
            setColorBackground(viewSubtitleIndicator, viewModel.noteColorBackground)
            imageNote.isVisible = viewModel.imagePath.isNotEmpty()
            imageRemoveImage.isVisible = viewModel.imagePath.isNotEmpty()
            imageNote.load(Uri.fromFile(File(viewModel.imagePath)))
            inputNoteText.setText(viewModel.noteText)
            textDateTime.text = viewModel.note?.createDateFormattedString

            layoutWebUrl.isVisible = viewModel.webLink.isNotEmpty()
            textWebUrl.text = viewModel.webLink

            imageBack.setOnClickListener {
                viewModel.onBackClicked()
            }

            imageSave.setOnClickListener {
                viewModel.onSaveClicked()
            }

            inputNoteTitle.addTextChangedListener {
                viewModel.noteTitle = it.toString()
            }

            inputNodeSubtitle.addTextChangedListener {
                viewModel.noteSubtitle = it.toString()
            }

            imageRemoveImage.setOnClickListener {
                viewModel.onRemoveImageClick()
            }

            imageRemoveWebUrl.setOnClickListener {
                viewModel.onRemoveUrlClicked()
            }

            inputNoteText.addTextChangedListener {
                viewModel.noteText = it.toString()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addEditNoteEvent.collect { event ->
                when(event) {
                    AddNoteViewModel.AddEditNoteEvent.NavigateBack -> {
                        findNavController().popBackStack()
                    }
                    AddNoteViewModel.AddEditNoteEvent.RemoveImage -> {
                        binding.apply {
                            imageNote.visibility = View.GONE
                            imageRemoveImage.visibility = View.GONE
                        }
                    }
                    AddNoteViewModel.AddEditNoteEvent.RemoveUrl -> {
                        binding.layoutWebUrl.isVisible = false
                    }
                    is AddNoteViewModel.AddEditNoteEvent.ShowInvalidateInputMessage -> {
                        Toast.makeText(
                            requireContext(),
                            event.msg,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is AddNoteViewModel.AddEditNoteEvent.NavigateBackWithResult -> {
                        setFragmentResult(
                            "add_edit_request",
                            bundleOf("add_edit_result" to event.result)
                        )
                        findNavController().popBackStack()
                    }
                }.exhaustive
            }
        }

//        selectedNoteColor = currentNote.color

        initMiscellaneous()
    }

    private fun initMiscellaneous() {
//        val layoutMiscellaneous = binding.includeMiscellaneous.layoutMiscellaneous
        bottomSheetBehavior = BottomSheetBehavior.from(binding.includeMiscellaneous.root)
        binding.includeMiscellaneous.textMiscellaneous.setOnClickListener {
            if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                when (viewModel.noteColorBackground) {
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
            viewModel.noteColorBackground = "#333333"
            binding.includeMiscellaneous.apply {
                imageColor1.setImageResource(R.drawable.ic_done)
                imageColor2.setImageResource(0)
                imageColor3.setImageResource(0)
                imageColor4.setImageResource(0)
                imageColor5.setImageResource(0)
            }
            setSubtitleIndicatorColor("#333333")
        }

        binding.includeMiscellaneous.imageColor2.setOnClickListener {
            viewModel.noteColorBackground = "#FDBE3B"
            binding.includeMiscellaneous.apply {
                imageColor1.setImageResource(0)
                imageColor2.setImageResource(R.drawable.ic_done)
                imageColor3.setImageResource(0)
                imageColor4.setImageResource(0)
                imageColor5.setImageResource(0)
            }
            setSubtitleIndicatorColor("#FDBE3B")
        }

        binding.includeMiscellaneous.imageColor3.setOnClickListener {
            viewModel.noteColorBackground = "#FF4842"
            binding.includeMiscellaneous.apply {
                imageColor1.setImageResource(0)
                imageColor2.setImageResource(0)
                imageColor3.setImageResource(R.drawable.ic_done)
                imageColor4.setImageResource(0)
                imageColor5.setImageResource(0)
            }
            setSubtitleIndicatorColor("#FF4842")
        }

        binding.includeMiscellaneous.imageColor4.setOnClickListener {
            viewModel.noteColorBackground = "#3A52FC"
            binding.includeMiscellaneous.apply {
                imageColor1.setImageResource(0)
                imageColor2.setImageResource(0)
                imageColor3.setImageResource(0)
                imageColor4.setImageResource(R.drawable.ic_done)
                imageColor5.setImageResource(0)
            }
            setSubtitleIndicatorColor("#3A52FC")
        }

        binding.includeMiscellaneous.imageColor5.setOnClickListener {
            viewModel.noteColorBackground = "#000000"
            binding.includeMiscellaneous.apply {
                imageColor1.setImageResource(0)
                imageColor2.setImageResource(0)
                imageColor3.setImageResource(0)
                imageColor4.setImageResource(0)
                imageColor5.setImageResource(R.drawable.ic_done)
            }
            setSubtitleIndicatorColor("#000000")
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

    private fun setSubtitleIndicatorColor(color: String) {
        setColorBackground(binding.viewSubtitleIndicator, color)
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
                viewModel.imagePath = getPathFromUri(it)
                binding.imageRemoveImage.isVisible = true
                binding.imageNote.isVisible = true
                binding.imageNote.load(getPathFromUri(it))
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
//                    viewModel.deleteNote(currentNote)
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

}
