package com.wscube_fire_store

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val add = findViewById<FloatingActionButton>(R.id.btnAdd)

        add.setOnClickListener {
            startActivity(Intent())
        }

    }
}