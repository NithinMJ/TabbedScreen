@file:Suppress("DEPRECATION")

package com.example.nithinjohn.tabbedscreen

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    var googleApiClient:GoogleApiClient?=null
    val GOOGLE_LOG_IN_RC = 1

    internal var lastPress: Long = 0
    private var mAuth: FirebaseAuth? = null
    var callbackManager: CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        FacebookSdk.getApplicationContext()
        AppEventsLogger.activateApp(this@LoginActivity)
        callbackManager = CallbackManager.Factory.create()

        fb_login_button.setReadPermissions("email")

        fb_login_button.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {

            override fun onSuccess(loginResult: LoginResult) {

                handleFacebookAccessToken(loginResult.accessToken)
            }
            override fun onCancel() {
                // App code
            }
            override fun onError(exception: FacebookException) {
                // App code
            }
        })


        // Configure Google Sign In
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.request_client_id))
                .requestEmail()
                .build()

        // Creating and Configuring Google Api Client.
        googleApiClient = GoogleApiClient.Builder(this@LoginActivity)
                .enableAutoManage(this@LoginActivity  /* OnConnectionFailedListener */) { }
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build()

        goto_signup.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignupActivity::class.java)
            startActivity(intent)
        }

        login_btn.setOnClickListener {
            signIn(username.text.toString(),password.text.toString())
        }

        google_signin_button.setOnClickListener {
            Log.i("TAG", "Trying Google LogIn.")
            googleLogin()
        }
    }

    private fun googleLogin() {
        Log.i("TAG", "Starting Google LogIn Flow.")
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
        startActivityForResult(signInIntent, GOOGLE_LOG_IN_RC)
    }
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager!!.onActivityResult(requestCode, resultCode, data)
        Log.i("TAG", "Got Result code $requestCode.")
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == GOOGLE_LOG_IN_RC) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            Log.i("TAG", "With Google LogIn, is result a success? ${result.isSuccess}.")
            if (result.isSuccess) {
                // Google Sign In was successful, authenticate with Firebase
                Toast.makeText(applicationContext, "Google Login Success !!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this@LoginActivity, "Some error occurred.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d("TAG", "handleFacebookAccessToken:" + token)
        val credential = FacebookAuthProvider.getCredential(token.token)
        mAuth!!.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAG", "signInWithCredential:success")
//                        val user = mAuth!!.currentUser
                        Toast.makeText(applicationContext, "FB Login Success !!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "signInWithCredential:failure", task.getException())
                        Toast.makeText(this@LoginActivity, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                    }
                }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.i("TAG", "Authenticating user with firebase.")
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth?.signInWithCredential(credential)?.addOnCompleteListener(this) { task ->
            Log.i("TAG", "Firebase Authentication, is result a success? ${task.isSuccessful}.")
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            } else {
                // If sign in fails, display a message to the user.
                Log.e("TAG", "Authenticating with Google credentials in firebase FAILED !!")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser: FirebaseUser? = mAuth?.currentUser
        if (currentUser != null) {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)

        }
    }

    override fun onBackPressed() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastPress > 5000) {
            val backpressToast: Toast? = Toast.makeText(baseContext, "Press back again to exit", Toast.LENGTH_LONG)
            backpressToast?.show()
            lastPress = currentTime

        } else {
            super.onBackPressed()
            //Exit Application on back press instead of moving to the first Activity
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }


    private fun signIn(email: String, password: String) {
        Log.e("TAG", "signIn:" + email)
        if (!validateForm(email, password)) {
            return
        }

        mAuth!!.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.e("TAG", "signIn: Success!")

                        // update UI with the signed-in user's information
                        Toast.makeText(applicationContext, "Login Success !!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Log.e("TAG", "signIn: Fail!", task.exception)
                        Toast.makeText(applicationContext, "Authentication failed!", Toast.LENGTH_SHORT).show()
                    }

                    if (!task.isSuccessful) {
                        Toast.makeText(applicationContext, "Authentication failed!", Toast.LENGTH_SHORT).show()
                    }
                }
    }

    private fun validateForm(email: String, password: String): Boolean {

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, "Enter email address!", Toast.LENGTH_SHORT).show()
            return false
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext, "Enter password!", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.length < 6) {
            Toast.makeText(applicationContext, "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}
