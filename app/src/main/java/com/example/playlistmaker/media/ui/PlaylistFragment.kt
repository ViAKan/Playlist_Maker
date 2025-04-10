package com.example.playlistmaker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.LikesFragmentBinding
import com.example.playlistmaker.databinding.PlaylistFragmentBinding
import com.example.playlistmaker.media.presentation.LikesViewModel
import com.example.playlistmaker.media.presentation.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistFragment : Fragment() {

    companion object {
        private const val PLAYLISTS = "playlists_state"

        fun newInstance(text: String) = PlaylistFragment().apply {
            arguments = Bundle().apply {
                putString(PLAYLISTS, text)
            }
        }
    }

    private val playlistsViewModel: LikesViewModel by viewModel {
        parametersOf(requireArguments().getString(PLAYLISTS))
    }

    private lateinit var binding: PlaylistFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = PlaylistFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistsViewModel.observe().observe(viewLifecycleOwner) {
            showMedia(it)
        }
    }

    fun showMedia(text: String){
        binding.mediaState.text = text
    }

}