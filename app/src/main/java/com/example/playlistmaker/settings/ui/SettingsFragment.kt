package com.example.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.SearchFragmentBinding
import com.example.playlistmaker.databinding.SettingsFragmentBinding
import com.example.playlistmaker.settings.presentation.SettingsViewModel
import com.google.android.material.switchmaterial.SwitchMaterial
import org.koin.androidx.viewmodel.ext.android.viewModel

const val THEME_PREFERENCES = "theme_preferences"
const val THEME_KEY = "key_for_theme"

class SettingsFragment: Fragment() {

    private lateinit var binding: SettingsFragmentBinding
    private val settingsViewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = SettingsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val shareButton = binding.share

        shareButton.setOnClickListener {
            settingsViewModel.shareApp()
        }

        val supportButton = binding.support

        supportButton.setOnClickListener{
            settingsViewModel.sendEmail()
        }

        val agreeButton = binding.agreement

        agreeButton.setOnClickListener{
            settingsViewModel.openAgreementTerms()
        }
        val themeSwitcher = binding.switcher

        settingsViewModel.getTheme().observe(viewLifecycleOwner) { isChecked ->
            themeSwitcher.isChecked = isChecked
        }

        themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.switchTheme(isChecked)
        }

    }
}


