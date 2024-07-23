package com.example.playlistmaker

import android.app.Application
import android.content.res.Resources
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.switchmaterial.SwitchMaterial

const val MODE = "mode"
const val ISDARK = "isDark"
class App : Application() {
    var darkTheme = false
    override fun onCreate() {
        val sharedPrefs = getSharedPreferences(THEME_PREFERENCES, MODE_PRIVATE)
        val key = sharedPrefs.getBoolean(THEME_KEY, (applicationContext as App).darkTheme)
        switchTheme(key)
        super.onCreate()
    }
    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}