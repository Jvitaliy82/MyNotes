package com.jdeveloperapps.noteapp.ui.activities

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