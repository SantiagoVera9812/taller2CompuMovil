package com.example.taller2

import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.taller2.adapters.ContactAdapter
import com.example.taller2.databinding.ActivityContactsBinding

class ContactsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactsBinding
    val projection =
        arrayOf(ContactsContract.Profile._ID, ContactsContract.Profile.DISPLAY_NAME_PRIMARY)
    lateinit var adapter: ContactAdapter//Create


    val getSimplePermission = registerForActivityResult(ActivityResultContracts.RequestPermission(),
        ActivityResultCallback { updateUI(it) })
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = ContactAdapter(this, null, 0)
        binding.listContacts.adapter = adapter

        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_DENIED
        ){
            if (shouldShowRequestPermissionRationale(android.Manifest.permission.READ_CONTACTS)) {

                Toast.makeText(this, "Se necesita para...", Toast.LENGTH_LONG).show()
            }
            getSimplePermission.launch(android.Manifest.permission.READ_CONTACTS)} else {
            updateUI(true)
        }
    }
    fun updateUI(permission: Boolean) {

        if (permission) {
            val cursor = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                projection,
                null,
                null,
                null
            )
            adapter.changeCursor(cursor)
        }
    }
}