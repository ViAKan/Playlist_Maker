package com.example.playlistmaker

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val artistName: TextView = itemView.findViewById(R.id.author)
    private val trackTime: TextView = itemView.findViewById(R.id.trackTime)
    private var imgSource: String = ""
    private val image: ImageView = itemView.findViewById(R.id.albumImg)

    fun bind(model: Track) {
        trackName.text = model.trackName
        artistName.text = model.artistName
        imgSource = model.artworkUrl100
        trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeMillis)
        Glide.with(itemView)
            .load(imgSource)
            .centerInside()
            .transform(RoundedCorners(2))
            .placeholder(R.drawable.placeholder)
            .into(image)
    }

}