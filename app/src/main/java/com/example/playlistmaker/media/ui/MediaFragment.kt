package com.example.playlistmaker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediaBinding
import com.example.playlistmaker.databinding.LikesFragmentBinding
import com.example.playlistmaker.media.presentation.LikesViewModel
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MediaFragment: Fragment() {

    private lateinit var binding: ActivityMediaBinding

    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityMediaBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val text1: String = getString(R.string.media_empty)
        val text2: String = getString(R.string.no_playlists)

        binding.viewPager.adapter =
            ViewPagerAdapter(requireActivity().supportFragmentManager, lifecycle, text1, text2)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.tab_likes)
                1 -> tab.text = getString(R.string.tab_playlists)
            }
        }
        tabMediator.attach()
    }


    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}

