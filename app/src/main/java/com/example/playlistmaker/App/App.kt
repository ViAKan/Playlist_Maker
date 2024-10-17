package com.example.playlistmaker.App

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.SettingsActivity.data.impl.SwitchThemeRepositoryImpl
import com.example.playlistmaker.SettingsActivity.ui.THEME_KEY
import com.example.playlistmaker.SettingsActivity.ui.THEME_PREFERENCES
import com.example.playlistmaker.creator.Creator

const val MODE = "mode"
const val ISDARK = "isDark"
class App : Application() {

    override fun onCreate() {

        val switcherInt = Creator.provideSwitcherInteractor(this)
        val key = switcherInt.getSwitchKey()
        switcherInt.switchTheme(key)
        super.onCreate()

    }

}