package com.chokus.konye.privatememo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import io.realm.Realm
import io.realm.RealmConfiguration

class NoteActivity : AppCompatActivity() {
    val realm by lazy{Realm.getDefaultInstance()}
    private var noteTitleEditView: EditText? = null
    private var noteContentEditView: EditText? = null
    private var noteTitle: String? = null
    private var noteContent: String? = null
    private var saveNoteButton: Button? = null
    private var note: NoteClass? = null
    private var dateCreated: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        Realm.init(this)
        val config = RealmConfiguration.Builder().name("notes.realm").build()
        Realm.setDefaultConfiguration(config)
        note = NoteClass()
        setFullScreen()
        receivingStringExtras()
        viewActions()
    }

    private fun viewActions() {
        noteTitleEditView = findViewById<View>(R.id.note_title_editView) as EditText
        noteContentEditView = findViewById<View>(R.id.note_content_editView) as EditText
        saveNoteButton = findViewById<View>(R.id.save_note_button) as Button
        noteTitleEditView!!.setText(noteTitle)
        noteContentEditView!!.setText(noteContent)
        saveNoteButton!!.setOnClickListener {
            dateCreated = note!!.creationDate()
            saveMemo(dateCreated)
            val intent = Intent(applicationContext, NoteListActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun saveMemo(dateCreated: String?) {
        val title = noteTitleEditView!!.text.toString().trim { it <= ' ' }
        val content = noteContentEditView!!.text.toString().trim { it <= ' ' }
        realm.executeTransactionAsync({ bgRealm ->
            val noteClass = bgRealm.createObject(NoteClass::class.java)
            noteClass.noteTitle = title
            noteClass.noteContent = content
            noteClass.dateCreated = dateCreated
        }, {
            // Transaction was a success.
            Toast.makeText(applicationContext, "Successfully Saved", Toast.LENGTH_LONG).show()
        }) {
            // Transaction failed and was automatically canceled.
            Toast.makeText(applicationContext, "Something went wrong, failure in saving", Toast.LENGTH_LONG).show()
        }
    }

    private fun receivingStringExtras() {
        noteTitle = intent.getStringExtra(NoteListActivity.NOTE_TITLE)
        noteContent = intent.getStringExtra(NoteListActivity.NOTE_CONTENT)
    }

    private fun setFullScreen() {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    override fun onDestroy() {
        super.onDestroy()
        realm!!.close()
    }
}
