package com.example.notesapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper



class NotesDatabaseHelper (context: Context) : SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION){

    companion object{
        private const val DATABASE_NAME = "notesapp.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "notes"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_CONTENT = "content"
    }
    override fun onCreate(db: SQLiteDatabase?) {
      val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_TITLE TEXT, $COLUMN_CONTENT TEXT)".trimIndent()
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    //Create
    fun insertNote(note: Note){

        //allows for database modification
        val db = writableDatabase
        val values = ContentValues().apply{
            put(COLUMN_TITLE,note.title)
            put(COLUMN_CONTENT, note.content)
        }
        db.insert(TABLE_NAME,null,values)
        db.close()
    }

    //Reading All the Notes
    fun getAllNotes(): List<Note> {
        val db = readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectQuery, null)

        val notes = mutableListOf<Note>()

        if (cursor.moveToFirst()) {
            do {
                val note = Note(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                    content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
                )

                notes.add(note)

            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return notes
    }
    //Update
    fun updateNotes(note: Note){
      val db = writableDatabase
      val values = ContentValues().apply{
         put(COLUMN_TITLE, note.title)
         put(COLUMN_CONTENT, note.content)
     }
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(note.id.toString())

        db.update(TABLE_NAME,values,whereClause,whereArgs)
        db.close()
    }

    fun getNoteByID(noteId: Int): Note{
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $noteId"
        val cursor = db.rawQuery(query,null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
        val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))

        cursor.close()
        db.close()
        return Note(id,title,content)
    }
    //Delete
    fun deleteNote(id: Int): Boolean{
        val db = writableDatabase

        val success = db.delete(TABLE_NAME,"$COLUMN_ID=?",arrayOf(id.toString())).toLong()
        db.close()

        return Integer.parseInt("$success") != -1
    }
}