package com.example.playlistmaker.settings.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.settings.domain.sharedPrefs.SwitchThemeInteractor
import com.example.playlistmaker.sharing.domain.share.SharingInteractor

class SettingsViewModel(private val sharingInteractor: SharingInteractor, private val switcherInteractor: SwitchThemeInteractor): ViewModel() {

//    val sharingInteractor = Creator.provideSharingInteractor()
//    val switcherInteractor = Creator.provideSwitcherInteractor()

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