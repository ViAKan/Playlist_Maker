package com.example.playlistmaker.settings.domain.themeSwitcher

interface SwitchThemeInteractor {
    fun switchTheme(darkThemeEnabled: Boolean)
    fun saveTheme(checked: Boolean)
    fun getSwitchKey(): Boolean
}