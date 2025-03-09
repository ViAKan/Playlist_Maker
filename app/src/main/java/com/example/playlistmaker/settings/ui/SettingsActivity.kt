package com.example.playlistmaker.settings.ui

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.settings.presentation.SettingsViewModel
import com.google.android.material.switchmaterial.SwitchMaterial

const val THEME_PREFERENCES = "theme_preferences"
const val THEME_KEY = "key_for_theme"

class SettingsActivity : AppCompatActivity() {

    private val settingsViewModel by lazy{
        ViewModelProvider(this)[SettingsViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings2)

        val backButton = findViewById<ImageButton>(R.id.buttonBack)

        backButton.setOnClickListener {
            finish()
        }

        val shareButton = findViewById<FrameLayout>(R.id.share)

        shareButton.setOnClickListener {
            settingsViewModel.shareApp()
        }

        val supportButton = findViewById<FrameLayout>(R.id.support)

        supportButton.setOnClickListener{
            settingsViewModel.sendEmail()
        }

        val agreeButton = findViewById<FrameLayout>(R.id.agreement)

        agreeButton.setOnClickListener{
           settingsViewModel.openAgreementTerms()
        }
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.switcher)

        settingsViewModel.getTheme().observe(this) { isChecked ->
            themeSwitcher.isChecked = isChecked
        }

        themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.switchTheme(isChecked)
        }

    }
}