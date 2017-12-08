package com.chokus.konye.privatememo

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class PrivateSignInActivity : AppCompatActivity() {
    var emailEdit: EditText? = null
    var passwordEdit: EditText? = null
    var signInButton: Button? = null
    var createAcct: LinearLayout? = null
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
        emailEdit = findViewById<View>(R.id.email_add_editText) as EditText
        passwordEdit = findViewById<View>(R.id.password_editText) as EditText
        signInButton = findViewById<View>(R.id.sign_in_button) as Button
        createAcct = findViewById<View>(R.id.create_acct_layout) as LinearLayout
        createAcct!!.setOnClickListener {
            val intent = Intent(applicationContext, PrivateRegisterActivity::class.java)
            startActivity(intent)
        }
        signInButton!!.setOnClickListener { signIn() }
    }

    private fun signIn() {
        email = emailEdit!!.text.toString().trim { it <= ' ' }
        password = passwordEdit!!.text.toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, "please enter Email", Toast.LENGTH_SHORT).show()
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext, "please enter Password", Toast.LENGTH_SHORT).show()
        }
        progressDialog!!.setMessage("Signing In")
        progressDialog!!.show()
        mAuth!!.signInWithEmailAndPassword(email!!, password!!)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = mAuth!!.currentUser
                        Toast.makeText(applicationContext, "Sign In successful", Toast.LENGTH_SHORT).show()
                        finish()
                        startActivity(Intent(applicationContext, NoteListActivity::class.java))
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(applicationContext, "Error in Sign In", Toast.LENGTH_SHORT).show()
                    }
                    progressDialog!!.dismiss()
                }
    }

    private fun setFullScreen() {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

}
