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
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class NoteActivity : AppCompatActivity() {
    private var noteTitleEditView: EditText? = null
    private var noteContentEditView: EditText? = null
    private var noteTitle: String? = null
    private var noteContent: String? = null
    private var saveNoteButton: Button? = null
    private var note: NoteClass? = null
    var noteId = -1L
    @Inject lateinit var noteRealmManager : NoteRealmManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        noteRealmManager = NoteRealmManager()
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
            val dateCreated : String = creationDate()
            saveMemo(dateCreated)
            val intent = Intent(applicationContext, NoteListActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    fun creationDate(): String {
        val myDate = Date()
        val calendar = Calendar.getInstance()
        calendar.timeZone = TimeZone.getTimeZone("UTC")
        calendar.time = myDate
        val time = calendar.time
        val outputFmt = SimpleDateFormat("MMM dd, yyy h:mm a zz")
        val dateCreated: String= outputFmt.format(time)
        return dateCreated
    }

    private fun saveMemo(dateCreated: String) {
        val title = noteTitleEditView!!.text.toString().trim { it <= ' ' }
        val content = noteContentEditView!!.text.toString().trim { it <= ' ' }
        noteRealmManager.insert(title, content, dateCreated)
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
    }
}
