package com.example.playlistmaker.SettingsActivity.domain.sharedPrefs

interface SwitchThemeRepository {
    fun switchTheme(darkThemeEnabled: Boolean)
    fun saveTheme(checked: Boolean)
    fun getSwitchKey(): Boolean
}