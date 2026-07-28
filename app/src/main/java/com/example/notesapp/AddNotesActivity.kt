package com.example.notesapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notesapp.databinding.ActivityAddNotesBinding

class AddNotesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNotesBinding
    private lateinit var db : NotesDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NotesDatabaseHelper(this)

        binding.btnSave.setOnClickListener {
            //Store user data
            val title = binding.etTitle.text.toString()
            val content = binding.etContent.text.toString()
            val note = Note(0, title, content)

            //Call the Database Helper Class
            db.insertNote(note)

            //User response
            Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show()

            //Activity is finished
            finish()


        }
    }
}