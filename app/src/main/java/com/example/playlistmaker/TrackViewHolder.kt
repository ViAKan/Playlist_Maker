package com.example.playlistmaker

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val artistName: TextView = itemView.findViewById(R.id.author)
    private val trackTime: TextView = itemView.findViewById(R.id.trackTime)
    private var imgSource: String = ""
    private val image: ImageView = itemView.findViewById(R.id.albumImg)

    fun bind(model: Track) {
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = model.trackTime
        imgSource = model.artworkUrl100

        if(trackName.text.length > 30){
            trackName.text = trackName.text.substring(0,30) + "..."
        }
        if(artistName.text.length > 30){
            artistName.text = artistName.text.substring(0,30) + "..."
        }

        Glide.with(itemView)
            .load(imgSource)
            .centerInside()
            .transform(RoundedCorners(2))
            .placeholder(R.drawable.placehold)
            .into(image)
    }

}