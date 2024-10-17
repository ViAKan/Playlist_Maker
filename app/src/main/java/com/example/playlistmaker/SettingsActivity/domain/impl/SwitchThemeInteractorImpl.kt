package com.example.playlistmaker.SettingsActivity.domain.impl

import com.example.playlistmaker.SettingsActivity.domain.sharedPrefs.SwitchThemeInteractor
import com.example.playlistmaker.SettingsActivity.domain.sharedPrefs.SwitchThemeRepository

class SwitchThemeInteractorImpl(val repository: SwitchThemeRepository): SwitchThemeInteractor{

    override fun switchTheme(darkThemeEnabled: Boolean) {
        repository.switchTheme(darkThemeEnabled)
    }

    override fun saveTheme(checked: Boolean) {
       repository.saveTheme(checked)
    }

    override fun getSwitchKey(): Boolean {
        return repository.getSwitchKey()
    }
}