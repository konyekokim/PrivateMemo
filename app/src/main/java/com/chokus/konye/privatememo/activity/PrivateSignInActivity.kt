package com.chokus.konye.privatememo.activity

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.chokus.konye.privatememo.R

import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_private_sign_in.*

class PrivateSignInActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    var email: String? = null
    var password: String? = null
    var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_private_sign_in)
        setFullScreen()
        viewActions()
        mAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
    }

    private fun viewActions() {
        sign_in_button.setOnClickListener {
            val intent = Intent(this, NoteListActivity::class.java)
            startActivity(intent)
        }
        sign_up_button.setOnClickListener{
            val intent = Intent(this, PrivateRegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setFullScreen() {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

}
