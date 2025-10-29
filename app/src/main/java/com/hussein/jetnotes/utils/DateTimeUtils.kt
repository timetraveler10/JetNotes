package com.hussein.jetnotes.utils

import java.text.SimpleDateFormat
import java.util.Date

object DateTimeUtils {
    fun formatTimeStamp(timestamp: Long): String {
        val sdf = SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a")
        return sdf.format(Date(timestamp))
    }
    fun parseDateString(dateString: String): Long  = TODO()
}