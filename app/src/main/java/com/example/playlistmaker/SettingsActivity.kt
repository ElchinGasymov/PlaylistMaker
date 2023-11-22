package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<ImageButton>(R.id.settings_back_button)

        backButton.setOnClickListener {
            val backButtonIntent = Intent(this, MainActivity::class.java)
            startActivity(backButtonIntent)
        }
    }
}
