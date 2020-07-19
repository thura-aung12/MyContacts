package com.thuraaung.mycontacts.fragments

import android.annotation.SuppressLint
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.thuraaung.mycontacts.R

//2758i38003985521590528.54i3


@SuppressLint("InlinedApi")
private val FROM_COLUMNS: Array<String> = arrayOf(
    ContactsContract.Contacts.LOOKUP_KEY
)

private val TO_IDS: IntArray = intArrayOf(android.R.id.text1)

private const val CONTACT_ID_INDEX: Int = 0
private const val CONTACT_KEY_INDEX: Int = 1

@SuppressLint("InlinedApi")
private val PROJECTION: Array<out String> = arrayOf(
    ContactsContract.Contacts._ID,
    ContactsContract.Contacts.LOOKUP_KEY,
    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
)


@SuppressLint("InlinedApi")
private val SELECTION: String =
    "${ContactsContract.Contacts.DISPLAY_NAME_PRIMARY} LIKE ?"

@Suppress("DEPRECATION")
class TestFragment : Fragment(),
    LoaderManager.LoaderCallbacks<Cursor>,
    AdapterView.OnItemClickListener {

    lateinit var contactsList: ListView
    var contactId: Long = 0
    var contactKey: String? = null
    var contactUri: Uri? = null
    private var cursorAdapter: SimpleCursorAdapter? = null

    private val searchString: String = "?"
    private val selectionArgs = arrayOf(searchString)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loaderManager.initLoader(0, null, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.also {
            contactsList = it.findViewById<ListView>(R.id.contact_list_view)
            // Gets a CursorAdapter
            cursorAdapter = SimpleCursorAdapter(
                it,
                R.layout.contact_list_item,
                null,
                FROM_COLUMNS,
                TO_IDS,
                0
            )
            // Sets the adapter for the ListView
            contactsList.adapter = cursorAdapter
        }
        contactsList.onItemClickListener = this
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {

        selectionArgs[0] = "%"
        // Starts the query
        return activity?.let {
            return CursorLoader(
                it,
                ContactsContract.Contacts.CONTENT_URI,
                PROJECTION,
                SELECTION,
                selectionArgs, "${ContactsContract.Contacts.DISPLAY_NAME_PRIMARY} ASC"
            )
        } ?: throw IllegalStateException()
    }

    override fun onLoadFinished(loader: Loader<Cursor>, cursor: Cursor?) {
        // Put the result Cursor in the adapter for the ListView
        cursorAdapter?.swapCursor(cursor)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        // Delete the reference to the existing Cursor
        cursorAdapter?.swapCursor(null)
    }

    override fun onItemClick(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        Toast.makeText(context,"Item click",Toast.LENGTH_SHORT).show()
        // Get the Cursor
        val cursor: Cursor? = (parent.adapter as? CursorAdapter)?.cursor?.apply {
            // Move to the selected contact
            moveToPosition(position)
            // Get the _ID value
            contactId = getLong(CONTACT_ID_INDEX)
            // Get the selected LOOKUP KEY
            contactKey = getString(CONTACT_KEY_INDEX)
            // Create the contact's content Uri
            contactUri = ContactsContract.Contacts.getLookupUri(contactId, contactKey)
            /*
             * You can use contactUri as the content URI for retrieving
             * the details for a contact.
             */
            Log.d("Lookup","Lookup key ${contactKey}")
        }
    }
}