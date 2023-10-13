package com.example.taller2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.taller2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.camara.setOnClickListener{

            startActivity(Intent(this, GalleryActivity::class.java))
        }

        binding.contactos.setOnClickListener{
            startActivity(Intent(this, ContactsActivity::class.java))
        }

        binding.osmap.setOnClickListener{
            startActivity(Intent(this, OsmapActivity::class.java))
        }
    }
}