package com.example.nithinjohn.tabbedscreen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler

class SplashActivity : Activity() {

    private val SPLASH_TIME_OUT = 10000L
    override fun onCreate(savedInstanceState:Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed(object:Runnable {
            override fun run() {
                val i = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(i)
                finish()
            }
        }, SPLASH_TIME_OUT)
    }
}
