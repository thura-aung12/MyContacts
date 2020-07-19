package com.thuraaung.mycontacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ContactAdapter : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    private val contactList = mutableListOf<ContactModel>()
    private var itemClickCallback : ((ContactModel) -> Unit)? = null

    inner class  ContactViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        private val tvName : TextView = view.findViewById(R.id.tv_name)
        private val tvPhone : TextView = view.findViewById(R.id.tv_phone)

        init {
            view.setOnClickListener {
                itemClickCallback?.invoke(contactList[adapterPosition])
            }
        }

        fun bind(contact : ContactModel) {
            tvName.text = contact.name
            tvPhone.text = contact.phoneNumber
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_contact_item,parent,false)
        return ContactViewHolder(view)
    }

    override fun getItemCount(): Int = contactList.size

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(contactList[position])
    }

    fun setItemClickListener(listener : (ContactModel) -> Unit) {
        itemClickCallback = listener
    }

    fun refreshContact(newContacts : List<ContactModel>) {
        contactList.clear()
        contactList.addAll(newContacts)
        notifyDataSetChanged()
    }
}