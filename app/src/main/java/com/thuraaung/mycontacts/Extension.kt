package com.thuraaung.mycontacts

import android.app.Activity
import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.view.inputmethod.InputMethodManager

fun Activity.hideSoftKeyboard() {
    if(currentFocus != null) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager
            .hideSoftInputFromWindow(currentFocus!!.windowToken,0)
    }
}


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