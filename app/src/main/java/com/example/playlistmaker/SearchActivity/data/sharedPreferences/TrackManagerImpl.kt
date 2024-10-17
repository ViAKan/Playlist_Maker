package com.example.playlistmaker.SearchActivity.data.sharedPreferences

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.example.playlistmaker.SearchActivity.data.dto.TrackDto
import com.example.playlistmaker.SearchActivity.domain.models.Track
import com.example.playlistmaker.SearchActivity.ui.HISTORY_KEY
import com.example.playlistmaker.SearchActivity.ui.HISTORY_PREFS
import com.example.playlistmaker.SearchActivity.ui.TrackAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TrackManagerImpl(private val context: Context): TrackManager {

    private var sharedPreferences = context.getSharedPreferences(HISTORY_PREFS, Context.MODE_PRIVATE)
    var historyList: ArrayList<TrackDto> = getHistoryFromSph()

    override fun getHistoryFromSph(): ArrayList<TrackDto> {
        val type = object : TypeToken<ArrayList<TrackDto>>() {}.type
        val json = sharedPreferences.getString(HISTORY_KEY, null)
            ?: return ArrayList()
        return Gson().fromJson(json, type)
    }

    override fun addInHistory(track: TrackDto) {

        for (song in historyList) {
            if (track.trackId == song.trackId) {
                historyList.remove(song)
                break
            }
        }
        if (historyList.size < 10) {
            historyList.add(0, track)
        } else {
            historyList.removeAt(9)
            historyList.add(0, track)
        }

        sharedPreferences.edit()
            .putString(HISTORY_KEY, Gson().toJson(historyList))
            .apply()

        Log.d("HistLMan", historyList.toString())
    }

    override fun clearHistory() {
        sharedPreferences.edit()
            .clear()
            .apply()

    }
}
