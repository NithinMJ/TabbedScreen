package com.example.nithinjohn.tabbedscreen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
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

    var adapter: ExpandableListAdapter? = null
    private var lv: ExpandableListView? = null
    private var lastExpandPosition: Int = -1
    private val contactReceiver = ContactReceiver()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragmenthome, container, false)
        lv = view.contact_list
        lv?.setGroupIndicator(null)

        getContacts()

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

        lv?.setOnGroupExpandListener { groupPosition ->
            if (lastExpandPosition != -1 && groupPosition != lastExpandPosition) {
                lv!!.collapseGroup(lastExpandPosition)
            }
            lastExpandPosition = groupPosition
        }

        contact_list.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            val num = adapter?.getChild(groupPosition, childPosition).toString()
//            Toast.makeText(context,num, Toast.LENGTH_SHORT).show()
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$num")
            startActivity(intent)
            true
        }

    }

    private fun filter(text: String) {
        val filterednames = ArrayList<Contact>()

        for (str in contactReceiver.contactList) {
            if (str.name.toLowerCase().contains(text.toLowerCase())) {
                filterednames.add(str)
            }
        }
        adapter?.filterList(filterednames)
    }

    private fun getContacts() {
        val contactsCursor = activity?.contentResolver?.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)
        adapter = ExpandableListAdapter(context!!, contactReceiver.getContactsData(contactsCursor))

        lv?.setAdapter(adapter)
    }


}