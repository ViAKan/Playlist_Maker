package com.example.playlistmaker.app

import android.app.Application
import com.example.playlistmaker.creator.Creator

const val MODE = "mode"
const val ISDARK = "isDark"
class App : Application() {

    override fun onCreate() {

        super.onCreate()
        Creator.initApplication(this)
        val switcherInt = Creator.provideSwitcherInteractor()
        val key = switcherInt.getSwitchKey()
        switcherInt.switchTheme(key)

    }

}