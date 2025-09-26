package com.example.playlistmaker.player.ui

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.HostActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.media.presentation.LikesViewModel
import com.example.playlistmaker.media.presentation.PlaylistsViewModel
import com.example.playlistmaker.media.ui.NewPlaylistFragment
import com.example.playlistmaker.media.ui.PlaylistAdapter
import com.example.playlistmaker.media.ui.PlaylistsListAdapter
import com.example.playlistmaker.player.presentation.PlayerViewModel
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.presentation.SearchViewModel
import com.example.playlistmaker.search.ui.NAME_TRACK
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity(), PlaylistsListAdapter.Listener {

    private lateinit var play: ImageButton
    private lateinit var currentTime: TextView
    private lateinit var likeButton: ImageButton

    private val playerViewModel by viewModel<PlayerViewModel>()
    private val playListViewModel by viewModel<PlaylistsViewModel>()

    private lateinit var adapter: PlaylistsListAdapter
    private lateinit var playlists: ArrayList<Playlist>
    lateinit var fragmentCont: FragmentContainerView
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var track: Track
    private lateinit var playlistName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_player)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        playlists = ArrayList()
        adapter = PlaylistsListAdapter(playlists,this)

        playListViewModel.loadAllPlaylists()

        playListViewModel.observePlaylists().observe(this) { newPlaylists ->
            playlists.clear()
            playlists.addAll(newPlaylists)
            adapter.notifyDataSetChanged()
            Log.d("PlaylistsPlayer", playlists.toString())
        }

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenHeight = displayMetrics.heightPixels
        val halfScreenHeight = (screenHeight * 0.5).toInt()
        fragmentCont = findViewById(R.id.fragment_container)

        bottomSheetDialog = BottomSheetDialog(this).apply {
            setContentView(R.layout.bottom_sheet_layout)
            behavior.isFitToContents = true
            behavior.skipCollapsed = true
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            window?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)?.setBackgroundResource(android.R.color.transparent)
            val recyclerView = findViewById<RecyclerView>(R.id.playlistsRecyclerView)
            recyclerView?.let {
                it.layoutManager = LinearLayoutManager(this@PlayerActivity)
                it.adapter = this@PlayerActivity.adapter
                it.setHasFixedSize(true)
            } ?: run {
                Log.e("PlayerActivity", "RecyclerView not found in bottom sheet")
            }

            findViewById<Button>(R.id.btnNewPlaylist)?.setOnClickListener {
                dismiss()
                fragmentCont.visibility = View.VISIBLE
                findNavController(R.id.fragment_container).navigate(R.id.action_global_newPlaylistFragment)
            }
        }

        bottomSheetDialog.setOnShowListener {
            val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as? FrameLayout
            bottomSheet?.let { sheet ->
                val shapeAppearanceModel = ShapeAppearanceModel.builder(
                    sheet.context,
                    0,
                    R.style.BottomSheetCorners
                ).build()

                ViewCompat.setBackground(sheet, MaterialShapeDrawable(shapeAppearanceModel).apply {
                    fillColor = ColorStateList.valueOf(Color.WHITE)
                    elevation = 0f
                })
            }
            bottomSheet?.viewTreeObserver?.addOnGlobalLayoutListener {
                val maxHeight = (resources.displayMetrics.heightPixels * 0.6).toInt()
                bottomSheet.layoutParams.height = maxHeight
                bottomSheet.requestLayout()
            }
        }

        findViewById<ImageButton>(R.id.addToPlaylist).setOnClickListener {
            bottomSheetDialog.show()
        }

        val backButton = findViewById<ImageButton>(R.id.buttonBack)

        backButton.setOnClickListener {
            finish()
        }

        val title = findViewById<TextView>(R.id.title)
        val author = findViewById<TextView>(R.id.author)
        val durationSong = findViewById<TextView>(R.id.durationSong)
        currentTime = findViewById<TextView>(R.id.currentTime)
        val albumSong = findViewById<TextView>(R.id.albumSong)
        val yearSong = findViewById<TextView>(R.id.yearSong)
        val genreSong = findViewById<TextView>(R.id.genreSong)
        val countrySong = findViewById<TextView>(R.id.countrySong)
        val cover = findViewById<ImageView>(R.id.cover)
        var imgSource: String = ""
        val type = object : TypeToken<Track>() {}.type
        track = Gson().fromJson<Track>(intent.getStringExtra(NAME_TRACK), type)
        likeButton = findViewById<ImageButton>(R.id.addToLikes)
        title.text = track.trackName
        author.text = track.artistName
        durationSong.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        albumSong.text = track.collectionName
        yearSong.text = track.releaseDate.substring(0,4)
        genreSong.text = track.primaryGenreName
        countrySong.text = track.country
        imgSource = track.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
        Glide.with(applicationContext)
            .load(imgSource)
            .centerInside()
            .transform(RoundedCorners(8))
            .placeholder(R.drawable.placeholder_max)
            .into(cover)

        play = findViewById(R.id.buttonPlay)
        val url = track.previewUrl
        playerViewModel.preparePlayer(url)
        Log.d("PlayerPP", "Player state: ${playerViewModel.getState().value}")
        Log.d("PlayerPP3", "${track.artworkUrl100}")
        play.setOnClickListener {
            playerViewModel.playbackControl()
            Log.d("PlayerPP1", "Player state: ${playerViewModel.getState().value}")
        }

        playerViewModel.checkInitialLikeState(track.trackId)

        playerViewModel.getLike().observe(this) { isLiked ->
            likeButton.setImageResource(
                if (isLiked) R.drawable.heart else R.drawable.add_to_likes
            )
        }
        likeButton.setOnClickListener {
            playerViewModel.toggleLike(track)
        }

        playerViewModel.getState().observe(this){ state ->
            currentTime.text = state.currentTime
            play.isEnabled = state.isPlayEnabled
            when (state.state) {
                PlayerViewModel.STATE_PLAYING -> play.setImageResource(R.drawable.pause)
                PlayerViewModel.STATE_PAUSED, PlayerViewModel.STATE_PREPARED -> play.setImageResource(R.drawable.button_play)
            }
        }
        playListViewModel.addTrackResult.observe(this) { result ->
            when (result) {
                is PlaylistsViewModel.AddTrackResult.Success -> {
                    Toast.makeText(this, "Добавлено в плейлист $playlistName", Toast.LENGTH_SHORT).show()
                    bottomSheetDialog.dismiss()
                }
                is PlaylistsViewModel.AddTrackResult.Error -> {
                    Toast.makeText(this, "Трек уже добавлен в плейлист $playlistName", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        playerViewModel.pausePlayer()
    }

    companion object {
        fun createArgs(strTrack: String): Bundle =
            bundleOf(NAME_TRACK to strTrack)
    }

    override fun onClick(playlist: Playlist) {
        playlistName = playlist.name
        playListViewModel.addTrackToPlaylist(playlist, track)
    }

}