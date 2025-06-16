package com.example.playlistmaker.media.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.media.domain.model.Playlist

class PlaylistsListAdapter (
    private var playlists: List<Playlist>,
) : RecyclerView.Adapter<PlaylistsSheetViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsSheetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlists_view, parent, false)
        return PlaylistsSheetViewHolder(view)
    }
    override fun onBindViewHolder(holder:PlaylistsSheetViewHolder, position: Int) {
        holder.bind(playlists[position])
    }
    override fun getItemCount(): Int {
        return playlists.size
    }

}