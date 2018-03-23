package com.chokus.konye.privatememo.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.chokus.konye.privatememo.R

import kotlinx.android.synthetic.main.activity_get_started.*
import kotlinx.android.synthetic.main.content_get_started.*

class GetStartedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_started)
        setFullScreen()
        viewActions()
    }

    private fun viewActions(){
        sign_in_textView.setOnClickListener {
            val intent = Intent(this, PrivateRegisterActivity::class.java)
            startActivity(intent)
        }
        forward_arrow_img.setOnClickListener {
            val intent = Intent(this, PrivateRegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setFullScreen() {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

}
