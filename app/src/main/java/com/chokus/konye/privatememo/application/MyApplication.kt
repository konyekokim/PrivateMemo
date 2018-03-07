package com.chokus.konye.privatememo.application

import android.app.Application
import io.realm.Realm

/**
 * Created by ALPHA AND JAM on 12/12/2017.
 */
class MyApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}