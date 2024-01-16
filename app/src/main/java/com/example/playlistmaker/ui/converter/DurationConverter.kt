package com.example.playlistmaker.ui.converter

import java.text.SimpleDateFormat
import java.util.*

class DurationConverter {
    fun milsToMinSec(milliseconds: Long): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(milliseconds)
    }
}
