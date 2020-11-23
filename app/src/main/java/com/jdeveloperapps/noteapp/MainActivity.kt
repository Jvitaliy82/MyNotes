package com.jdeveloperapps.noteapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val REQUEST_CODE_ADD_NOTE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageAddNoteMain.setOnClickListener {
            startActivityForResult(Intent(applicationContext, CreateNoteActivity::class.java),
            REQUEST_CODE_ADD_NOTE)
        }
    }
}