package com.chokus.konye.privatememo

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ListView
import android.widget.RelativeLayout
import io.realm.*

import java.util.ArrayList
import java.util.Collections

class NoteListActivity : AppCompatActivity() {
    private val realm by lazy {Realm.getDefaultInstance()}
    private var noteListView: ListView? = null
    private var noteAdapter: NoteAdapter? = null
    private var noteClassArrayList: ArrayList<NoteClass>? = null
    private var noteDrawerLayout: DrawerLayout? = null
    private var newNoteLayout: RelativeLayout? = null
    private var encryptLayout: RelativeLayout? = null
    private var saveToCloudLayout: RelativeLayout? = null
    private var logoutLayout: RelativeLayout? = null
    private var noteDrawerRelativeLayout: RelativeLayout? = null
    private var topMenuIcon: ImageView? = null
    private var realmChangeListener: RealmChangeListener<Realm>? = null

    companion object {
        const val NOTE_TITLE = "com.chokus.konye.privatememo NoteTitle"
        const val NOTE_CONTENT = "com.chokus.konye.privatememo NoteContent"
        const val DATE_CREATED = "com.chokus.konye.privatememo DateCreated"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_list)
        Realm.init(this)
        val config = RealmConfiguration.Builder().name("notes.realm").build()
        Realm.setDefaultConfiguration(config)
        setFullScreen()
        viewActions()
        realmChangeListener = RealmChangeListener {
            noteAdapter = NoteAdapter(applicationContext, R.layout.note_list_item, retrieveFromDb())
            noteListView!!.adapter = noteAdapter
        }
        scrollMyListViewToBottom()
        sideMenuWidgets()
    }

    private fun viewActions() {
        noteListView = findViewById<View>(R.id.note_listView) as ListView
        noteAdapter = NoteAdapter(applicationContext, R.layout.note_list_item, retrieveFromDb())
        noteListView!!.adapter = noteAdapter
        noteListView!!.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val intent = Intent(applicationContext, NoteActivity::class.java)
            intent.putExtra(NOTE_TITLE, retrieveFromDb()[position].noteTitle)
            intent.putExtra(NOTE_CONTENT, retrieveFromDb()[position].noteContent)
            intent.putExtra(DATE_CREATED, retrieveFromDb()[position].dateCreated)
            startActivity(intent)
        }
        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener {
            val intent = Intent(applicationContext, NoteActivity::class.java)
            intent.putExtra(NOTE_TITLE, "")
            intent.putExtra(NOTE_CONTENT, "")
            startActivity(intent)
        }
    }

    private fun retrieveFromDb(): ArrayList<NoteClass> {
        val notes = ArrayList<NoteClass>()
        try {
            val noteClass : RealmResults<NoteClass>? = realm.where(NoteClass::class.java).findAll()
            noteClass!!.forEach{
                notes.addAll(0, realm!!.copyFromRealm(noteClass))
            }
        } finally {
            if (realm != null) {
                realm!!.close()
            }
        }
        return notes
    }

    private fun sideMenuWidgets() {
        noteDrawerLayout = findViewById<View>(R.id.note_drawer_layout) as DrawerLayout
        noteDrawerRelativeLayout = findViewById<View>(R.id.note_relative_drawer_layout) as RelativeLayout
        newNoteLayout = findViewById<View>(R.id.new_note_side_layout) as RelativeLayout
        encryptLayout = findViewById<View>(R.id.encrypt_side_layout) as RelativeLayout
        saveToCloudLayout = findViewById<View>(R.id.cloud_save_side_layout) as RelativeLayout
        logoutLayout = findViewById<View>(R.id.logout_side_layout) as RelativeLayout
        topMenuIcon = findViewById<View>(R.id.top_menu_icon) as ImageView
        topMenuIcon!!.setOnClickListener { noteDrawerLayout!!.openDrawer(noteDrawerRelativeLayout!!) }
        newNoteLayout!!.setOnClickListener {
            //do action in here
        }
        encryptLayout!!.setOnClickListener {
            //do action in here
        }
        saveToCloudLayout!!.setOnClickListener {
            //do action in here
        }
        logoutLayout!!.setOnClickListener {
            //do action in here
        }
    }

    private fun scrollMyListViewToBottom() {
        noteListView!!.post {
            // Select the last row so it will scroll into view...
            noteListView!!.setSelection(noteAdapter!!.count - 1)
        }
    }

    private fun setFullScreen() {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    override fun onDestroy() {
        super.onDestroy()
        realm!!.removeChangeListener(realmChangeListener!!)
        realm!!.close()
    }
}
