package com.example.nithinjohn.tabbedscreen

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*


class ForgotPassword : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        mAuth = FirebaseAuth.getInstance()

        reset_pwd.setOnClickListener {
            mAuth?.sendPasswordResetEmail(pwd_reset_email.text.toString())?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email Sent")
                    Toast.makeText(applicationContext, "Email Link sent to reset password", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@ForgotPassword, LoginActivity::class.java)
                    startActivity(intent)
                }else{
                    Toast.makeText(applicationContext, "Account does not exist !", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this@ForgotPassword, LoginActivity::class.java)
        startActivity(intent)
    }
}
