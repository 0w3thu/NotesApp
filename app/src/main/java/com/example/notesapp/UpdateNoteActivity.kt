package com.example.notesapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notesapp.databinding.ActivityUpdateNoteBinding

class UpdateNoteActivity : AppCompatActivity() {

   private lateinit var binding : ActivityUpdateNoteBinding
   private lateinit var db : NotesDatabaseHelper

   //-1 shows that the ID is empty
   private var noteId : Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NotesDatabaseHelper(this)
        noteId = intent.getIntExtra("note_id",-1)

        if(noteId == -1){
            finish()
            return
        }

        val note = db.getNoteByID(noteId)
        binding.etpUpdateTitle.setText(note.title)
        binding.etUpdateContent.setText(note.content)

        binding.btnUpdateSave.setOnClickListener {
            val newTitle = binding.etpUpdateTitle.text.toString()
            val newContent = binding.etUpdateContent.text.toString()
            val updateNote = Note(noteId,newTitle,newContent)

            db.updateNotes(updateNote)
            finish()

            Toast.makeText(this,"Changes Saved", Toast.LENGTH_SHORT).show()
        }

    }
}