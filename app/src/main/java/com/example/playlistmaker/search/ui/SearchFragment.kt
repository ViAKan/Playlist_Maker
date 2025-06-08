package com.example.playlistmaker.search.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.LikesFragmentBinding
import com.example.playlistmaker.databinding.SearchFragmentBinding
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.presentation.SearchViewModel
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

const val HISTORY_PREFS = "history_prefs"
const val HISTORY_KEY = "key_for_hist"
const val NAME_TRACK = "name"
const val SEARCH_DEBOUNCE_DELAY = 2000L
private const val CLICK_DEBOUNCE_DELAY = 1000L

class SearchFragment: Fragment(), TrackAdapter.Listener {

    private var searchQuery: CharSequence? = null
    private val trackList = ArrayList<Track>()
    val adapter = TrackAdapter(trackList, this)
    private lateinit var placeholderMessage: TextView
    private lateinit var additionalMes: TextView
    private lateinit var placeholderImg: ImageView
    private lateinit var inputEditText: EditText
    private lateinit var updateBtn: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var historyView: LinearLayout
    private lateinit var historyRecyclerView: RecyclerView

    private lateinit var historyAdapter: TrackAdapter
    private lateinit var historyList: ArrayList<Track>


    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true

    private val searchViewModel by viewModel<SearchViewModel>()

    private lateinit var binding: SearchFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = SearchFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        historyView = binding.historyView
        val btn_clear_hist = binding.btnClearHistory
        inputEditText = binding.inputEditText
        val clearButton = binding.clearIcon
        updateBtn = binding.btnUpdate
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        progressBar = binding.progressBar
        placeholderImg = binding.placeholderImg
        placeholderMessage = binding.placeholderMessage
        additionalMes = binding.additionalMes

        historyView.visibility = View.GONE

        historyList = ArrayList()
        historyAdapter = TrackAdapter(historyList, this)

        Log.d("beforeclear", historyList.toString())
        historyRecyclerView = binding.recyclerViewHistory
        historyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        historyRecyclerView.adapter = historyAdapter

        searchViewModel.historyLiveData.observe(viewLifecycleOwner) { history ->
            historyList.clear()
            historyList.addAll(history)
            historyAdapter.updateTracks(history)
            Log.d("SearchActivity", "History updated: ${history.size} items")
        }

        searchViewModel.getScreenState().observe(viewLifecycleOwner){ state ->
            adapter.updateTracks(state.trackList)
            if(state.error){
                progressBar.visibility = View.GONE
                showMessage(getString(R.string.something_went_wrong), getString(R.string.additional_message))
                updateBtn.visibility = View.VISIBLE
            }
            else{
                if(state.trackList.isNotEmpty()){
                    progressBar.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    trackList.clear()
                }
                else if(state.trackList.isEmpty() && inputEditText.text.isNotEmpty()){
                    showMessage(getString(R.string.nothing_found), "")
                }
                else{
                    showMessage("", "")
                }
            }
        }

        btn_clear_hist.setOnClickListener{
            historyView.visibility = View.GONE
            searchViewModel.clearHistory()
            Log.d("clear", historyList.toString())
        }

        clearButton.setOnClickListener {
            trackList.clear()
            hide()
            inputEditText.setText("")
            val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchQuery = s
                clearButton.visibility = clearButtonVisibility(s)
                historyView.visibility = if (inputEditText.hasFocus() && s?.isEmpty() == true && historyList.isNotEmpty()) View.VISIBLE else View.GONE
                progressBar.visibility = if (inputEditText.hasFocus() && s?.isNotEmpty() == true) View.VISIBLE else View.GONE
                hide()
                searchViewModel.searchDebounce(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                searchViewModel.search(inputEditText.text.toString())
                true
            }
            false
        }
        updateBtn.setOnClickListener{
            progressBar.visibility = View.VISIBLE
            hide()
            searchViewModel.searchDebounce(inputEditText.text.toString())
        }
        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            historyView.visibility = if (hasFocus && inputEditText.text.isEmpty() && historyList.isNotEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_NAME, searchQuery.toString())
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
    companion object {
        private const val SEARCH_NAME = "NAME"
    }
    private fun showMessage(text: String, additionalMessage: String) {
        placeholderMessage = binding.placeholderMessage
        additionalMes = binding.additionalMes
        placeholderImg = binding.placeholderImg
        placeholderImg.setImageResource(R.drawable.nothing_found_img)
        if (text.isNotEmpty()) {
            placeholderMessage.visibility = View.VISIBLE
            placeholderImg.visibility = View.VISIBLE
            additionalMes.visibility = View.GONE
            trackList.clear()
            adapter.notifyDataSetChanged()
            placeholderMessage.text = text
            if(!additionalMessage.equals("")){
                additionalMes.visibility = View.VISIBLE
                placeholderImg.setImageResource(R.drawable.internet_problem)
                additionalMes.text = additionalMessage
            }
        } else {
            placeholderMessage.visibility = View.GONE
            additionalMes.visibility = View.GONE
            placeholderImg.visibility = View.GONE
        }
    }


    override fun onClick(track: Track) {

//        val displayIntent = Intent(requireContext(), PlayerActivity::class.java)
        val strTrack = Gson().toJson(track)

        if(clickDebounce()) {
//            displayIntent.putExtra(NAME_TRACK, strTrack)
//            startActivity(displayIntent)
            findNavController().navigate(R.id.action_searchFragment_to_playerActivity,
                PlayerActivity.createArgs(strTrack))
            searchViewModel.addTrackToHistory(track)
            Log.d("cleadd", historyList.toString())
        }
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun hide(){
        recyclerView.visibility = View.GONE
        placeholderMessage.visibility = View.GONE
        additionalMes.visibility = View.GONE
        placeholderImg.visibility = View.GONE
        updateBtn.visibility = View.GONE
    }

}