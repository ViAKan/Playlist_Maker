package com.example.playlistmaker.sharing.data.resources

import android.content.Context
import com.example.playlistmaker.R

class ResourceProviderImpl(private val context: Context) : ResourceProvider {

    override fun getShareLink(): String {
        return context.getString(R.string.shareLink)
    }

    override fun getTitle(): String {
        return context.getString(R.string.title)
    }

    override fun getOfferLink(): String {
        return context.getString(R.string.offer)
    }

    override fun getSupportEmail(): String {
        return context.getString(R.string.eMail)
    }

    override fun getSupportEmailSubject(): String {
        return context.getString(R.string.subject)
    }

    override fun getSupportEmailBody(): String {
        return context.getString(R.string.text)
    }
}