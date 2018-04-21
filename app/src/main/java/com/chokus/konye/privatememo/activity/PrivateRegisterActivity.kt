package com.chokus.konye.privatememo.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.SpannableStringBuilder
import android.view.WindowManager
import android.widget.Toast
import com.chokus.konye.privatememo.R
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.content_private_register.*


class PrivateRegisterActivity : AppCompatActivity() {
    private var firebaseAuth : FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_private_register)
        setFullScreen()
        viewActions()
        firebaseAuth = FirebaseAuth.getInstance()
    }

    private fun viewActions() {
        email_add_editText.text = getEmailIntent()
        sign_in_button.setOnClickListener {
            val intent = Intent(this, PrivateSignInActivity::class.java)
            startActivity(intent)
        }
        sign_up_button.setOnClickListener {
            checkWidgetsBeforeRegistering()
        }
    }

    private fun getEmailIntent() : Editable {
        val intent = intent.getStringExtra(GetStartedActivity.USER_EMAIL)
        val intentEditable = SpannableStringBuilder(intent)
        return intentEditable
    }

    private fun checkWidgetsBeforeRegistering(){
        when{
            private_name_editText.text.toString().isEmpty() -> toastMethod("please enter user name")
            email_add_editText.text.toString().isEmpty() -> toastMethod("please enter email address")
            password_editText.text.toString().isEmpty() -> toastMethod("please type in password")
            else -> checkPassword()
        }
    }

    private fun checkPassword(){
        if(password_editText.text.toString() == confirm_password_editText.text.toString()){
            registerUser()
        }else{
            toastMethod("passwords do not match")
        }
    }

    private fun registerUser(){
        val email = email_add_editText.text.toString()
        val password = password_editText.text.toString()
        firebaseAuth!!.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) {task ->
                    if(task.isComplete){
                        //sign in is successful. take action
                        toastMethod("Registration successful")
                        val user = firebaseAuth!!.currentUser
                        startActivity(Intent(applicationContext, NoteListActivity::class.java))
                    }else{
                        toastMethod("Registration unsuccessful")
                    }
                }
    }

    override fun onStart() {
        super.onStart()
        //check if user is currently signed in then take action
        val currentUser : FirebaseUser? = firebaseAuth?.currentUser
        if(currentUser != null){
            startActivity(Intent(applicationContext, NoteListActivity::class.java))
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
