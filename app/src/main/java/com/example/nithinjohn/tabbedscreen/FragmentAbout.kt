package com.example.nithinjohn.tabbedscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlinx.android.synthetic.main.fragmentabout.*

class FragmentAbout : Fragment() {



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.fragmentabout, container, false)

//        val callButton: Button = view.findViewById(R.id.callbutton)
        call().execute()

        return view
    }

    @SuppressLint("StaticFieldLeak")
    inner class call:AsyncTask<String,Unit,Unit>(){
        override fun doInBackground(vararg params: String?) {
            callbutton.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:" + phoneNumber.text)
                startActivity(intent)
            }
        }

    }

}


