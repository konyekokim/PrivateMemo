package com.chokus.konye.privatememo.adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.chokus.konye.privatememo.datamodel.NoteClass
import com.chokus.konye.privatememo.R
import com.chokus.konye.privatememo.activity.NoteActivity
import com.chokus.konye.privatememo.activity.NoteListActivity
import com.chokus.konye.privatememo.interfaces.CustomItemClickListener
import kotlinx.android.synthetic.main.security_dialog.*

/**
 * Created by omen on 12/02/2018.
 */
class NoteRecyclerAdapter(var noteList : MutableList<NoteClass>, context: Context) : RecyclerView.Adapter<NoteRecyclerAdapter.MyViewHolder>(){
    var mContext = context
    private var securityDialog : Dialog? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.note_grid_item, parent, false)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val notes: NoteClass = noteList[position]
        holder.noteTitleTextView.text = notes.noteTitle
        holder.noteGridImageView.setImageResource(R.mipmap.ic_grid)
        holder.setOnCustomItemClickListener(object : CustomItemClickListener {
            override fun onCustomItemClickListener(view: View, position: Int) {
                SecurityDialog(notes)
                securityDialog!!.show()
            }
        })
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView),View.OnClickListener{
        var noteTitleTextView : TextView
        var noteGridImageView : ImageView
        var customItemClickListener : CustomItemClickListener? = null
        init{
            noteTitleTextView = itemView.findViewById(R.id.note_title_textView)
            noteGridImageView = itemView.findViewById(R.id.note_grid_imgView)
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

    private fun SecurityDialog(notes : NoteClass){
        securityDialog = Dialog(mContext)
        securityDialog!!.setContentView(R.layout.security_dialog)
        securityDialog!!.setCancelable(false)
        securityDialog!!.dialog_ok_button.setOnClickListener {
            val intent = Intent(mContext, NoteActivity::class.java)
            intent.putExtra(NoteListActivity.NOTE_TITLE, notes.noteTitle)
            intent.putExtra(NoteListActivity.NOTE_CONTENT,notes.noteContent)
            mContext.startActivity(intent)
            securityDialog!!.cancel()
        }
        securityDialog!!.dialog_cancel_button.setOnClickListener {
            securityDialog!!.cancel()
        }
    }

}