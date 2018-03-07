package com.chokus.konye.privatememo.datamodel

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.RealmModule

/**
 * Created by ALPHA AND JAM on 11/22/2017.
 */
open class NoteClass : RealmObject(){
    @PrimaryKey
    var id : Long = 0
    var noteTitle: String? = null
    var noteContent: String? = null
    var dateCreated: String? = null
}
