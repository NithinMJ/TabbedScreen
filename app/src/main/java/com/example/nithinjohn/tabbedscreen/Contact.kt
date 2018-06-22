package com.example.nithinjohn.tabbedscreen

import android.net.Uri

data class Contact(
        val name: String,
        val phoneNumbers: MutableList<String>,
        val photo: Uri?
)