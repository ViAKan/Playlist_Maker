package com.example.playlistmaker.sharing.data.navigator

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.R
import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.sharing.EmailDataMapper.toDto
import com.example.playlistmaker.sharing.domain.model.EmailData
import com.example.playlistmaker.sharing.domain.share.ExternalNavigator
import java.util.Locale

class ExternalNavigatorImpl(val context: Context) : ExternalNavigator {

    override fun shareLink(link:String, title:String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain")
        shareIntent.putExtra(Intent.EXTRA_TEXT, link)
        val chooserIntent = Intent.createChooser(shareIntent, title).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(chooserIntent)
    }

    override fun openLink(link:String) {
        val agreeIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        agreeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(agreeIntent)
    }

    override fun openEmail(emailData: EmailData) {
        val emailDataDto = emailData.toDto()
        val message = emailDataDto.subject
        val text = emailDataDto.body
        val mail = emailDataDto.email
        val supportIntent = Intent(Intent.ACTION_SENDTO)
        supportIntent.data = Uri.parse("mailto:")
        supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(mail))
        supportIntent.putExtra(Intent.EXTRA_SUBJECT, message)
        supportIntent.putExtra(Intent.EXTRA_TEXT, text)
        supportIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(supportIntent)
    }

    override fun sharePlaylist(playlistName: String, description: String, tracks: List<TrackDto>) {
        val shareText = buildPlaylistShareText(playlistName, description, tracks)
        shareLink(shareText, context.getString(R.string.share_playlist))
    }

    private fun buildPlaylistShareText(
        playlistName: String,
        description: String,
        tracks: List<TrackDto>
    ): String {
        return buildString {
            append(playlistName).append("\n")
            append(description).append("\n")
            append("[${tracks.size}] треков").append("\n\n")

            tracks.forEachIndexed { index, track ->
                append("${index + 1}. ${track.artistName} - ${track.trackName} (${formatTrackTime(track.trackTimeMillis)})\n")
            }
        }
    }

    private fun formatTrackTime(millis: Long): String {
        val totalSeconds = millis / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }
}