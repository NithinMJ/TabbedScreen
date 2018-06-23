package com.example.nithinjohn.tabbedscreen

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
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
import android.widget.ExpandableListView
import kotlinx.android.synthetic.main.fragmenthome.*
import kotlinx.android.synthetic.main.fragmenthome.view.*


class FragmentHome : Fragment() {

    var contactList = ArrayList<Contact>()
    val REQUEST_PERMISSION = 1
    var adapter: ExpandableListAdapter? = null
    private var lv: ExpandableListView? = null
    var contactMap = HashMap<String, Contact>()
    var phoneNumberList: List<String> = ArrayList()
    var contactNumberListSorted: HashMap<Contact, List<String>> = HashMap()
    var lastExpandPosition: Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragmenthome, container, false)

        lv = view.contact_list

        activity?.run {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_PERMISSION)
            } else {
                getContacts()
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        search_item?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filter(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })


        lv?.setOnGroupExpandListener(object : ExpandableListView.OnGroupExpandListener {
            override fun onGroupExpand(groupPosition: Int) {
                if (lastExpandPosition != -1 && groupPosition != lastExpandPosition) {
                    lv!!.collapseGroup(lastExpandPosition)
                }
                lastExpandPosition = groupPosition
            }

        })

        contact_list.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            val num = adapter?.getChild(groupPosition, childPosition).toString()
//            Toast.makeText(context,num, Toast.LENGTH_SHORT).show()
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:" + num)
            startActivity(intent)
            true
        }

    }

    private fun filter(text: String) {
        val filterednames = ArrayList<Contact>()

        for (str in contactList) {
            if (str.name.toLowerCase().contains(text.toLowerCase())) {
                filterednames.add(str)
            }
        }
        adapter?.filterList(filterednames)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_PERMISSION) getContacts()
    }

    private fun getContacts() {
        getContactsData()

        adapter = ExpandableListAdapter(context!!, contactList, contactNumberListSorted)

        lv?.setAdapter(adapter)
    }


    @SuppressLint("Recycle")
    private fun getContactsData() {
        phoneNumberList = listOf("")
        val contactsCursor = activity?.contentResolver?.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)
        if (contactsCursor?.count ?: 0 > 0) {
            while (contactsCursor != null && contactsCursor.moveToNext()) {
                val rowID = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts._ID))

                val name = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

                val photoUriString = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI))

                val phoneNumber = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                if (contactMap.containsKey(name)) {
                    contactMap[name]?.phoneNumbers?.add(phoneNumber)
                } else {
                    contactMap.put(
                            name,
                            Contact(
                                    name = name,
                                    photo = if (!photoUriString.isNullOrEmpty()) Uri.parse(photoUriString) else null,
                                    phoneNumbers = mutableListOf(phoneNumber)
                            )
                    )
                }
            }
        }
        contactMap.forEach{
            (key,value) ->
            println("KEY : $key   VALUE: " +value)
            contactList.add(value)


            val phNum = value.phoneNumbers.toSet().toList()

            contactNumberListSorted.put(value, phNum.dropWhile { it == "" })
        }
        contactList = ArrayList(contactList.sortedBy { it.name })
//        println("CONTACT LIST: $contactList")
//        println("CONTACT NUMBER LIST SORTED: $contactNumberListSorted")
        contactsCursor?.close()

    }
}