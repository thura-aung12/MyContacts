package com.thuraaung.mycontacts

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.thuraaung.mycontacts.databinding.FragmentContactsBinding

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
        const val SELECTION: String =
            "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} LIKE ? "

        const val SORT_ORDER =
            "${ContactsContract.Contacts.DISPLAY_NAME_PRIMARY} ASC"
        const val SEARCH_STRING = "SEARCH_STRING"
    }

    private var mSearchString: String = "%"

    private lateinit var binding: FragmentContactsBinding
    private lateinit var contactAdapter: ContactAdapter


    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loaderManager.initLoader(0, savedInstanceState, this)
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
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    filterContact(newText)
                } else {
                    filterContact()
                }
                return true
            }
        })

        binding.rvContact.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScroll(
                view: AbsListView?,
                firstVisibleItem: Int,
                visibleItemCount: Int,
                totalItemCount: Int
            ) {}

            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
                if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    requireActivity().hideSoftKeyboard()
                    binding.searchView.clearFocus()
                }
            }
        })
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {

        args?.let {
            mSearchString = it.getString(SEARCH_STRING,"%")
        }
        return activity?.let {
            return CursorLoader(
                it,ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                PROJECTION,
                SELECTION,
                arrayOf(mSearchString),
                SORT_ORDER
            )
        } ?: throw IllegalStateException()
    }

    override fun onLoadFinished(loader: Loader<Cursor>, cursor: Cursor) {

        binding.lytShimmer.startShimmer()

        Handler().postDelayed({

            binding.lytShimmer.stopShimmer()
            binding.lytShimmer.visibility = View.GONE

            contactAdapter = ContactAdapter(requireContext(),cursor).apply {
                itemClickListener = { name,phone ->
                    requireActivity().hideSoftKeyboard()
                    binding.searchView.clearFocus()// hide keyboard first
                    MaterialAlertDialogBuilder(context)
                        .setTitle("Are your sure?")
                        .setMessage("Call $name($phone)")
                        .setPositiveButton("Ok"
                        ) { dialog , _ ->
                            val intent = Intent(Intent.ACTION_CALL)
                            intent.data = Uri.parse("tel:$phone")
                            startActivity(intent)
                            dialog.dismiss()
                        }
                        .setNegativeButton("Cancel") { _, _ ->

                        }
                        .show()
                }
            }
            binding.rvContact.adapter = contactAdapter

        },1000)

    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        contactAdapter.swapCursor(null)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_STRING,mSearchString)
    }

    private fun filterContact(searchString : String = "%") {

        mSearchString = "%$searchString%"

        val cursor = requireActivity().contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            PROJECTION,
            SELECTION, arrayOf(mSearchString),
            SORT_ORDER
        )

        contactAdapter.swapCursor(cursor)
    }
}