package com.example.playlistmaker.search.data.sharedPreferences

import android.content.Context
import android.util.Log
import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.ui.HISTORY_KEY
import com.example.playlistmaker.search.ui.HISTORY_PREFS
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

        historyList = getHistoryFromSph()

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

        Log.d("add1", historyList.toString())

    }

    override fun clearHistory() {
        sharedPreferences.edit()
            .clear()
            .apply()
        Log.d("clear1", getHistoryFromSph().toString())

    }
}
