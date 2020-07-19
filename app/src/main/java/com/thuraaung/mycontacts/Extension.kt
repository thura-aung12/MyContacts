package com.thuraaung.mycontacts

import android.database.Cursor
import android.provider.ContactsContract

fun Cursor.getContactList() : List<ContactModel> {
    val contacts = mutableListOf<ContactModel>()

    val idIndex = getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID)
    val nameIndex = getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
    val phoneIndex = getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER)
    val imageUrlIndex = getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI)

    while(moveToNext()) {

        val id = getString(idIndex)
        val name = getString(nameIndex)
        val phone = getString(phoneIndex)
        val imageUrl = getString(imageUrlIndex)

        contacts.add(ContactModel(id,name,phone,imageUrl))
    }

//    close()
    return contacts
}