package com.chokus.konye.privatememo.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import android.widget.Toast
import com.chokus.konye.privatememo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.content_get_started.*

class GetStartedActivity : AppCompatActivity() {
    private var firebaseAuth : FirebaseAuth? = null
    companion object {
        const val USER_EMAIL = "com.chokus.konye.privatememo.activity user email address"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_started)
        setFullScreen()
        viewActions()
        firebaseAuth = FirebaseAuth.getInstance()
    }

    private fun viewActions(){
        sign_in_textView.setOnClickListener {
            val intent = Intent(this, PrivateRegisterActivity::class.java)
            if(get_started_email.text.trim().isNotEmpty()){
                intent.putExtra(USER_EMAIL,get_started_email.text.toString())
                startActivity(intent)
            }else{
                Toast.makeText(this, "Enter your email  address", Toast.LENGTH_LONG).show()
            }
        }
        forward_arrow_img.setOnClickListener {
            val intent = Intent(this, PrivateRegisterActivity::class.java)
            if(get_started_email.text.trim().isNotEmpty()){
                intent.putExtra(USER_EMAIL,get_started_email.text.toString())
                startActivity(intent)
            }else{
                Toast.makeText(this, "Enter your email  address", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser : FirebaseUser? = firebaseAuth?.currentUser
        if(currentUser != null){
            startActivity(Intent(applicationContext, NoteListActivity::class.java))
        }
    }

    private fun setFullScreen() {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

}
