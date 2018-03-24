package com.chokus.konye.privatememo.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.chokus.konye.privatememo.datamodel.NoteClass
import com.chokus.konye.privatememo.R
import com.chokus.konye.privatememo.activity.NoteActivity
import com.chokus.konye.privatememo.activity.NoteListActivity
import com.chokus.konye.privatememo.interfaces.CustomItemClickListener

/**
 * Created by omen on 12/02/2018.
 */
class NoteRecyclerAdapter(var noteList : MutableList<NoteClass>, context: Context) : RecyclerView.Adapter<NoteRecyclerAdapter.MyViewHolder>(){
    var mContext = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.note_list_item, parent, false)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val notes: NoteClass = noteList[position]
        holder.noteTitleTextView.text = notes.noteTitle
        holder.noteContentTextView.text = notes.noteContent
        holder.noteDateCreatedTextView.text = notes.dateCreated
        holder.setOnCustomItemClickListener(object : CustomItemClickListener {
            override fun onCustomItemClickListener(view: View, position: Int) {
                val intent = Intent(mContext, NoteActivity::class.java)
                intent.putExtra(NoteListActivity.NOTE_TITLE, notes.noteTitle)
                intent.putExtra(NoteListActivity.NOTE_CONTENT,notes.noteContent)
                mContext.startActivity(intent)
            }
        })
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView),View.OnClickListener{
        var noteTitleTextView : TextView
        var noteContentTextView : TextView
        var noteDateCreatedTextView : TextView
        var customItemClickListener : CustomItemClickListener? = null
        init{
            noteTitleTextView = itemView.findViewById(R.id.note_title_textView)
            noteContentTextView = itemView.findViewById(R.id.note_content_textView)
            noteDateCreatedTextView = itemView.findViewById(R.id.note_date_created_textView)
            itemView.setOnClickListener(this)
        }
        fun setOnCustomItemClickListener(itemClickListener: CustomItemClickListener){
            this.customItemClickListener = itemClickListener
        }

        override fun onClick(v: View?) {
            this.customItemClickListener!!.onCustomItemClickListener(v!!,adapterPosition)
        }
    }

    fun removeItem(position: Int){
        //noteList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, noteList.size)
    }

}