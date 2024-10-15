package com.example.playlistmaker.SearchActivity.data.sharedPreferences

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.SettingsActivity.ui.THEME_KEY
import com.example.playlistmaker.SettingsActivity.ui.THEME_PREFERENCES

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