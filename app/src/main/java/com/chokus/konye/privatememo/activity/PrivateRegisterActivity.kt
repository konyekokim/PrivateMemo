package com.chokus.konye.privatememo.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast

import com.google.android.gms.tasks.Task
import com.chokus.konye.privatememo.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.content_private_register.*


class PrivateRegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_private_register)
        setFullScreen()
        viewActions()
    }

    private fun viewActions() {
        private_name_editText.text = getEmailIntent()
        sign_in_button.setOnClickListener {
            val intent = Intent(this, PrivateSignInActivity::class.java)
            startActivity(intent)
        }
        sign_up_button.setOnClickListener {
            val intent = Intent(this, NoteListActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getEmailIntent() : Editable {
        val intent = intent.getStringExtra(GetStartedActivity.USER_EMAIL)
        val intentEditable = SpannableStringBuilder(intent)
        return intentEditable
    }

    private fun setFullScreen() {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }
}
