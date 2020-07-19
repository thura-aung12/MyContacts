package com.thuraaung.mycontacts

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cursoradapter.widget.CursorAdapter

class TestAdapter(context : Context,cursor : Cursor) : CursorAdapter(context,cursor,0) {

    override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View {
        return LayoutInflater.from(context).inflate(R.layout.layout_contact_item,parent,false)
    }

    override fun bindView(view: View, context: Context?, cursor: Cursor) {
        val tvName : TextView = view.findViewById(R.id.tv_name)
        val tvPhone : TextView = view.findViewById(R.id.tv_phone)

        val nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
        val phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER)
        val imageUrlIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI)

        tvName.text = cursor.getString(nameIndex)
        tvPhone.text = cursor.getString(phoneIndex)

    }
}