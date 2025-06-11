package com.example.playlistmaker.media.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.LikesFragmentBinding
import com.example.playlistmaker.media.domain.model.LikesState
import com.example.playlistmaker.media.presentation.LikesViewModel
import com.example.playlistmaker.media.presentation.PlaylistsViewModel
import com.example.playlistmaker.media.ui.PlaylistFragment.Companion
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.presentation.SearchViewModel
import com.example.playlistmaker.search.ui.TrackAdapter
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

private const val CLICK_DEBOUNCE_DELAY = 1000L

class LikesFragment : Fragment(), TrackAdapter.Listener {

    companion object {
        private const val LIKES = "likes_state"

        fun newInstance(text: String) = LikesFragment().apply {
            arguments = Bundle().apply {
                putString(LIKES, text)
            }
        }
    }

    private val likesViewModel by viewModel<LikesViewModel>()
    private lateinit var adapter: TrackAdapter

    private var isClickAllowed = true

    private lateinit var placeholderMessage: TextView
    private lateinit var placeholderImage: ImageView
    private lateinit var likesList: RecyclerView
    private lateinit var invisibleButton: AppCompatButton
    private lateinit var trackList: ArrayList<Track>

    private lateinit var binding: LikesFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = LikesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        trackList = ArrayList()
        adapter = TrackAdapter(trackList, this)

        placeholderMessage = binding.mediaState
        likesList = binding.recyclerViewLikes
        placeholderImage = binding.placeholderImg
        invisibleButton = binding.btnNewPlaylist

        likesList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        likesList.adapter = adapter

        likesViewModel.fillData()
        adapter.notifyDataSetChanged()

        likesViewModel.observeState().observe(viewLifecycleOwner) {history ->
            Log.d("LIKES", history.size.toString())
            if(history.isNotEmpty()){
                trackList.clear()
                trackList.addAll(history)
                adapter.updateTracks(history)
                likesList.visibility = View.VISIBLE
                placeholderMessage.visibility = View.GONE
                placeholderImage.visibility = View.GONE
                invisibleButton.visibility = View.GONE
            }
            else{
                likesList.visibility = View.GONE
                placeholderMessage.visibility = View.VISIBLE
                placeholderMessage.text = requireArguments().getString(LIKES).toString()
                placeholderImage.visibility = View.VISIBLE
                invisibleButton.visibility = View.INVISIBLE
            }
        }
//        likesViewModel.fillData()

//        likesViewModel.observeState().observe(viewLifecycleOwner) {state ->
//            if(state is LikesState.Content){
//                likesList.visibility = View.VISIBLE
//                placeholderMessage.visibility = View.GONE
//                placeholderImage.visibility = View.GONE
//            }
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        likesList.adapter = null
    }

//    private fun render(state: LikesState) {
//        when (state) {
//            is LikesState.Content -> showContent(state.tracks!!)
//            is LikesState.Empty -> showEmpty(state.message!!)
//        }
//    }
//
//    private fun showEmpty(message: String) {
//        likesList.visibility = View.GONE
//        placeholderMessage.visibility = View.VISIBLE
//        placeholderImage.visibility = View.VISIBLE
//        placeholderMessage.text = message
//    }
//
//    private fun showContent(tracks: List<Track>) {
//        likesList.visibility = View.VISIBLE
//        placeholderMessage.visibility = View.GONE
//        placeholderImage.visibility = View.GONE
//        trackList.clear()
//        trackList.addAll(tracks)
//        adapter?.updateTracks(tracks)
//    }

    override fun onClick(track: Track) {
        val strTrack = Gson().toJson(track)

        if(clickDebounce()) {
            findNavController().navigate(R.id.action_mediaFragment_to_playerActivity2,
                PlayerActivity.createArgs(strTrack))
            Log.d("PlayerVM2", "Is favorite: ${track.isFavorite}")
        }
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

}