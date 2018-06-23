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
import java.util.*

class ExpandableListAdapter(val context: Context, var contactList: ArrayList<Contact>, var contactNumber: HashMap<Contact,List<String>>) : BaseExpandableListAdapter() {

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

        val titleName = contactList[groupPosition].name
        val viewTitle: View = LayoutInflater.from(context).inflate(R.layout.contact_data, parent, false)
        viewTitle.contact_name.text = titleName

        val drawable: TextDrawable? = getTextDrawableBuilder()?.buildRound(getNameLetters(titleName), Color.DKGRAY)
        Picasso.get().load(contactList[groupPosition].photo).placeholder(drawable!!).into(viewTitle.contact_photo)

        return viewTitle
    }

    fun filterList(filterdNames: ArrayList<Contact>) {
        this.contactList = filterdNames
        notifyDataSetChanged()
    }

    private var textDrawableBuilder: TextDrawable.IShapeBuilder? = null

    fun getTextDrawableBuilder(): TextDrawable.IShapeBuilder? {
        if (textDrawableBuilder == null) {
            textDrawableBuilder = TextDrawable.builder().beginConfig().fontSize(14).bold().width(50).height(50).endConfig()
        }
        return textDrawableBuilder
    }

    fun getNameLetters(contactName: String): String {
        if (contactName.isEmpty()) {
            return "#"
        } else if (contactName.matches("-?\\d+(\\.\\d+)?".toRegex())) {
            return "#"
        }

        val strings = contactName.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val nameLetters = StringBuilder()
        for (s in strings) {
            if (nameLetters.length >= 2)
                return nameLetters.toString().capitalize()
            if (!s.isEmpty()) {
                nameLetters.append(s.trim { it <= ' ' }[0])
            }
        }
        return nameLetters.toString().capitalize()
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return contactNumber.get(contactList[groupPosition])!!.size
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return contactNumber.get(contactList[groupPosition])!![childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        var titleNumber = getChild(groupPosition, childPosition) as String

        val re = Regex("[^\\d+]")
        titleNumber = re.replace(titleNumber,"")

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