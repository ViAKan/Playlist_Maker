package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter (
    private val tracks: List<Track>,
    val listener: Listener
) : RecyclerView.Adapter<TrackViewHolder> () {

    val historyList: MutableList<Track> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TrackViewHolder(view)
    }
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position], listener)
    }
    override fun getItemCount(): Int {
        return tracks.size
    }

    interface Listener{
        fun onClick(track: Track)
    }

}
