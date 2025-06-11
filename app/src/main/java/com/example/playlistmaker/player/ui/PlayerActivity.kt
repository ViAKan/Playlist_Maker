package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.media.presentation.LikesViewModel
import com.example.playlistmaker.player.presentation.PlayerViewModel
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.presentation.SearchViewModel
import com.example.playlistmaker.search.ui.NAME_TRACK
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var play: ImageButton
    private lateinit var currentTime: TextView
    private lateinit var likeButton: ImageButton

    private val playerViewModel by viewModel<PlayerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_player)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
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
        val track = Gson().fromJson<Track>(intent.getStringExtra(NAME_TRACK), type)
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
        play.setOnClickListener {
            playerViewModel.playbackControl()
        }

//        likeButton.setOnClickListener {
//            lifecycleScope.launch {
//                val isLiked = playerViewModel.isTrackLiked(track.trackId)
//                if (isLiked) {
//                    playerViewModel.removeFromLikes(track)
//                } else {
//                    playerViewModel.addToLikes(track)
//                }
//                // Обновляем кнопку после изменения
//                val newState = playerViewModel.isTrackLiked(track.trackId)
//                likeButton.setImageResource(
//                    if (newState) R.drawable.heart else R.drawable.add_to_likes
//                )
//            }
//        }
//
//        lifecycleScope.launch {
//            val isLiked = playerViewModel.isTrackLiked(track.trackId)
//            likeButton.setImageResource(
//                if (isLiked) R.drawable.heart else R.drawable.add_to_likes
//            )
//        }

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
    }

    override fun onPause() {
        super.onPause()
        playerViewModel.pausePlayer()
    }

    companion object {
        fun createArgs(strTrack: String): Bundle =
            bundleOf(NAME_TRACK to strTrack)
    }

}