package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class SearchHistory {

    fun getHistoryFromSpH(sharedPref: SharedPreferences): ArrayList<Track> {
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        val json = sharedPref.getString(HISTORY_KEY, null)
            ?: return ArrayList()
        return Gson().fromJson(json, type)
    }

    fun createJsonFromTrack(trackList: ArrayList<Track>): String {
        return Gson().toJson(trackList)
    }
}