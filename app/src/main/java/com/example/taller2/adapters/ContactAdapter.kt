package com.example.taller2.adapters

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cursoradapter.widget.CursorAdapter
import com.example.taller2.R
import org.w3c.dom.Text

class ContactAdapter(context: Context?,c: Cursor?, flags:Int ): CursorAdapter(context, c, flags) {
    override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View {
        return LayoutInflater.from(context).inflate(R.layout.contactsrow,parent,false)
    }

    override fun bindView(view: View?, context: Context?, cursor: Cursor?) {
        val tvId = view!!.findViewById<TextView>(R.id.indexID)
        val tvContact = view!!.findViewById<TextView>(R.id.nombreContacto)

        val id = cursor!!.getInt(0)
        val name = cursor!!.getString(1)
        tvId.text = id.toString()
        tvContact.text = name
    }



}