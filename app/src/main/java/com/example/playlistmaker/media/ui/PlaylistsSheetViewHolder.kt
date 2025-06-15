package com.example.playlistmaker.media.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.media.domain.model.Playlist
import java.io.File

class PlaylistsSheetViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val playlistName: TextView = itemView.findViewById(R.id.playlistName)
    private val number: TextView = itemView.findViewById(R.id.count_playlists)
    private var imgSource: String = ""
    private val image: ImageView = itemView.findViewById(R.id.playlistImg)

    fun bind(model: Playlist) {
        playlistName.text = model.name
        number.text = model.tracksCount.toString()+" треков"
        val coverPath = model.coverPath ?: ""
        if (coverPath.isNotEmpty()) {
            val imageFile = File(coverPath)
            Glide.with(itemView)
                .load(imageFile) // или .load(imageUri)
                .centerInside()
                .transform(RoundedCorners(8))
                .placeholder(R.drawable.placeholder_max)
                .into(image)
        } else {
            image.setImageResource(R.drawable.placeholder_max)
        }
//        imgSource = model.coverPath.toString()
//        Glide.with(itemView)
//            .load(imgSource)
//            .centerInside()
//            .transform(RoundedCorners(8))
//            .placeholder(R.drawable.placeholder_max)
//            .into(image)
    }

}