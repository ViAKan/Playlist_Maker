package com.example.playlistmaker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.LikesFragmentBinding
import com.example.playlistmaker.media.presentation.LikesViewModel
import com.example.playlistmaker.media.presentation.PlaylistsViewModel
import com.example.playlistmaker.media.ui.PlaylistFragment.Companion
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class LikesFragment : Fragment() {

    companion object {
        private const val LIKES = "likes_state"

        fun newInstance(text: String) = LikesFragment().apply {
            arguments = Bundle().apply {
                putString(LIKES, text)
            }
        }
    }

    private val likesViewModel: LikesViewModel by viewModel {
        parametersOf(requireArguments().getString(LIKES))
    }

    private lateinit var binding: LikesFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = LikesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        likesViewModel.observe().observe(viewLifecycleOwner) {
            showMedia(it)
        }
    }

    fun showMedia(text: String){
        binding.mediaState.text = text
    }

}