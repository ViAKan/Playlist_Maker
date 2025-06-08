//package com.example.playlistmaker.media.ui
//
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import com.example.playlistmaker.R
//import com.example.playlistmaker.databinding.ActivityMediaBinding
//import com.google.android.material.tabs.TabLayoutMediator
//
//class MediaActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityMediaBinding
//
//    private lateinit var tabMediator: TabLayoutMediator
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMediaBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        val text1: String = getString(R.string.media_empty)
//        val text2: String = getString(R.string.no_playlists)
//
//        binding.viewPager.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle, text1, text2)
//
//        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
//            when(position) {
//                0 -> tab.text = getString(R.string.tab_likes)
//                1 -> tab.text = getString(R.string.tab_playlists)
//            }
//        }
//        tabMediator.attach()
//
//        binding.buttonBack.setOnClickListener {
//            finish()
//        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        tabMediator.detach()
//    }
//}