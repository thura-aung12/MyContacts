package com.thuraaung.mycontacts.fragments

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.database.Cursor
import android.os.Bundle
import android.os.Handler
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.thuraaung.mycontacts.*
import com.thuraaung.mycontacts.databinding.FragmentContactsBinding
import java.util.*

class ContactsFragment : Fragment(),
    LoaderManager.LoaderCallbacks<Cursor> {

    companion object {

        @SuppressLint("InlinedApi")
        private val PROJECTION: Array<out String> = arrayOf(
            ContactsContract.CommonDataKinds.Phone._ID,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER,
            ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI
        )

        @SuppressLint("InlinedApi")
        private val SELECTION: String =
            "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} LIKE ?"
    }

    private lateinit var binding: FragmentContactsBinding
    private val contactAdapter = ContactAdapter()
    private lateinit var testAdapter: TestAdapter

    private lateinit var contactList : List<ContactModel>

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loaderManager.initLoader(0, null, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvContact.apply {
            adapter = contactAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return activity?.let {
            return CursorLoader(
                it,ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                PROJECTION,
                null,
                null, "${ContactsContract.Contacts.DISPLAY_NAME_PRIMARY} ASC"
            )
        } ?: throw IllegalStateException()
    }

    override fun onLoadFinished(loader: Loader<Cursor>, cursor: Cursor) {

        binding.lytShimmer.startShimmer()

        Handler().postDelayed({

            binding.lytShimmer.stopShimmer()
            binding.lytShimmer.visibility = View.GONE
            contactList = cursor.getContactList()
            filterContact()

        },1000)

    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
    }

    private fun filterContact(searchString : String = "") {
        val filteredContacts = mutableListOf<ContactModel>()

        for(contact in contactList) {
            contact.name?.also {
                if (it.toLowerCase(Locale.getDefault()).startsWith(searchString.toLowerCase(Locale.getDefault())))
                    filteredContacts.add(contact)
            }
        }

        contactAdapter.refreshContact(filteredContacts)
    }
}