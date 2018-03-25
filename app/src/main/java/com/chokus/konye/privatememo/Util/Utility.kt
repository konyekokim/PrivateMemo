package com.chokus.konye.privatememo.Util

import android.content.Context


/**
 * Created by omen on 25/03/2018.
 */
class Utility{
    companion object {
        fun calculateNoOfColumns(context : Context) : Int{
            val displayMetrics = context.resources.displayMetrics
            val dpWidth : Float = displayMetrics.widthPixels / displayMetrics.density
            return ((dpWidth/180).toInt())
        }
    }
}