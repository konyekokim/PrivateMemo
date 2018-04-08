package com.chokus.konye.privatememo.activity

import android.app.Dialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import com.chokus.konye.privatememo.datamodel.NoteClass
import com.chokus.konye.privatememo.datamanager.NoteRealmManager
import com.chokus.konye.privatememo.adapter.NoteRecyclerAdapter
import com.chokus.konye.privatememo.R
import kotlinx.android.synthetic.main.save_note_dialog.*

import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class NoteActivity : AppCompatActivity() {
    private var noteTitleEditView: EditText? = null
    private var noteContentEditView: EditText? = null
    private var noteTitle: String? = null
    private var noteContent: String? = null
    private var checkImgView : ImageView? = null
    private var noteDrawerLayout: DrawerLayout? = null
    private var noteDrawerRelativeLayout: RelativeLayout? = null
    private var topMenuIcon: ImageView? = null
    private var saveNoteDialog: Dialog? = null
    private var noteAdapter: NoteRecyclerAdapter? = null
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
        sideMenuWidgets()
    }

    private fun viewActions() {
        noteTitleEditView = findViewById<View>(R.id.note_title_editView) as EditText
        noteContentEditView = findViewById<View>(R.id.note_content_editView) as EditText
        checkImgView = findViewById<View>(R.id.check_imageView) as ImageView
        noteTitleEditView!!.setText(noteTitle)
        noteContentEditView!!.setText(noteContent)
        checkImgView!!.setOnClickListener {
            SaveNoteDialog()
            saveNoteDialog!!.show()
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

    private fun sideMenuWidgets() {
        noteDrawerLayout = findViewById<View>(R.id.note_drawer_layout) as DrawerLayout
        noteDrawerRelativeLayout = findViewById<View>(R.id.note_relative_drawer_layout) as RelativeLayout
        topMenuIcon = findViewById<View>(R.id.top_menu_icon) as ImageView
        topMenuIcon!!.setOnClickListener { noteDrawerLayout!!.openDrawer(noteDrawerRelativeLayout!!) }
    }

    private fun SaveNoteDialog(){
        saveNoteDialog = Dialog(this)
        saveNoteDialog!!.setContentView(R.layout.save_note_dialog)
        saveNoteDialog!!.setCancelable(false)
        saveNoteDialog!!.dialog_save_editText.text = noteTitleEditView!!.text
        saveNoteDialog!!.dialog_save_button.setOnClickListener {
            //do whatever function you want here.
            val dateCreated : String = creationDate()
            noteTitleEditView!!.text = saveNoteDialog!!.dialog_save_editText.text
            saveMemo(dateCreated)
            val intent = Intent(applicationContext, NoteListActivity::class.java)
            startActivity(intent)
            saveNoteDialog!!.cancel()
            finish()
        }
        saveNoteDialog!!.dialog_save_cancel_button.setOnClickListener {
            saveNoteDialog!!.cancel()
        }
    }

    private fun setFullScreen() {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
