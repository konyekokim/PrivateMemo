package com.chokus.konye.privatememo

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

import io.realm.RealmObject

/**
 * Created by ALPHA AND JAM on 11/22/2017.
 */

open class NoteClass : RealmObject() {
    var noteTitle: String? = null
    var noteContent: String? = null
    var dateCreated: String? = null
    fun creationDate(): String? {
        val myDate = Date()
        val calendar = Calendar.getInstance()
        calendar.timeZone = TimeZone.getTimeZone("UTC")
        calendar.time = myDate
        val time = calendar.time
        val outputFmt = SimpleDateFormat("MMM dd, yyy h:mm a zz")
        dateCreated = outputFmt.format(time)
        return dateCreated
    }
}
