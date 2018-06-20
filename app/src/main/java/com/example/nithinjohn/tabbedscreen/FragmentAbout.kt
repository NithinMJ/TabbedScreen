package com.example.nithinjohn.tabbedscreen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlinx.android.synthetic.main.fragmentabout.*

class FragmentAbout : Fragment(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.callbutton -> {
                val intent = Intent(Intent.ACTION_CALL)
                intent.data = Uri.parse("tel:" + phoneNumber.text)
//                println("PHONE NUMBER : ${phoneNumber.text}")
//                println("INTENT : $intent")
                startActivity(intent)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.fragmentabout, container, false)

        val callButton: Button = view.findViewById(R.id.callbutton)
        callButton.setOnClickListener(this)

        return view
    }

}


