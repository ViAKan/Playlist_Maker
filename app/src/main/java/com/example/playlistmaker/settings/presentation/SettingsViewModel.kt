package com.example.playlistmaker.settings.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.creator.Creator

class SettingsViewModel(): ViewModel() {

    val sharingInteractor = Creator.provideSharingInteractor()
    val switcherInteractor = Creator.provideSwitcherInteractor()

    private val _isThemeChecked = MutableLiveData<Boolean>()

    fun getTheme(): LiveData<Boolean> = _isThemeChecked

    init {
        _isThemeChecked.value = getSwitchKey()
    }

    fun getSwitchKey(): Boolean {
        return switcherInteractor.getSwitchKey()
    }

    fun switchTheme(isChecked: Boolean) {
        switcherInteractor.switchTheme(isChecked)
        switcherInteractor.saveTheme(isChecked)
        _isThemeChecked.value = isChecked
    }

    fun sendEmail(){
        sharingInteractor.openSupport()
    }

    fun openAgreementTerms(){
        sharingInteractor.openTerms()
    }

    fun shareApp(){
        sharingInteractor.shareApp()
    }

}