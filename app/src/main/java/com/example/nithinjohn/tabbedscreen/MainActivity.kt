package com.example.nithinjohn.tabbedscreen

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

const val TAG: String = "TAG"

class MainActivity : AppCompatActivity() {

    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null
    private var mAuth: FirebaseAuth? = null
    private var lastPress: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        viewPager = findViewById(R.id.container)
        setupViewPager(viewPager!!)

        tabLayout = findViewById(R.id.tabs)
        tabLayout!!.setupWithViewPager(viewPager)


        backbutton.setOnClickListener {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        signout.setOnClickListener {
            signOut()
        }

    }

    override fun onBackPressed() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastPress > 5000) {
            val backpressToast: Toast? = Toast.makeText(baseContext, "Do you want to log out?", Toast.LENGTH_SHORT)
            backpressToast?.show()
            lastPress = currentTime

        } else {
            super.onBackPressed()
            //Exit Application on back press instead of moving to the first Activity
            signOut()
        }
    }

    private fun signOut() {
        mAuth!!.signOut()
        LoginManager.getInstance().logOut()
        Toast.makeText(applicationContext, "Sign Out Success !!", Toast.LENGTH_SHORT).show()
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intent)
    }


    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(FragmentHome(), "Phonebook")
        adapter.addFragment(FragmentAbout(), "Dialer")
        viewPager.adapter = adapter
    }

    internal inner class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitleList[position]
        }
    }


}
