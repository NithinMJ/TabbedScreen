package com.example.nithinjohn.tabbedscreen

import com.amulyakhare.textdrawable.TextDrawable

var textDrawableBuilder: TextDrawable.IShapeBuilder? = null

class FetchTextDrawable {

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

        val string = contactName.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
        val nameLetters = StringBuilder()
        if (!string.isEmpty()) {
            nameLetters.append(string.trim { it <= ' ' }[0])
        }
        return nameLetters.toString().capitalize()
    }
}