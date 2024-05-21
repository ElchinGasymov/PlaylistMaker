package com.example.playlistmaker.settings.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private val binding: FragmentSettingsBinding by viewBinding(CreateMethod.INFLATE)
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initThemeSwitch()
        initSharingButton()
        initSupportButton()
        initTermsOfUseButton()

        viewModel.isDarkModeOn.observe(viewLifecycleOwner) { isDarkMode ->
            binding.appThemeSwitch.isChecked = isDarkMode
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
