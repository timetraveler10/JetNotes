package com.hussein.jetnotes.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Reminder(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0 ,
    val title: String,
    val checked: Boolean ,
    val timeStamp: Long,
    val remindAt: Long
)