package com.example.nithinjohn.tabbedscreen

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import kotlinx.android.synthetic.main.fragmenthome.*


class FragmentHome : Fragment() {

    val contactList = ArrayList<Contact>()
    val REQUEST_PERMISSION =1
    var adapter:ContactAdapter?= null
    var lv:ListView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragmenthome, container, false)

        lv = view.findViewById<ListView>(R.id.contact_list)


        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(ActivityCompat.checkSelfPermission(context!!, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_PERMISSION)
        }else{
            getContacts()
        }

        search_item?.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                filter(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })

    }

    private  fun filter(text:String){
        val filterednames = ArrayList<Contact>()

        for(str in contactList){
            if(str.name.toLowerCase().contains(text.toLowerCase())){
                filterednames.add(str)
            }
        }
        adapter?.filterList(filterednames)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == REQUEST_PERMISSION)getContacts()
    }

    private fun getContacts() {
        adapter = ContactAdapter(context!!,getContactsData())

        lv?.adapter = adapter
    }


    @SuppressLint("Recycle")
    private fun getContactsData(): ArrayList<Contact> {

        val contactsCursor = activity?.contentResolver?.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null)
        if(contactsCursor?.count ?: 0 > 0){
            while(contactsCursor != null && contactsCursor.moveToNext()){
                val rowID = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts._ID))

                val name = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

                var phoneNumber = ""
                if(contactsCursor.getInt(contactsCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0){
                    val phoneNumberCursor = activity?.contentResolver?.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "= ?",
                            arrayOf<String>(rowID),
                            null
                    )
                    while(phoneNumberCursor?.moveToNext()!!){
                        phoneNumber += phoneNumberCursor.getString(
                                phoneNumberCursor.getColumnIndex((ContactsContract.CommonDataKinds.Phone.NUMBER))
                        ) + "\n"
                    }

                    phoneNumberCursor.close()
                }


                val contactPhotoUri : Uri? = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI,rowID)
                println("CONTACT PHOTO URI : $contactPhotoUri")
                contactList.add(Contact(name, phoneNumber,contactPhotoUri))

            }
        }
        contactsCursor?.close()
        contactList.sortWith(compareBy { it.name })
        println("CONTACT LIST: $contactList")
        return contactList
    }



}