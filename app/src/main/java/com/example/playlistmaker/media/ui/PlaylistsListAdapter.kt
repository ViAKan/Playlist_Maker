package com.example.playlistmaker.media.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.TrackAdapter.Listener

class PlaylistsListAdapter (
    private var playlists: List<Playlist>, val listener: Listener
) : RecyclerView.Adapter<PlaylistsSheetViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsSheetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlists_view, parent, false)
        return PlaylistsSheetViewHolder(view)
    }
    override fun onBindViewHolder(holder:PlaylistsSheetViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            listener.onClick(playlists[position])
        }
    }
    override fun getItemCount(): Int {
        return playlists.size
    }
    interface Listener{
        fun onClick(playlist: Playlist)
    }

}