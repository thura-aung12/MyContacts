package com.thuraaung.mycontacts.fragments

import android.annotation.SuppressLint
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.thuraaung.mycontacts.R


@Suppress("DEPRECATION")
class DetailFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {

    @SuppressLint("InlinedApi")
    private val FROM_COLUMNS: Array<String> = arrayOf(
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
    )

    private val TO_IDS: IntArray = intArrayOf(android.R.id.text1)

    // Defines a constant that identifies the loader
    private val DETAILS_QUERY_ID: Int = 0

    private val PROJECTION: Array<out String> = arrayOf(
        ContactsContract.CommonDataKinds.Phone._ID,
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
    )

    private val SELECTION: String =
                "${android.provider.ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY} = ?"

    private val selectionArgs: Array<String> = arrayOf("")
    private var lookupKey: String? = null
    private  val SORT_ORDER = ContactsContract.Data.MIMETYPE


    private var cursorAdapter: SimpleCursorAdapter? = null


    lateinit var contactsList: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loaderManager.initLoader(DETAILS_QUERY_ID,null,this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.also {
            contactsList = it.findViewById<ListView>(R.id.contact_detail_list)
            // Gets a CursorAdapter
            cursorAdapter = SimpleCursorAdapter(
                it,
                R.layout.contact_list_item,
                null,
                FROM_COLUMNS, TO_IDS,
                0
            )
            // Sets the adapter for the ListView
            contactsList.adapter = cursorAdapter
        }
    }


    override fun onCreateLoader(loaderId : Int, args: Bundle?): Loader<Cursor> {

        // Choose the proper action
        val mLoader = when(loaderId) {
            DETAILS_QUERY_ID -> {
                // Assigns the selection parameter
                selectionArgs[0] = "2758i38003985521590528.54i3"
                // Starts the query
                activity?.let {
                    CursorLoader(
                        it,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        PROJECTION,
                        SELECTION,
                        selectionArgs,
                        null
                    )
                }
            }
            else -> null
        }
        return mLoader!!
    }

    override fun onLoadFinished(loader: Loader<Cursor>, cursor: Cursor) {

        when(loader.id) {
            DETAILS_QUERY_ID -> {
                cursorAdapter?.swapCursor(cursor)
            }
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        when (loader.id) {
            DETAILS_QUERY_ID -> {
                // Delete the reference to the existing Cursor
                cursorAdapter?.swapCursor(null)
            }
        }
    }
}