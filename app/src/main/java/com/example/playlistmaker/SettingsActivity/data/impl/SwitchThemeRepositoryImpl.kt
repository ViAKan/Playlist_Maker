package com.example.playlistmaker.SettingsActivity.data.impl

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.SettingsActivity.domain.sharedPrefs.SwitchThemeRepository
import com.example.playlistmaker.SettingsActivity.ui.THEME_KEY
import com.example.playlistmaker.SettingsActivity.ui.THEME_PREFERENCES

class SwitchThemeRepositoryImpl(val context: Context): SwitchThemeRepository {

    var darkTheme = false
    val sharedPrefs = context.getSharedPreferences(THEME_PREFERENCES, Context.MODE_PRIVATE)
    val key = sharedPrefs.getBoolean(THEME_KEY, darkTheme)

    override fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    override fun saveTheme(checked: Boolean) {

        sharedPrefs.edit()
            .putBoolean(THEME_KEY, checked)
            .apply()

    }

    override fun getSwitchKey(): Boolean {
        return key
    }
}