package com.example.playlistmaker.media.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.InsidePlaylistFragmentBinding
import com.example.playlistmaker.databinding.PlaylistFragmentBinding
import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.media.presentation.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Calendar

class InsidePlaylistFragment: Fragment() {

    private val playListViewModel by viewModel<PlaylistsViewModel>()
    private var playlistId: Long = -1
    private lateinit var binding: InsidePlaylistFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = InsidePlaylistFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistId = arguments?.getLong("playlistId", -1) ?: -1
        Log.d("PlaylistId", playlistId.toString())

        if (playlistId == -1L) {
            findNavController().popBackStack()
            return
        }

        playListViewModel.loadPlaylistById(playlistId)

        playListViewModel.playListById.observe(viewLifecycleOwner) { playlist ->
            playlist?.let { updateUI(it) }
        }

        binding.buttonBack.setOnClickListener{
            findNavController().popBackStack()
        }
    }

    private fun updateUI(playlist: Playlist) {
        binding.titlePlaylist.text = playlist.name
        binding.treckCount.text = playlist.tracksCount.toString()+" треков"
        binding.year.text = playlist.description
        binding.duration.text = "300 минут"

        playlist.coverPath?.let { path ->
            Glide.with(this)
                .load(path)
                .into(binding.showCover)
        }

    }

}