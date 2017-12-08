package com.chokus.konye.privatememo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView

import java.util.ArrayList

/**
 * Created by ALPHA AND JAM on 11/22/2017.
 */

class NoteAdapter(context: Context, resources: Int,  var noteClassArrayList: ArrayList<NoteClass>) : ArrayAdapter<NoteClass>(context, resources, noteClassArrayList) {


    override fun getView(position: Int, convertView: View?, container: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.note_list_item, null)
        }
        val noteClass = getItem(position)
        val noteTitleText = convertView!!.findViewById<View>(R.id.note_title_textView) as TextView
        val noteContentText = convertView.findViewById<View>(R.id.note_content_textView) as TextView
        val noteDateCreated = convertView.findViewById<View>(R.id.note_date_created_textView) as TextView
        noteTitleText.text = noteClass!!.noteTitle
        noteContentText.text = noteClass.noteContent
        noteDateCreated.text = noteClass.dateCreated
        return convertView
    }
}
