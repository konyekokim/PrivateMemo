package com.chokus.konye.privatememo

import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.Sort

/**
 * Created by ALPHA AND JAM on 12/12/2017.
 */
class NoteRealmManager{
    val realm: Realm by lazy {
        val config = RealmConfiguration.Builder()
                .name("notes.realm")
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(3)
                .build()
        Realm.getInstance(config)
    }

    fun find(id : Long): NoteClass?{
        return  realm.where(NoteClass::class.java).equalTo("id",id).findFirst()
    }

    fun findAll(): MutableList<NoteClass>{
        return realm.where(NoteClass::class.java).findAllSorted("id",Sort.DESCENDING)
    }

    fun insert(title: String, content: String, dateCreated: String){
        realm.beginTransaction()
        var newId: Long = 1
        if(realm.where(NoteClass::class.java).max("id") != null){
            newId = realm.where(NoteClass::class.java).max("id") as Long + 1
        }
        val note = realm.createObject(NoteClass::class.java, newId)
        note.noteTitle = title
        note.noteContent = content
        note.dateCreated = dateCreated
        realm.commitTransaction()
    }

    fun update(id: Long, title: String, content: String){
        realm.beginTransaction()
        val note = find(id)
        note?.noteTitle = title
        note?.noteContent = content
        realm.commitTransaction()
    }

    fun deleteById(id: Long){
        realm.beginTransaction()
        val results = realm.where(NoteClass::class.java).equalTo("id",id).findAll()
        results.deleteAllFromRealm()
        realm.commitTransaction()
    }
}