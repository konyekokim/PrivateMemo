package com.chokus.konye.privatememo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.WindowManager

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        setFullScreen()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val WELCOME_TIMEOUT = 4000
        Handler().postDelayed({
            val intent = Intent(applicationContext, PrivateRegisterActivity::class.java)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            startActivity(intent)
            finish()
        }, WELCOME_TIMEOUT.toLong())
    }

    private fun setFullScreen() {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }
}
