package com.chokus.konye.privatememo.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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
    private var privateNameEdit: EditText? = null
    private var emailEdit: EditText? = null
    private var passwordEdit: EditText? = null
    private var confirmPasswordEdit: EditText? = null
    private var createAcctButton: Button? = null
    private var signInLayout: LinearLayout? = null
    private var mAuth: FirebaseAuth? = null
    private var progressDialog: ProgressDialog? = null
    private var mGoogleSignInClient : GoogleSignInClient? = null
    private val RC_SIGN_IN : Int = 1001
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_private_register)
        setFullScreen()
        viewActions()
        mAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        /*if (mAuth.currentUser != null) {
            val intent = Intent(applicationContext, NoteListActivity::class.java)
            startActivity(intent)
        }*/
        val gso : GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }


    private fun viewActions() {
        sign_in_button.setSize(SignInButton.SIZE_STANDARD)
        sign_in_button.setOnClickListener {
            googleSignIn()
        }
        privateNameEdit = findViewById<View>(R.id.private_name_editText) as EditText
        emailEdit = findViewById<View>(R.id.email_add_editText) as EditText
        passwordEdit = findViewById<View>(R.id.password_editText) as EditText
        confirmPasswordEdit = findViewById<View>(R.id.confirm_password_editText) as EditText
        createAcctButton = findViewById<View>(R.id.create_acct_button) as Button
        signInLayout = findViewById<View>(R.id.sign_in_layout) as LinearLayout
        signInLayout!!.setOnClickListener {
            val intent = Intent(applicationContext, PrivateSignInActivity::class.java)
            startActivity(intent)
        }
        //for phones that work with sense.. non rooted  phones
        //createAcctButton!!.setOnClickListener { createAccount() }
        //just for test as i am using rooted phone
        createAcctButton!!.setOnClickListener{
            val intent = Intent(applicationContext, NoteListActivity::class.java)
            startActivity(intent)
        }
    }

    private fun googleSignIn(){
        val signInIntent : Intent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent,RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...)
        if(requestCode == RC_SIGN_IN){
            val task : Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                //Google sign in was successful, authenticate with Firebase
                val account : GoogleSignInAccount = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            }catch (e : ApiException){
                Log.w("api error","Google Sign In failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(account : GoogleSignInAccount){
        Log.d("tag","firebase with Google" + account.id)
        val credential : AuthCredential = GoogleAuthProvider.getCredential(account.idToken,null)
        mAuth!!.signInWithCredential(credential)
                .addOnCompleteListener(this){ task ->
                    if(task.isSuccessful){
                        //Sign in is successful, move to next activity
                        val user : FirebaseUser = mAuth!!.currentUser!!
                        Toast.makeText(applicationContext, "Account created successfully", Toast.LENGTH_LONG).show()
                        val intent = Intent(applicationContext, PrivateSignInActivity:: class.java)
                        startActivity(intent)
                    }else{
                        //if sign in fails
                        Toast.makeText(applicationContext,"google authentication failed", Toast.LENGTH_LONG).show()
                    }
                }
    }

    private fun createAccount() {
        val email :String = emailEdit!!.text.toString()
        val password : String = passwordEdit!!.text.toString()
        val confirmPassword : String = confirmPasswordEdit!!.text.toString()
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, "please enter email", Toast.LENGTH_SHORT).show()
            return
        }
        if (password != confirmPassword) {
            Toast.makeText(applicationContext, "password do not match", Toast.LENGTH_LONG).show()
            //stopping function executing further
            return
        }
        progressDialog!!.setMessage("Creating Account")
        progressDialog!!.show()
        mAuth!!.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(applicationContext, "Account created successfully", Toast.LENGTH_LONG).show()
                        verifyEmail()
                        updateUser()
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(applicationContext, "Error, try again",
                                Toast.LENGTH_SHORT).show()
                    }
                    progressDialog!!.dismiss()
                }
    }

    private fun verifyEmail(){
        val mUser = mAuth!!.currentUser
        mUser!!.sendEmailVerification()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(applicationContext,
                                "Verification email sent to " + mUser.email,
                                Toast.LENGTH_SHORT).show()
                    } else {
                        Log.e("Error in verifying", "sendEmailVerification", task.exception)
                        Toast.makeText(applicationContext,
                                "Failed to send verification email.",
                                Toast.LENGTH_SHORT).show()
                    }
                }
    }

    private fun updateUser(){
        //start next activity
        val intent = Intent(applicationContext, PrivateSignInActivity::class.java)
        startActivity(intent)
    }

    private fun setFullScreen() {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }
}
