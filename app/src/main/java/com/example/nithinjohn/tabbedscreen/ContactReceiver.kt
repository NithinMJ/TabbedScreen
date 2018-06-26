package com.example.nithinjohn.tabbedscreen

import android.annotation.SuppressLint
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract

object ContactReceiver {


    @SuppressLint("Recycle")
    fun getContactsData(contactsCursor: Cursor?): Pair<ArrayList<Contact>,HashMap<Contact, List<String>>> {
        var contactList = ArrayList<Contact>()
        var phNum: List<String>
        val contactMap = HashMap<String, Contact>()
        val contactNumberListSorted: HashMap<Contact, List<String>> = HashMap()
        if (contactsCursor?.count ?: 0 > 0) {
            while (contactsCursor != null && contactsCursor.moveToNext()) {

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
        contactMap.forEach { (key, value) ->
            println("KEY : $key   VALUE: $value")
            contactList.add(value)
            phNum = listOf()
            value.phoneNumbers.forEach {
                val re = Regex("[^\\d+]")
                phNum += re.replace(it, "")
            }
            contactNumberListSorted[value] = phNum.toSet().toList()
        }
        contactList = ArrayList(contactList.sortedBy { it.name })
        FragmentHome.listOfContacts = contactList

        contactsCursor?.close()

        return Pair(contactList,contactNumberListSorted)
    }
}