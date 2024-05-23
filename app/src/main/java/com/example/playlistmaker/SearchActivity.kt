package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.random.Random

class SearchActivity : AppCompatActivity() {
    private var searchQuery: CharSequence? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val backButton = findViewById<ImageButton>(R.id.buttonBack)
        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)

        inputEditText.requestFocus()

        backButton.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
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

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        //Track 1
        val name1 = resources.getString(R.string.track1_name)
        val time1 = resources.getString(R.string.track1_time)
        val author1 = resources.getString(R.string.track1_author)
        val imgSrc1 = resources.getString(R.string.track1_imageSource)
        //Track 2
        val name2 = resources.getString(R.string.track2_name)
        val time2 = resources.getString(R.string.track2_time)
        val author2 = resources.getString(R.string.track2_author)
        val imgSrc2 = resources.getString(R.string.track2_imageSource)
        //Track 3
        val name3 = resources.getString(R.string.track3_name)
        val time3 = resources.getString(R.string.track3_time)
        val author3 = resources.getString(R.string.track3_author)
        val imgSrc3 = resources.getString(R.string.track3_imageSource)
        //Track 4
        val name4 = resources.getString(R.string.track4_name)
        val time4 = resources.getString(R.string.track4_time)
        val author4 = resources.getString(R.string.track4_author)
        val imgSrc4 = resources.getString(R.string.track4_imageSource)
        //Track 5
        val name5 = resources.getString(R.string.track5_name)
        val time5 = resources.getString(R.string.track5_time)
        val author5 = resources.getString(R.string.track5_author)
        val imgSrc5 = resources.getString(R.string.track5_imageSource)

        val trackList: List<Track> = listOf(Track(name1, author1, time1, imgSrc1), Track(name2, author2, time2, imgSrc2), Track(name3, author3, time3, imgSrc3), Track(name4, author4, time4, imgSrc4), Track(name5, author5, time5, imgSrc5) )

        val trackAdapter = TrackAdapter(trackList)
        recyclerView.adapter = trackAdapter

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
}