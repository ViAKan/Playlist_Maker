package com.example.playlistmaker.settings.domain.sharedPrefs

interface SwitchThemeInteractor {
    fun switchTheme(darkThemeEnabled: Boolean)
    fun saveTheme(checked: Boolean)
    fun getSwitchKey(): Boolean
}