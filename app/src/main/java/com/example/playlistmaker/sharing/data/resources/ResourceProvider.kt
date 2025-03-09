package com.example.playlistmaker.sharing.data.resources

interface ResourceProvider {
    fun getShareLink(): String
    fun getTitle(): String
    fun getOfferLink(): String
    fun getSupportEmail(): String
    fun getSupportEmailSubject(): String
    fun getSupportEmailBody(): String
}