package com.example.playlistmaker.SearchActivity.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.PlayerActivity.ui.PlayerActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.SearchActivity.domain.sharedPrefs.HistoryInteractor
import com.example.playlistmaker.SearchActivity.domain.models.Track
import com.example.playlistmaker.SearchActivity.domain.api.TrackInteractor
import com.example.playlistmaker.SearchActivity.domain.models.ConsumerData
import com.google.gson.Gson

const val HISTORY_PREFS = "history_prefs"
const val HISTORY_KEY = "key_for_hist"
const val NAME_TRACK = "name"
private const val SEARCH_DEBOUNCE_DELAY = 2000L
private const val CLICK_DEBOUNCE_DELAY = 1000L

class SearchActivity : AppCompatActivity(), TrackAdapter.Listener {
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

//    private lateinit var historyList:ArrayList<Track>
    private lateinit var historyAdapter: TrackAdapter
//    private var historyList:ArrayList<Track> = arrayListOf()
//    private var historyAdapter: TrackAdapter = TrackAdapter(historyList, this)

    private val searchRunnable = Runnable { searchRequest() }

    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true

    private val provideTrackInteractor = Creator.provideTrackInteractor()

    private lateinit var historyInteractor: HistoryInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        historyView = findViewById<LinearLayout>(R.id.historyView)
        val btn_clear_hist = findViewById<AppCompatButton>(R.id.btn_clear_history)
        val backButton = findViewById<ImageButton>(R.id.buttonBack)
        inputEditText = findViewById(R.id.inputEditText)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        updateBtn = findViewById(R.id.btn_update)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        progressBar = findViewById(R.id.progressBar)
        placeholderImg = findViewById(R.id.placeholder_img)
        placeholderMessage = findViewById(R.id.placeholderMessage)
        additionalMes = findViewById(R.id.additionalMes)

        historyView.visibility = View.GONE

        historyInteractor = Creator.provideHistoryInteractor(this)

//        Log.d("HistLBef", historyList.toString())
        val historyList = historyInteractor.getHistoryFromSph()
        historyAdapter = TrackAdapter(historyList, this)
        historyAdapter.notifyDataSetChanged()

        Log.d("HistLAft", historyList.toString())

        historyRecyclerView = findViewById<RecyclerView>(R.id.recyclerViewHistory)
        historyRecyclerView.layoutManager = LinearLayoutManager(this)
        historyRecyclerView.adapter = historyAdapter

        btn_clear_hist.setOnClickListener{
            historyView.visibility = View.GONE
            historyList.clear()
            historyInteractor.clearHistory()
            historyAdapter.notifyDataSetChanged()
        }

        backButton.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            trackList.clear()
            recyclerView.visibility = View.GONE
            placeholderMessage.visibility = View.GONE
            placeholderImg.visibility = View.GONE
            additionalMes.visibility = View.GONE
            updateBtn.visibility = View.GONE
            inputEditText.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchQuery = s
                clearButton.visibility = clearButtonVisibility(s)
                historyView.visibility = if (inputEditText.hasFocus() && s?.isEmpty() == true && historyList.isNotEmpty()) View.VISIBLE else View.GONE
                progressBar.visibility = if (inputEditText.hasFocus() && s?.isNotEmpty() == true) View.VISIBLE else View.GONE
                searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchRequest()
                true
            }
            false
        }
        updateBtn.setOnClickListener{
                progressBar.visibility = View.VISIBLE
                searchDebounce()
        }
        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            historyView.visibility = if (hasFocus && inputEditText.text.isEmpty() && historyList.isNotEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun searchRequest(){
        if (inputEditText.text.isNotEmpty()) {
            provideTrackInteractor.search(inputEditText.text.toString(),object: TrackInteractor.TrackConsumer<List<Track>?> {
                override fun consume(data: ConsumerData<List<Track>?>) {
                    handler.post {
                        when (data) {
                            is ConsumerData.Data -> {
                                progressBar.visibility = View.GONE
                                recyclerView.visibility = View.VISIBLE
                                trackList.clear()
                                val results = data.data
                                if (results!!.isNotEmpty()) {
                                    trackList.addAll(results)
                                    adapter.notifyDataSetChanged()
                                }
                                if (results.isEmpty()) {
                                    showMessage(getString(R.string.nothing_found), "")
                                } else {
                                    showMessage("", "")
                                }
                            }

                            is ConsumerData.Error -> {
                                progressBar.visibility = View.GONE
                                showMessage(
                                    getString(R.string.something_went_wrong),
                                    getString(R.string.additional_message)
                                )
                                updateBtn.visibility = View.VISIBLE
                            }

                        }
                    }
                }

            })
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_NAME, searchQuery.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val editText = findViewById<EditText>(R.id.inputEditText)
        searchQuery = savedInstanceState.getString(SEARCH_NAME, null)
        editText.setText(searchQuery)
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
        placeholderMessage = findViewById(R.id.placeholderMessage)
        additionalMes = findViewById(R.id.additionalMes)
        placeholderImg = findViewById(R.id.placeholder_img)
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

        val displayIntent = Intent(this, PlayerActivity::class.java)
        val strTrack = Gson().toJson(track)

        if(clickDebounce()) {
            displayIntent.putExtra(NAME_TRACK, strTrack)
            startActivity(displayIntent)
            historyInteractor.addInHistory(track)
            val historyList = historyInteractor.getHistoryFromSph()
            historyAdapter = TrackAdapter(historyList, this)
            historyAdapter.notifyDataSetChanged()
            historyRecyclerView.layoutManager = LinearLayoutManager(this)
            historyRecyclerView.adapter = historyAdapter
            Log.d("HistLClick", historyList.toString())
        }
    }


    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        recyclerView.visibility = View.GONE
        placeholderMessage.visibility = View.GONE
        additionalMes.visibility = View.GONE
        placeholderImg.visibility = View.GONE
        updateBtn.visibility = View.GONE
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }
}