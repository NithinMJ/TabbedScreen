package com.example.nithinjohn.tabbedscreen

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import com.amulyakhare.textdrawable.TextDrawable
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.contact_data.view.*
import kotlinx.android.synthetic.main.contact_number.view.*

val fetchTextDrawable = FetchTextDrawable()
class ExpandableListAdapter(private val context: Context, ContactData: Pair<ArrayList<Contact>, HashMap<Contact, List<String>>>) : BaseExpandableListAdapter() {
    private var contactList = ContactData.first
    private var contactNumber = ContactData.second
    override fun getGroup(groupPosition: Int): Any {
        return contactList[groupPosition]
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {

        val drawable: TextDrawable?
        val titleName = contactList[groupPosition].name
        val viewTitle: View = LayoutInflater.from(context).inflate(R.layout.contact_data, parent, false)
        viewTitle.contact_name.text = titleName

        drawable = when {
            groupPosition % 3 == 0 -> fetchTextDrawable.getTextDrawableBuilder()?.buildRound(fetchTextDrawable.getNameLetters(titleName), Color.parseColor("#1B3C7E"))
            groupPosition % 2 == 0 -> fetchTextDrawable.getTextDrawableBuilder()?.buildRound(fetchTextDrawable.getNameLetters(titleName), Color.parseColor("#0DD391"))
            else -> fetchTextDrawable.getTextDrawableBuilder()?.buildRound(fetchTextDrawable.getNameLetters(titleName), Color.parseColor("#EACC0E"))
        }
        Picasso.get().load(contactList[groupPosition].photo).placeholder(drawable!!).into(viewTitle.contact_photo)

        return viewTitle
    }

    fun filterList(filterdNames: ArrayList<Contact>) {
        this.contactList = filterdNames
        notifyDataSetChanged()
    }


    override fun getChildrenCount(groupPosition: Int): Int {
        return contactNumber[contactList[groupPosition]]!!.size
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return contactNumber[contactList[groupPosition]]!![childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        val titleNumber = getChild(groupPosition, childPosition) as String

        val viewNumber: View = LayoutInflater.from(context).inflate(R.layout.contact_number, parent, false)

        viewNumber.contact_phone_number.text = titleNumber

        return viewNumber
    }


    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return contactList.size
    }
}