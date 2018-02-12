package com.chokus.konye.privatememo

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Created by omen on 12/02/2018.
 */
class NoteRecyclerAdapter(var noteList : List<NoteClass>) : RecyclerView.Adapter<NoteRecyclerAdapter.MyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.note_list_item, parent, false)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {
        val notes: NoteClass = noteList[position]
        holder?.noteTitleTextView?.text = notes.noteTitle
        holder?.noteContentTextView?.text = notes.noteContent
        holder?.noteDateCreatedTextView?.text = notes.dateCreated
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val noteTitleTextView = itemView.findViewById(R.id.note_title_textView) as TextView
        val noteContentTextView = itemView.findViewById(R.id.note_content_textView) as TextView
        val noteDateCreatedTextView = itemView.findViewById(R.id.note_date_created_textView) as TextView
    }

}