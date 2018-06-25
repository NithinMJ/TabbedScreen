package com.example.nithinjohn.tabbedscreen

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_loader.*

class Loader : AppCompatActivity() {

    private val splash = 5000L
    private val requestpermission = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), requestpermission)
        }

        setContentView(R.layout.activity_loader)
        Glide.with(this)
                .asGif()
                .load(R.drawable.loader_now)
                .into(img)

        Handler().postDelayed({
            val i = Intent(this@Loader, LoginActivity::class.java)
            startActivity(i)
            finish()
        }, splash)
    }
}

