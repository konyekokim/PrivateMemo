package com.chokus.konye.privatememo

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.WindowManager
import android.widget.*
import io.realm.*
import kotlinx.android.synthetic.main.content_note_list.*

import java.util.ArrayList
import java.util.Collections
import javax.inject.Inject
import kotlin.properties.Delegates

class NoteListActivity : AppCompatActivity() {
    private var noteListView: ListView by Delegates.notNull()
    private var noteAdapter: NoteAdapter? = null
    private var noteRecyclerAdapter : NoteRecyclerAdapter? = null
    private var noteClassList: List<NoteClass> by Delegates.notNull()
    private var notesList: MutableList<NoteClass> by Delegates.notNull()
    private var noteDrawerLayout: DrawerLayout? = null
    private var newNoteLayout: RelativeLayout? = null
    private var encryptLayout: RelativeLayout? = null
    private var saveToCloudLayout: RelativeLayout? = null
    private var logoutLayout: RelativeLayout? = null
    private var noteDrawerRelativeLayout: RelativeLayout? = null
    private var topMenuIcon: ImageView? = null
    @Inject lateinit var noteRealmManager : NoteRealmManager
    private lateinit var linearLayoutManager : LinearLayoutManager
    companion object {
        const val NOTE_TITLE = "com.chokus.konye.privatememo NoteTitle"
        const val NOTE_CONTENT = "com.chokus.konye.privatememo NoteContent"
        const val DATE_CREATED = "com.chokus.konye.privatememo DateCreated"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_list)
        setFullScreen()
        //initializing lateinit variables
        noteRealmManager = NoteRealmManager()
        linearLayoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        //noteClassList = noteRealmManager.findAll()
        notesList = noteRealmManager.findAll()
        viewActions()
        //scrollMyListViewToBottom()
        sideMenuWidgets()
    }

    private fun viewActions() {
        //noteListView = findViewById<View>(R.id.note_listView) as ListView
        note_recyclerView.layoutManager = linearLayoutManager
        //noteAdapter = NoteAdapter(applicationContext, R.layout.note_list_item, noteClassList)
        noteRecyclerAdapter = NoteRecyclerAdapter(notesList, this)
        note_recyclerView.adapter = noteRecyclerAdapter
        /*noteListView.adapter = noteAdapter
        noteListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val intent = Intent(applicationContext, NoteActivity::class.java)
            startActivity(intent)
        }*/
        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener {
            val intent = Intent(applicationContext, NoteActivity::class.java)
            intent.putExtra(NOTE_TITLE, "")
            intent.putExtra(NOTE_CONTENT, "")
            startActivity(intent)
        }
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
            val intent = Intent(this, NoteActivity::class.java)
            startActivity(intent)
        }
        encryptLayout!!.setOnClickListener {
            //do action in here
            //just to test the delete function
            noteRealmManager.deleteById(1)
        }
        saveToCloudLayout!!.setOnClickListener {
            //do action in here
        }
        logoutLayout!!.setOnClickListener {
            //let us use this to test the delete function
            noteRealmManager.deleteById(2)
            //adding this temporarily
            noteDrawerLayout!!.closeDrawer(noteDrawerRelativeLayout!!)
        }
    }

    private fun scrollMyListViewToBottom() {
        noteListView.post {
            // Select the last row so it will scroll into view...
            noteListView.setSelection(noteAdapter!!.count - 1)
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
