package com.example.playlistmaker.settings.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.app.App
import com.example.playlistmaker.creator.Creator
import com.google.android.material.switchmaterial.SwitchMaterial

const val THEME_PREFERENCES = "theme_preferences"
const val THEME_KEY = "key_for_theme"

class SettingsActivity : AppCompatActivity() {
    val app = App()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings2)

        val backButton = findViewById<ImageButton>(R.id.buttonBack)

        backButton.setOnClickListener {
            finish()
        }

        val shareButton = findViewById<FrameLayout>(R.id.share)

        shareButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            val link = resources.getString(R.string.shareLink)
            shareIntent.setType("text/plain")
            shareIntent.putExtra(Intent.EXTRA_TEXT, link)
            startActivity(Intent.createChooser(shareIntent, resources.getString(R.string.title)))
        }

        val supportButton = findViewById<FrameLayout>(R.id.support)

        supportButton.setOnClickListener{
            val message = resources.getString(R.string.subject)
            val text = resources.getString(R.string.text)
            val mail = resources.getString(R.string.eMail)
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(mail))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, message)
            supportIntent.putExtra(Intent.EXTRA_TEXT, text)
            startActivity(supportIntent)
        }

        val agreeButton = findViewById<FrameLayout>(R.id.agreement)

        agreeButton.setOnClickListener{
            val agreeIntent = Intent(Intent.ACTION_VIEW, Uri.parse(resources.getString(R.string.offer)))
            startActivity(agreeIntent)
        }
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.switcher)

        val switcherInt = Creator.provideSwitcherInteractor()
        val key = switcherInt.getSwitchKey()
        themeSwitcher.setChecked(key)
        switcherInt.switchTheme(key)
        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            switcherInt.switchTheme(checked)
            switcherInt.saveTheme(checked)

        }
    }
}