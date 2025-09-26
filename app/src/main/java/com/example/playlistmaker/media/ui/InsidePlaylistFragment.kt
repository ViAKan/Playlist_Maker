package com.example.playlistmaker.media.ui

import android.app.AlertDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.InsidePlaylistFragmentBinding
import com.example.playlistmaker.databinding.PlaylistFragmentBinding
import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.media.presentation.PlaylistsViewModel
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.TrackAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Locale



class InsidePlaylistFragment: Fragment(), TrackAdapter.Listener {

    private val playListViewModel by viewModel<PlaylistsViewModel>()
    private var playlistId: Long = -1
    private lateinit var binding: InsidePlaylistFragmentBinding
    private val dateFormat by lazy { SimpleDateFormat("mm", Locale.getDefault()) }
    private val trackList = ArrayList<Track>()
    val adapter = TrackAdapter(trackList, this)
    private lateinit var recyclerView: RecyclerView
    private var isClickAllowed = true
    private lateinit var bottomSheetDialog: BottomSheetDialog

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
            playlist?.let {
                updateUI(it)
                playListViewModel.fillData(playlistId)
            }
        }

        binding.buttonBack.setOnClickListener{
            findNavController().popBackStack()
        }
        recyclerView = binding.tracksInPlstRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        playListViewModel.playlistTracks.observe(viewLifecycleOwner){ tracks ->
            trackList.clear()
            trackList.addAll(tracks)
            adapter.updateTracks(tracks)
            if (tracks.isEmpty()) {
                binding.textEmpty.visibility = View.VISIBLE
                binding.tracksInPlstRecyclerView.visibility = View.GONE
            } else {
                binding.textEmpty.visibility = View.GONE
                binding.tracksInPlstRecyclerView.visibility = View.VISIBLE
            }
            val totalDuration = tracks.sumOf { it.trackTimeMillis }
            binding.duration.text = dateFormat.format(totalDuration)+" минут"

        }

        playListViewModel.removeTrackResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is PlaylistsViewModel.RemoveTrackResult.Success -> {
                    Toast.makeText(requireContext(),"Трек успешно удален", Toast.LENGTH_SHORT).show()
                }
                is PlaylistsViewModel.RemoveTrackResult.Error -> {
                    // Показать ошибку
                }
            }
        }
        binding.shareButton.setOnClickListener {
            sharePlaylistContent()
        }
        initBottomSheetDialog()

        binding.moreButton.setOnClickListener {
            updateBottomSheetContent()
            bottomSheetDialog.show()
        }

        playListViewModel.deletePlaylistResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is PlaylistsViewModel.DeletePlaylistResult.Success -> {
                    Toast.makeText(
                        requireContext(),
                        "Успешно удалено",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().popBackStack()
                }
                is PlaylistsViewModel.DeletePlaylistResult.Error -> {
                    Toast.makeText(
                        requireContext(),
                        result.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun updateUI(playlist: Playlist) {
        binding.titlePlaylist.text = playlist.name
        binding.treckCount.text = playlist.tracksCount.toString()+" треков"
        binding.year.text = playlist.description

        playlist.coverPath?.let { path ->
            Glide.with(this)
                .load(path)
                .into(binding.showCover)
        }

    }

    override fun onClick(track: Track) {
        val strTrack = Gson().toJson(track)

        if(clickDebounce()) {
            findNavController().navigate(R.id.action_insidePlaylistFragment_to_playerActivity,
                PlayerActivity.createArgs(strTrack))
        }
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(com.example.playlistmaker.search.ui.CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    override fun onLongClick(track: Track) {
        val confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(requireContext().getString(R.string.delete_track))
            .setNeutralButton(requireContext().getString(R.string.no)) { dialog, which ->
                dialog.dismiss()
            }.setPositiveButton(requireContext().getString(R.string.yes)) { dialog, which ->
                playListViewModel.removeTrackFromPlaylist(playlistId, track.trackId.toLong())
            }
        confirmDialog.show()
    }

    private fun sharePlaylistContent() {
        if (trackList.isEmpty()) {
            Toast.makeText(
                requireContext(),
                requireContext().getString(R.string.no_tracks_to_share),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val playlist = playListViewModel.playListById.value ?: return
            playListViewModel.share(playlist.name, playlist.description, trackList)
        }
    }

    private fun initBottomSheetDialog() {
        bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        bottomSheetDialog.setContentView(R.layout.new_bottom_sheet)

        bottomSheetDialog.behavior.isFitToContents = true
        bottomSheetDialog.behavior.skipCollapsed = true

        bottomSheetDialog.findViewById<TextView>(R.id.share)?.setOnClickListener {
            sharePlaylistContent()
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.findViewById<TextView>(R.id.delete_playlist)?.setOnClickListener {
            showDeletePlaylistDialog()
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.findViewById<TextView>(R.id.edit_playlist)?.setOnClickListener {
            val playlist = playListViewModel.playListById.value ?: return@setOnClickListener
            val fragment = NewPlaylistFragment.newInstance(playlist)
            val bundle = Bundle().apply {
                putParcelable("playlist_arg", playlist)
            }
            findNavController().navigate(
                R.id.action_insidePlaylistFragment_to_newPlaylistFragment,
                bundle
            )
            bottomSheetDialog.dismiss()
        }
    }

    private fun updateBottomSheetContent() {
        val playlist = playListViewModel.playListById.value ?: return

        bottomSheetDialog.findViewById<TextView>(R.id.playlistName)?.text = playlist.name
        bottomSheetDialog.findViewById<TextView>(R.id.count_playlists)?.text =
            "${playlist.tracksCount} треков"

        playlist.coverPath?.let { path ->
            bottomSheetDialog.findViewById<ImageView>(R.id.playlistImg)?.let { imageView ->
                Glide.with(requireContext())
                    .load(path)
                    .into(imageView)
            }
        }
    }

    private fun showDeletePlaylistDialog() {
        val playlist = playListViewModel.playListById.value ?: return
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.want_to_delete)+" \"${playlist.name}\"?")
            .setNegativeButton(getString(R.string.no)) { dialog, _ -> dialog.dismiss() }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                Log.d("DeletePlaylist", "Trying to delete playlist $playlistId")
                playListViewModel.deletePlaylist(playlist.id)
            }
            .show()
    }
}