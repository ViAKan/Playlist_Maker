package com.example.playlistmaker.sharing.domain.impl

import com.example.playlistmaker.sharing.data.navigator.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.data.resources.ResourceProvider
import com.example.playlistmaker.sharing.data.resources.ResourceProviderImpl
import com.example.playlistmaker.sharing.domain.model.EmailData
import com.example.playlistmaker.sharing.domain.share.ExternalNavigator
import com.example.playlistmaker.sharing.domain.share.SharingInteractor

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator, private val resourceProvider: ResourceProvider
) : SharingInteractor {

    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink(), getTitle())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return resourceProvider.getShareLink()
    }

    private fun getTitle(): String {
        return resourceProvider.getTitle()
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            email = resourceProvider.getSupportEmail(),
            subject = resourceProvider.getSupportEmailSubject(),
            body = resourceProvider.getSupportEmailBody()
        )
    }

    private fun getTermsLink(): String {
        return resourceProvider.getOfferLink()
    }
}