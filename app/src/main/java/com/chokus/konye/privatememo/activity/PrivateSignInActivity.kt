package com.chokus.konye.privatememo.activity

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.chokus.konye.privatememo.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_private_sign_in.*

class PrivateSignInActivity : AppCompatActivity() {
    private var firebaseAuth: FirebaseAuth? = null
    var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_private_sign_in)
        setFullScreen()
        viewActions()
        firebaseAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
    }

    private fun viewActions() {
        error_layout.visibility = View.INVISIBLE
        sign_in_button.setOnClickListener {
            checkWidgetsBeforeSigning()
        }
        sign_up_button.setOnClickListener{
            val intent = Intent(this, PrivateRegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkWidgetsBeforeSigning(){
        when{
            email_add_editText.text.toString().isEmpty() -> toastMethod("Please enter email address")
            password_editText.text.toString().isEmpty() -> toastMethod("input password")
            else -> signUserIn()
        }
    }

    private fun signUserIn(){
        val email = email_add_editText.text.toString()
        val password = password_editText.text.toString()
        firebaseAuth!!.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this){task ->
                    if(task.isSuccessful){
                        //Sign in successful.. take action
                        val user = firebaseAuth!!.currentUser
                        startActivity(Intent(applicationContext, NoteListActivity::class.java))
                        error_layout.visibility = View.INVISIBLE
                    }else{
                        error_layout.visibility = View.VISIBLE
                    }
                }
    }

    private fun setFullScreen() {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    private fun toastMethod(message : String?){
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

}
