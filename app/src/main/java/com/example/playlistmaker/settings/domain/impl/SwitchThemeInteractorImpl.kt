package com.example.playlistmaker.settings.domain.impl

import com.example.playlistmaker.settings.domain.themeSwitcher.SwitchThemeInteractor
import com.example.playlistmaker.settings.domain.themeSwitcher.SwitchThemeRepository

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