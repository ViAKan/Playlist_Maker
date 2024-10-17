package com.example.playlistmaker.SettingsActivity.domain.sharedPrefs

interface SwitchThemeInteractor {
    fun switchTheme(darkThemeEnabled: Boolean)
    fun saveTheme(checked: Boolean)
    fun getSwitchKey(): Boolean
}