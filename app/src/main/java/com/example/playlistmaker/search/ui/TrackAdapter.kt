package com.example.playlistmaker.search.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.search.domain.models.Track

class TrackAdapter (
    private var tracks: List<Track>,
    val listener: Listener
) : RecyclerView.Adapter<TrackViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TrackViewHolder(view)
    }
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            listener.onClick(tracks[position])
        }
        holder.itemView.setOnLongClickListener {
            listener.onLongClick(tracks[position])
            true
        }
    }
    override fun getItemCount(): Int {
        return tracks.size
    }

    fun updateTracks(newTracks: List<Track>) {
        tracks = newTracks
        Log.d("clearTr", tracks.toString())
        notifyDataSetChanged()
    }

    interface Listener{
        fun onClick(track: Track)
        fun onLongClick(track: Track)
    }
}
