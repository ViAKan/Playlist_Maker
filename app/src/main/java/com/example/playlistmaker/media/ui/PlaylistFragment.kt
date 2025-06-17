package com.example.playlistmaker.media.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistFragmentBinding
import com.example.playlistmaker.media.presentation.PlaylistsViewModel
import com.example.playlistmaker.media.domain.model.Playlist
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {

    companion object {
        private const val PLAYLISTS = "playlists_state"

        fun newInstance(text: String) = PlaylistFragment().apply {
            arguments = Bundle().apply {
                putString(PLAYLISTS, text)
            }
        }
    }

    private val playListViewModel by viewModel<PlaylistsViewModel>()
    private lateinit var adapter: PlaylistAdapter
    private lateinit var playlists: ArrayList<Playlist>
    private lateinit var binding: PlaylistFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = PlaylistFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlists = ArrayList()
        adapter = PlaylistAdapter(playlists)
        binding.recyclerView2.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView2.adapter = adapter

        showMedia(requireArguments().getString(PLAYLISTS).toString())
        binding.btnNewPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_mediaFragment_to_newPlaylistFragment)
        }

        playListViewModel.loadAllPlaylists()

        playListViewModel.observePlaylists().observe(viewLifecycleOwner){ playlist ->
            if(playlist.isNotEmpty()){
                playlists.clear()
                playlists.addAll(playlist)
                adapter.notifyDataSetChanged()
                binding.recyclerView2.visibility = View.VISIBLE
                binding.mediaState.visibility= View.GONE
                binding.placeholderImg.visibility = View.GONE
            }
            else{
                binding.recyclerView2.visibility = View.GONE
                binding.mediaState.visibility= View.VISIBLE
                showMedia(requireArguments().getString(PLAYLISTS).toString())
                binding.placeholderImg.visibility = View.VISIBLE
            }
            Log.d("PLAYLISTS", playlists.toString())
        }
    }

    fun showMedia(text: String){
        binding.mediaState.text = text
    }

}