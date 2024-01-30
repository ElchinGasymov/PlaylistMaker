package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private val PREFERENCES = "playlistmaker_preferences"
    private val APP_THEME_KEY = "key_for_app_theme"

    private val sharedPrefs by lazy {
        getSharedPreferences(PREFERENCES, MODE_PRIVATE)
    }

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.appThemeSwitch.isChecked = sharedPrefs.getBoolean(APP_THEME_KEY, false)

        binding.settingsBackBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.shareAppBtn.setOnClickListener {
            val shareAppButtonIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getString(R.string.android_developer_link))
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(shareAppButtonIntent, null)
            startActivity(shareIntent)
        }

        binding.writeToSupportBtn.setOnClickListener {
            val writeToSupportIntent: Intent = Intent().apply {
                action = Intent.ACTION_SENDTO
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.my_email)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text))
            }
            startActivity(writeToSupportIntent)
        }

        binding.termsOfUseBtn.setOnClickListener {
            val termsOfUseIntent: Intent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(getString(R.string.terms_of_use_url))
            }
            startActivity(termsOfUseIntent)
        }

        binding.appThemeSwitch.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
            sharedPrefs.edit().putBoolean(APP_THEME_KEY, checked).apply()
        }

    }
}
