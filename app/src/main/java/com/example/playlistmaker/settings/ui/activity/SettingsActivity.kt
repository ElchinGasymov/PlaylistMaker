package com.example.playlistmaker.settings.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar()
        initThemeSwitch()
        initSharingButton()
        initSupportButton()
        initTermsOfUseButton()

        viewModel.isDarkModeOn.observe(this) { isDarkMode ->
            binding.appThemeSwitch.isChecked = isDarkMode
        }

    }

    private fun initToolbar() {
        binding.settingsToolbar.apply {
            setNavigationOnClickListener {
                finish()
            }
        }
    }

    private fun initThemeSwitch() {
        viewModel.isDarkThemeOn()
        binding.appThemeSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.switchTheme(isChecked)
        }
    }

    private fun initSharingButton() {
        binding.shareAppBtn.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, getString(R.string.android_developer_link))
                type = "text/plain"
                startActivity(Intent.createChooser(this, null))
            }
        }
    }

    private fun initSupportButton() {
        binding.writeToSupportBtn.setOnClickListener {
            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.my_email)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text))
                startActivity(Intent.createChooser(this, null))
            }
        }
    }

    private fun initTermsOfUseButton() {
        binding.termsOfUseBtn.setOnClickListener {
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(getString(R.string.terms_of_use_url))
                startActivity(Intent.createChooser(this, null))
            }
        }
    }

}
