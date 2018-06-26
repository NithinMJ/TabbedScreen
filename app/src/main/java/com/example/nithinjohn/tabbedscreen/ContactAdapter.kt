package com.example.nithinjohn.tabbedscreen

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.amulyakhare.textdrawable.TextDrawable
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.contact_data.view.*


class ContactAdapter(val context: Context, var contactList: ArrayList<Contact>) : BaseAdapter() {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.contact_data, parent, false)

        view.contact_name.text = contactList[position].name
        val drawable: TextDrawable? = getTextDrawableBuilder()?.buildRound(getNameLetters(contactList[position].name), Color.DKGRAY)

        Picasso.get().load(contactList[position].photo).placeholder(drawable!!).into(view.contact_photo)

        return view
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

    override fun getItem(position: Int): Any {
        return contactList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return contactList.size
    }
}
