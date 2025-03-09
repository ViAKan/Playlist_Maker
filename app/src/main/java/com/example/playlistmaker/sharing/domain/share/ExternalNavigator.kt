package com.example.playlistmaker.sharing.domain.share

import com.example.playlistmaker.sharing.domain.model.EmailData


interface ExternalNavigator {
    fun shareLink(link:String, title:String)
    fun openLink(link:String)
    fun openEmail(emailData: EmailData)
}