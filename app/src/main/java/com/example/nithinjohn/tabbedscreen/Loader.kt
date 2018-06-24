package com.example.nithinjohn.tabbedscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_loader.*

class Loader : AppCompatActivity() {

    private val SPLASH_TIME_OUT = 5000L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_loader)
        Glide.with(this)
                .asGif()
                .load(R.drawable.loader_now)
                .into(img)

        Handler().postDelayed({
            val i = Intent(this@Loader, MainActivity::class.java)
            startActivity(i)
            finish()
        }, SPLASH_TIME_OUT)
    }
}

