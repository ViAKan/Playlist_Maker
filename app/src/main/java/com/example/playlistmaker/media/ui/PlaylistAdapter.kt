package com.example.playlistmaker.media.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.media.ui.PlaylistsListAdapter.Listener

class PlaylistAdapter (
    private var playlists: List<Playlist>, val listener: Listener
) : RecyclerView.Adapter<PlaylistViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_covers_view, parent, false)
        return PlaylistViewHolder(view)
    }
    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
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
