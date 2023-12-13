package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<ImageButton>(R.id.settings_back_button)
        val shareAppButton = findViewById<TextView>(R.id.shareApp)
        val writeToSupportButton = findViewById<TextView>(R.id.writeToSupport)
        val termsOfUseButton = findViewById<TextView>(R.id.termsOfUse)

        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        shareAppButton.setOnClickListener {
            val shareAppButtonIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getString(R.string.android_developer_link))
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(shareAppButtonIntent, null)
            startActivity(shareIntent)
        }

        writeToSupportButton.setOnClickListener {
            val writeToSupportIntent: Intent = Intent().apply {
                action = Intent.ACTION_SENDTO
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.my_email)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text))
            }
            startActivity(writeToSupportIntent)
        }

        termsOfUseButton.setOnClickListener {
            val termsOfUseIntent: Intent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(getString(R.string.terms_of_use_url))
            }
            startActivity(termsOfUseIntent)
        }

    }
}
