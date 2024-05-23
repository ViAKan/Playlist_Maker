package com.example.playlistmaker

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.random.Random

class SearchActivity : Activity() {
    private var searchQuery: CharSequence? = null
    private val baseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val trackService = retrofit.create(iTunesApi::class.java)
    private val trackList = ArrayList<Track>()
    val adapter = TrackAdapter(trackList)
    private lateinit var placeholderMessage: TextView
    private lateinit var additionalMes: TextView
    private lateinit var placeholderImg: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val backButton = findViewById<ImageButton>(R.id.buttonBack)
        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        val updateBtn = findViewById<Button>(R.id.btn_update)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

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
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                trackService.search(inputEditText.text.toString()).enqueue(object :
                    Callback<TrackResponse> {
                    override fun onResponse(call: Call<TrackResponse>,
                                            response: Response<TrackResponse>
                    ) {
                        if (response.code() == 200) {
                            trackList.clear()
                            recyclerView.visibility = View.VISIBLE
                            if (response.body()?.results?.isNotEmpty() == true) {
                                trackList.addAll(response.body()?.results!!)
                                adapter.notifyDataSetChanged()
                            }
                            if (trackList.isEmpty()) {
                                showMessage(getString(R.string.nothing_found),"")
                            }
                            else {
                                showMessage("", "")
                            }
                        }
                        else {
                            showMessage(getString(R.string.something_went_wrong),getString(R.string.additional_message))
                            updateBtn.visibility = View.VISIBLE
                        }
                    }

                    override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                        showMessage(getString(R.string.something_went_wrong), getString(R.string.additional_message))
                        updateBtn.visibility = View.VISIBLE
                    }

                })
                true
            }
            false
        }
        updateBtn.setOnClickListener{
                trackService.search(inputEditText.text.toString()).enqueue(object :
                    Callback<TrackResponse> {
                    override fun onResponse(call: Call<TrackResponse>,
                                            response: Response<TrackResponse>
                    ) {
                        if (response.code() == 200) {
                            updateBtn.visibility = View.GONE
                            recyclerView.visibility = View.VISIBLE
                            trackList.clear()
                            if (response.body()?.results?.isNotEmpty() == true) {
                                trackList.addAll(response.body()?.results!!)
                                adapter.notifyDataSetChanged()
                            }
                            if (trackList.isEmpty()) {
                                showMessage(getString(R.string.nothing_found),"")
                            }
                            else {
                                showMessage("", "")
                            }
                        }
                        else {
                            showMessage(getString(R.string.something_went_wrong),getString(R.string.additional_message))
                            updateBtn.visibility = View.VISIBLE
                        }
                    }

                    override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                        showMessage(getString(R.string.something_went_wrong), getString(R.string.additional_message))
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
}