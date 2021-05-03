package com.jdeveloperapps.noteapp.ui.activities

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jdeveloperapps.noteapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}

const val ADD_NOTE_RESULT_OK = Activity.RESULT_FIRST_USER
const val EDIT_NOTE_RESULT_OK = Activity.RESULT_FIRST_USER + 1