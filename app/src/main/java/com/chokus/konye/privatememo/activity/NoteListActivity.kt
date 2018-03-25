package com.chokus.konye.privatememo.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.chokus.konye.privatememo.datamodel.NoteClass
import com.chokus.konye.privatememo.datamanager.NoteRealmManager
import com.chokus.konye.privatememo.adapter.NoteRecyclerAdapter
import com.chokus.konye.privatememo.R
import com.chokus.konye.privatememo.Util.Utility
import kotlinx.android.synthetic.main.content_note_list.*

import javax.inject.Inject
import kotlin.properties.Delegates

class NoteListActivity : AppCompatActivity() {
    private var noteListView: ListView by Delegates.notNull()
    private var noteRecyclerAdapter : NoteRecyclerAdapter? = null
    private var notesList: MutableList<NoteClass> = mutableListOf<NoteClass>()
    private var noteDrawerLayout: DrawerLayout? = null
    private var newNoteLayout: RelativeLayout? = null
    private var encryptLayout: RelativeLayout? = null
    private var saveToCloudLayout: RelativeLayout? = null
    private var logoutLayout: RelativeLayout? = null
    private var noteDrawerRelativeLayout: RelativeLayout? = null
    private var topMenuIcon: ImageView? = null
    @Inject lateinit var noteRealmManager : NoteRealmManager
    private lateinit var linearLayoutManager : LinearLayoutManager
    private lateinit var gridLayoutManager: GridLayoutManager
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
        //this gridLayoutManager with number of columns included
        val noOfColumns : Int = Utility.calculateNoOfColumns(this)
        gridLayoutManager = GridLayoutManager(this, noOfColumns)
        //linearLayoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        //noteClassList = noteRealmManager.findAll()
        notesList = noteRealmManager.findAll()
        viewActions()
        //scrollMyListViewToBottom()
        sideMenuWidgets()
        //add a realmchange listener
    }

    private fun viewActions() {
        note_recyclerView.layoutManager = gridLayoutManager
        noteRecyclerAdapter = NoteRecyclerAdapter(notesList, this)
        note_recyclerView.adapter = noteRecyclerAdapter
        noteRecyclerAdapter!!.notifyDataSetChanged()
        new_note_imageView.setOnClickListener {
            val intent = Intent(applicationContext, NoteActivity::class.java)
            intent.putExtra(NOTE_TITLE, "")
            intent.putExtra(NOTE_CONTENT, "")
            startActivity(intent)
        }
        search_imgView.setOnClickListener {
            search_imgView.visibility = View.GONE
            search_box_relativeLayout.visibility = View.VISIBLE
        }
        cancel_imgView.setOnClickListener {
            search_box_relativeLayout.visibility = View.GONE
            search_imgView.visibility = View.VISIBLE
        }
        removeBackground()
    }

    private fun removeBackground(){
        if(notesList.isEmpty()){
            noteList_bg.visibility = View.VISIBLE
        }else{
            noteList_bg.visibility = View.GONE
        }
    }

    private fun sideMenuWidgets() {
        noteDrawerLayout = findViewById<View>(R.id.note_drawer_layout) as DrawerLayout
        noteDrawerRelativeLayout = findViewById<View>(R.id.note_relative_drawer_layout) as RelativeLayout
        topMenuIcon = findViewById<View>(R.id.top_menu_icon) as ImageView
        topMenuIcon!!.setOnClickListener { noteDrawerLayout!!.openDrawer(noteDrawerRelativeLayout!!) }
    }

    private fun setFullScreen() {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}

