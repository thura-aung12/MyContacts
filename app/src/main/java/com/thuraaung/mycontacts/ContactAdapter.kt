package com.thuraaung.mycontacts

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cursoradapter.widget.CursorAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ContactAdapter(context : Context, cursor : Cursor) : CursorAdapter(context,cursor,0) {

    var itemClickListener : ((String,String) -> Unit)? = null

    override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View {
        return LayoutInflater.from(context).inflate(R.layout.layout_contact_item,parent,false)
    }

    override fun bindView(view: View, context: Context?, cursor: Cursor) {

        val tvName : TextView = view.findViewById(R.id.tv_name)
        val tvPhone : TextView = view.findViewById(R.id.tv_phone)
        val imgContact : ImageView = view.findViewById(R.id.img_contact)

        val nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
        val phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER)
        val imageUrlIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI)

        val name = cursor.getString(nameIndex)
        val phone = cursor.getString(phoneIndex)
        val imageUrl = cursor.getString(imageUrlIndex)

        tvName.text = name
        tvPhone.text = phone
        Glide.with(view.context)
            .load(imageUrl)
            .placeholder(R.drawable.ic_baseline_account_circle_24)
            .apply(RequestOptions.circleCropTransform())
            .into(imgContact)

        view.setOnClickListener { itemClickListener?.invoke(name,phone) }

    }
}