package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.text.method.TextKeyListener.clear
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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Locale

const val HISTORY_PREFS = "history_prefs"
const val HISTORY_KEY = "key_for_hist"
const val NAME_TRACK = "name"
private const val SEARCH_DEBOUNCE_DELAY = 2000L
private const val CLICK_DEBOUNCE_DELAY = 1000L

class SearchActivity : AppCompatActivity(), TrackAdapter.Listener {
    private var searchQuery: CharSequence? = null
    private val baseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val trackService = retrofit.create(iTunesApi::class.java)
    private val trackList = ArrayList<Track>()
    val adapter = TrackAdapter(trackList, this)
    private lateinit var placeholderMessage: TextView
    private lateinit var additionalMes: TextView
    private lateinit var placeholderImg: ImageView
    private lateinit var inputEditText: EditText
    private lateinit var updateBtn: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    private var historyList:ArrayList<Track> = arrayListOf()
    var historyAdapter = TrackAdapter(historyList, this)

    private lateinit var changeListener: SharedPreferences.OnSharedPreferenceChangeListener
    private lateinit var sharedPreferences: SharedPreferences

    private val searchHist = SearchHistory()

    private val searchRunnable = Runnable { searchRequest() }

    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val historyView = findViewById<LinearLayout>(R.id.historyView)
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

        sharedPreferences = getSharedPreferences(HISTORY_PREFS, MODE_PRIVATE)
        historyList = searchHist.getHistoryFromSpH(sharedPreferences)
        historyAdapter = TrackAdapter(historyList, this)

        val historyRecyclerView = findViewById<RecyclerView>(R.id.recyclerViewHistory)
        historyRecyclerView.layoutManager = LinearLayoutManager(this)
        historyRecyclerView.adapter = historyAdapter
        historyAdapter.notifyDataSetChanged()

        changeListener =
            SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
                if (key == HISTORY_KEY) {
                    val track = sharedPreferences?.getString(HISTORY_KEY, null)
                    if (track != null) {
                        historyAdapter.notifyItemInserted(0)
                    }
                }
            }

        sharedPreferences.registerOnSharedPreferenceChangeListener(changeListener)

        btn_clear_hist.setOnClickListener{
            historyView.visibility = View.GONE
            historyList.clear()
            historyAdapter.notifyDataSetChanged()
            sharedPreferences.edit()
                .clear()
                .apply()
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
            trackService.search(inputEditText.text.toString()).enqueue(object :
                Callback<TrackResponse> {
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ) {
                    if (response.code() == 200) {
                        progressBar.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        trackList.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            trackList.addAll(response.body()?.results!!)
                            adapter.notifyDataSetChanged()
                        }
                        if (trackList.isEmpty()) {
                            showMessage(getString(R.string.nothing_found), "")
                        } else {
                            showMessage("", "")
                        }
                    } else {
                        showMessage(
                            getString(R.string.something_went_wrong),
                            getString(R.string.additional_message)
                        )
                        updateBtn.visibility = View.VISIBLE
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    showMessage(
                        getString(R.string.something_went_wrong),
                        getString(R.string.additional_message)
                    )
                    updateBtn.visibility = View.VISIBLE
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

            sharedPreferences = getSharedPreferences(HISTORY_PREFS, MODE_PRIVATE)

            for (song in historyList) {
                if (track.trackId == song.trackId) {
                    historyList.remove(song)
                    historyAdapter.notifyDataSetChanged()
                    break
                }
            }
            if (historyList.size < 10) {
                historyList.add(0, track)
                historyAdapter.notifyDataSetChanged()
            } else {
                historyList.removeAt(9)
                historyList.add(0, track)
                historyAdapter.notifyDataSetChanged()
            }

            sharedPreferences.edit()
                .putString(HISTORY_KEY, searchHist.createJsonFromTrack(historyList))
                .apply()

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